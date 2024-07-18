package com.aleksey.combatradar;

import com.aleksey.combatradar.config.PlayerType;
import com.aleksey.combatradar.config.PlayerTypeInfo;
import com.aleksey.combatradar.config.RadarConfig;
import com.aleksey.combatradar.entities.*;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Aleksey Terzi
 */
public class Radar {
    private static final Pattern MinecraftSpecialCodes = Pattern.compile("(?i)§[0-9A-FK-OR]");
    private static RadarConfig _config;
    private final List<RadarEntity> _entities = new ArrayList<>();
    private final HashMap<UUID, MessageInfo> _messages = new HashMap<>();
    private final List<PlayerSoundInfo> _sounds = new ArrayList<>();
    private final float[] _sinList = new float[361];
    private final float[] _cosList = new float[361];
    // Calculated settings
    private int _radarRadius;
    private float _radarScale;
    private int _radarDisplayX;
    private int _radarDisplayY;
    private Map<UUID, PlayerInfo> _radarPlayers;
    private Map<UUID, String> _onlinePlayers;

    public Radar(RadarConfig config) {
        if (instance != null) {
            throw new IllegalStateException("Radar instance has already been initialized");
        }
        instance = this;

        _config = config;

        for (int i = 0; i <= 360; i++) {
            _sinList[i] = (float) Math.sin(i * Math.PI / 180.0D);
            _cosList[i] = (float) Math.cos(i * Math.PI / 180.0D);
        }
    }

    private static Radar instance;
    public static @NotNull Radar getInstance() {
        return Objects.requireNonNull(instance);
    }

    public static @NotNull RadarConfig getConfig() {
        return Objects.requireNonNull(_config);
    }

    private static Component getJourneyMapCoord(PlayerInfo playerInfo) {
        MutableComponent hover = Component.literal("JourneyMap: ")
                .withStyle(ChatFormatting.YELLOW)
                .append(Component.literal("Click to create Waypoint.\nCtrl+Click to view on map.")
                        .withStyle(ChatFormatting.AQUA)
                );

        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover);
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/jm wpedit " + getChatCoordText(playerInfo, true, true, _config.getShowYLevel()));

        Style coordStyle = Style.EMPTY
                .withClickEvent(clickEvent)
                .withHoverEvent(hoverEvent)
                .withColor(ChatFormatting.AQUA);

        return Component.literal(getChatCoordText(playerInfo, false, true, _config.getShowYLevel())).setStyle(coordStyle);
    }

    private static Component getVoxelMapCoord(PlayerInfo playerInfo) {
        Component hover = Component.literal("Click to highlight coordinate,\nor Ctrl-Click to add/edit waypoint.")
                .withStyle(ChatFormatting.WHITE);

        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover);
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/newWaypoint " + getChatCoordText(playerInfo, true, false, _config.getShowYLevel()));

        Style coordStyle = Style.EMPTY
                .withClickEvent(clickEvent)
                .withHoverEvent(hoverEvent)
                .withColor(ChatFormatting.AQUA);

        return Component.literal(getChatCoordText(playerInfo, false, true, _config.getShowYLevel())).setStyle(coordStyle);
    }

    private static String getChatCoordText(PlayerInfo playerInfo, boolean includeName, boolean includeBrackets, boolean includeY) {
        StringBuilder coordText = new StringBuilder();

        if (includeBrackets) {
            coordText.append("[");
        }

        coordText.append("x:");
        coordText.append((int) playerInfo.posX);
        if (includeY) {
            coordText.append(", y:");
            coordText.append((int) playerInfo.posY);
        }
        coordText.append(", z:");
        coordText.append((int) playerInfo.posZ);

        if (includeName) {
            coordText.append(", name:");
            coordText.append(playerInfo.playerName);
        }

        if (includeBrackets) {
            coordText.append("]");
        }

        return coordText.toString();
    }

    public int getRadarRadius() {
        return _radarRadius;
    }

    public int getRadarDisplayX() {
        return _radarDisplayX;
    }

    public int getRadarDisplayY() {
        return _radarDisplayY;
    }

    public void calcSettings() {
        Window window = Minecraft.getInstance().getWindow();
        int radarDiameter = (int) ((window.getGuiScaledHeight() - 2) * _config.getRadarSize());

        _radarRadius = radarDiameter / 2;

        int windowInnerWidth = window.getGuiScaledWidth() - radarDiameter;
        int windowInnerHeight = window.getGuiScaledHeight() - radarDiameter;

        _radarDisplayX = _radarRadius + 1 + (int) (_config.getRadarX() * (windowInnerWidth - 2));
        _radarDisplayY = _radarRadius + 1 + (int) (_config.getRadarY() * (windowInnerHeight - 2));

        _radarScale = (float) _radarRadius / _config.getRadarDistance();
    }

    public void render(GuiGraphics guiGraphics, float partialTicks) {
        PoseStack poseStack = guiGraphics.pose();

        if (_radarRadius == 0)
            return;

        float rotationYaw = Minecraft.getInstance().player.getViewYRot(partialTicks);

        poseStack.pushPose();
        poseStack.translate(_radarDisplayX, _radarDisplayY, 0);
        poseStack.mulPose(Axis.ZP.rotationDegrees(-rotationYaw));

        renderCircleBg(poseStack, _radarRadius);
        renderCircleBorder(poseStack, _radarRadius);
        renderLines(poseStack, _radarRadius);

        renderNonPlayerEntities(guiGraphics, partialTicks);

        poseStack.mulPose(Axis.ZP.rotationDegrees(rotationYaw));
        renderTriangle(poseStack);

        poseStack.mulPose(Axis.ZP.rotationDegrees(-rotationYaw));
        renderPlayerEntities(guiGraphics, partialTicks);

        poseStack.popPose();
    }

    private void renderNonPlayerEntities(GuiGraphics guiGraphics, float partialTicks) {
        PoseStack poseStack = guiGraphics.pose();

        poseStack.pushPose();
        poseStack.scale(_radarScale, _radarScale, _radarScale);

        for (RadarEntity radarEntity : _entities) {
            if (!(radarEntity instanceof PlayerRadarEntity))
                radarEntity.render(guiGraphics, partialTicks);
        }

        poseStack.popPose();
    }

    private void renderPlayerEntities(GuiGraphics guiGraphics, float partialTicks) {
        PoseStack poseStack = guiGraphics.pose();

        poseStack.pushPose();
        poseStack.scale(_radarScale, _radarScale, _radarScale);

        for (RadarEntity radarEntity : _entities) {
            if (radarEntity instanceof PlayerRadarEntity)
                radarEntity.render(guiGraphics, partialTicks);
        }

        poseStack.popPose();
    }

    private void renderTriangle(PoseStack poseStack) {
        Matrix4f lastPose = poseStack.last().pose();

        poseStack.mulPose(Axis.ZP.rotationDegrees(180));

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionShader);

        GL11.glEnable(GL11.GL_POLYGON_SMOOTH);

        renderTriangle(lastPose, 0, 0);
        renderTriangle(lastPose, 1, 0.5f);

        GL11.glDisable(GL11.GL_POLYGON_SMOOTH);

        RenderSystem.disableBlend();

        poseStack.mulPose(Axis.ZP.rotationDegrees(-180));
    }

    private void renderTriangle(Matrix4f lastPose, float color, float offset) {
        RenderSystem.setShaderColor(color, color, color, 1);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION);
        buffer.vertex(lastPose, 0f, 3f - offset, 0).endVertex();
        buffer.vertex(lastPose, 3f - offset, -3f + offset, 0).endVertex();
        buffer.vertex(lastPose, -3f + offset, -3f + offset, 0).endVertex();
        tesselator.end();
    }

    private void renderLines(PoseStack poseStack, float radius) {
        final float cos45 = 0.7071f;
        final float a = 0.25f;
        float length = radius - a;
        float d = cos45 * length;
        float c = d + a / cos45;

        float opacity = _config.getRadarOpacity() + 0.5f;
        Matrix4f lastPose = poseStack.last().pose();

        GL11.glEnable(GL11.GL_POLYGON_SMOOTH);

        RenderSystem.setShaderColor(_config.getRadarColor().getRed() / 255.0f, _config.getRadarColor().getGreen() / 255.0f, _config.getRadarColor().getBlue() / 255.0f, opacity);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

        buffer.vertex(lastPose, -a, -length, 0f).endVertex();
        buffer.vertex(lastPose, -a, length, 0f).endVertex();
        buffer.vertex(lastPose, a, length, 0f).endVertex();
        buffer.vertex(lastPose, a, -length, 0f).endVertex();

        buffer.vertex(lastPose, -length, a, 0f).endVertex();
        buffer.vertex(lastPose, length, a, 0f).endVertex();
        buffer.vertex(lastPose, length, -a, 0f).endVertex();
        buffer.vertex(lastPose, -length, -a, 0f).endVertex();

        buffer.vertex(lastPose, -c, -d, 0f).endVertex();
        buffer.vertex(lastPose, d, c, 0f).endVertex();
        buffer.vertex(lastPose, c, d, 0f).endVertex();
        buffer.vertex(lastPose, -d, -c, 0f).endVertex();

        buffer.vertex(lastPose, -d, c, 0f).endVertex();
        buffer.vertex(lastPose, c, -d, 0f).endVertex();
        buffer.vertex(lastPose, d, -c, 0f).endVertex();
        buffer.vertex(lastPose, -c, d, 0f).endVertex();

        tesselator.end();
        RenderSystem.disableBlend();

        GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
    }

    private void renderCircleBg(PoseStack poseStack, float radius) {
        float opacity = _config.getRadarOpacity();
        Matrix4f lastPose = poseStack.last().pose();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(_config.getRadarColor().getRed() / 255.0f, _config.getRadarColor().getGreen() / 255.0f, _config.getRadarColor().getBlue() / 255.0f, opacity);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION);

        for (int i = 0; i <= 360; i++) {
            float x = _sinList[i] * radius;
            float y = _cosList[i] * radius;
            buffer.vertex(lastPose, x, y, 0).endVertex();
        }

        tesselator.end();
        RenderSystem.disableBlend();
    }

    private void renderCircleBorder(PoseStack poseStack, float radius) {
        float opacity = _config.getRadarOpacity() + 0.5f;
        Matrix4f lastPose = poseStack.last().pose();

        GL11.glEnable(GL11.GL_POLYGON_SMOOTH);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(_config.getRadarColor().getRed() / 255.0f, _config.getRadarColor().getGreen() / 255.0f, _config.getRadarColor().getBlue() / 255.0f, opacity);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION);

        for (int i = 0; i <= 360; i++) {
            float sin = _sinList[i];
            float cos = _cosList[i];
            float x1 = sin * (radius - 0.5f);
            float y1 = cos * (radius - 0.5f);
            float x2 = sin * radius;
            float y2 = cos * radius;

            buffer.vertex(lastPose, x1, y1, 0).endVertex();
            buffer.vertex(lastPose, x2, y2, 0).endVertex();
        }

        tesselator.end();
        RenderSystem.disableBlend();

        GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
    }

    public void scanEntities() {
        _entities.clear();
        _sounds.clear();
        _messages.clear();

        scanRadarEntities();

        if (_config.getLogPlayerStatus()) {
            scanOnlinePlayers();
        }

    }

    private void scanRadarEntities() {
        Minecraft minecraft = Minecraft.getInstance();

        Map<UUID, PlayerInfo> oldPlayers = _radarPlayers;

        _radarPlayers = new HashMap<>();

        EntitySettings settings = createEntitySettings();

        Iterable<Entity> entities = minecraft.level.entitiesForRendering();
        for (Entity entity : entities) {
            if (entity == minecraft.player)
                continue;

            ResourceLocation icon = _config.getEnabledIcon(entity);
            if (icon != null)
                addEntity(entity, settings, oldPlayers, icon);
        }

        if (oldPlayers != null) {
            for (UUID playerKey : oldPlayers.keySet()) {
                PlayerInfo playerInfo = oldPlayers.get(playerKey);
                PlayerType playerType = _config.getPlayerType(playerInfo.playerName);
                PlayerTypeInfo playerTypeInfo = _config.getPlayerTypeInfo(playerType);

                if (playerTypeInfo.ping || _config.getLogPlayerStatus()) {
                    _messages.put(playerKey, new MessageInfo(playerInfo, MessageReason.Disappeared, playerTypeInfo.ping));
                }
            }
        }
    }

    private void addEntity(Entity entity, EntitySettings settings, Map<UUID, PlayerInfo> oldPlayers, ResourceLocation icon) {
        RadarEntity radarEntity;

        if (entity instanceof ExperienceOrb) {
            radarEntity = new CustomRadarEntity(entity, settings, icon);
        } else if (entity instanceof ItemEntity) {
            radarEntity = new ItemRadarEntity(entity, settings);
        } else if (entity instanceof RemotePlayer) {
            PlayerType playerType = _config.getPlayerType(entity.getScoreboardName());
            radarEntity = new PlayerRadarEntity(entity, settings, playerType);

            UUID playerKey = entity.getUUID();
            PlayerInfo playerInfo = new PlayerInfo((RemotePlayer) entity);

            _radarPlayers.put(playerKey, playerInfo);

            if (oldPlayers == null || !oldPlayers.containsKey(playerKey)) {
                PlayerTypeInfo playerTypeInfo = _config.getPlayerTypeInfo(playerType);

                if (playerTypeInfo.ping) {
                    _sounds.add(new PlayerSoundInfo(playerTypeInfo.soundEventName, playerKey));
                }

                if (playerTypeInfo.ping || _config.getLogPlayerStatus()) {
                    _messages.put(playerKey, new MessageInfo(playerInfo, MessageReason.Appeared, playerTypeInfo.ping));
                }
            } else {
                oldPlayers.remove(playerKey);
            }
        } else if (entity instanceof ChestBoat) {
            radarEntity = new ItemRadarEntity(entity, settings, new ItemStack(Items.OAK_CHEST_BOAT));
        } else if (entity instanceof Boat) {
            radarEntity = new ItemRadarEntity(entity, settings, new ItemStack(Items.OAK_BOAT));
        } else if (entity instanceof AbstractMinecart) {
            radarEntity = new ItemRadarEntity(entity, settings, new ItemStack(Items.MINECART));
        } else {
            radarEntity = new LiveRadarEntity(entity, settings, icon);
        }

        _entities.add(radarEntity);
    }

    private EntitySettings createEntitySettings() {
        EntitySettings settings = new EntitySettings();
        settings.radarDistanceSq = _config.getRadarDistance() * _config.getRadarDistance();
        settings.iconScale = _config.getIconScale();
        settings.iconOpacity = 1;
        settings.radarScale = _radarScale;
        settings.fontScale = _config.getFontScale();
        settings.neutralPlayerColor = _config.getPlayerTypeInfo(PlayerType.Neutral).color;
        settings.allyPlayerColor = _config.getPlayerTypeInfo(PlayerType.Ally).color;
        settings.enemyPlayerColor = _config.getPlayerTypeInfo(PlayerType.Enemy).color;
        settings.showPlayerNames = _config.getShowPlayerNames();
        settings.showExtraPlayerInfo = _config.getShowExtraPlayerInfo();
        settings.showYLevel = _config.getShowYLevel();

        return settings;
    }

    private void scanOnlinePlayers() {
        Minecraft minecraft = Minecraft.getInstance();
        Collection<net.minecraft.client.multiplayer.PlayerInfo> players = minecraft.getConnection().getOnlinePlayers();
        Map<UUID, String> oldOnlinePlayers = _onlinePlayers;
        UUID currentPlayerId = minecraft.player.getUUID();

        _onlinePlayers = new HashMap<>();

        for (net.minecraft.client.multiplayer.PlayerInfo p : players) {
            GameProfile profile = p.getProfile();
            UUID playerKey = profile.getId();

            if (playerKey.equals(currentPlayerId))
                continue;

            String playerName = profile.getName();
            String playerNameTrimmed = MinecraftSpecialCodes.matcher(playerName).replaceAll("");
            if (_config.isPlayerExcluded(playerNameTrimmed))
                continue;

            _onlinePlayers.put(playerKey, playerName);

            if (oldOnlinePlayers == null || !oldOnlinePlayers.containsKey(playerKey)) {
                MessageInfo message = _messages.get(playerKey);

                if (message != null) {
                    message.reason = MessageReason.Login;
                    message.log = true;
                } else {
                    _messages.put(playerKey, new MessageInfo(playerName, MessageReason.Login));
                }
            } else {
                oldOnlinePlayers.remove(playerKey);
            }
        }

        if (oldOnlinePlayers != null) {
            for (UUID playerKey : oldOnlinePlayers.keySet()) {
                MessageInfo message = _messages.get(playerKey);

                if (message != null) {
                    message.reason = MessageReason.Logout;
                    message.log = true;
                } else {
                    _messages.put(playerKey, new MessageInfo(oldOnlinePlayers.get(playerKey), MessageReason.Logout));
                }
            }
        }
    }

    public void playSounds() {
        for (PlayerSoundInfo sound : _sounds) {
            SoundHelper.playSound(sound.soundEventName, sound.playerKey);
        }
    }

    public void sendMessages() {
        Minecraft minecraft = Minecraft.getInstance();

        for (MessageInfo message : _messages.values()) {
            if (message.log) {
                sendMessage(minecraft, message);
            }
        }
    }

    private void sendMessage(Minecraft minecraft, MessageInfo messageInfo) {
        MutableComponent text = Component.literal("[CR] ").withStyle(ChatFormatting.DARK_AQUA);

        ChatFormatting playerColor;
        PlayerType playerType = _config.getPlayerType(messageInfo.playerName);

        playerColor = switch (playerType) {
            case Ally -> ChatFormatting.GREEN;
            case Enemy -> ChatFormatting.DARK_RED;
            default -> ChatFormatting.WHITE;
        };

        text = text.append(Component.literal(messageInfo.playerName).withStyle(playerColor));

        String actionText;
        ChatFormatting actionColor;

        switch (messageInfo.reason) {
            case Login -> {
                actionText = " joined the game";
                actionColor = messageInfo.playerInfo != null ? ChatFormatting.YELLOW : ChatFormatting.DARK_GREEN;
            }
            case Logout -> {
                actionText = " left the game";
                actionColor = ChatFormatting.DARK_GREEN;
            }
            case Appeared -> {
                actionText = " appeared on radar";
                actionColor = ChatFormatting.YELLOW;
            }
            case Disappeared -> {
                actionText = " disappeared from radar";
                actionColor = ChatFormatting.YELLOW;
            }
            default -> {
                return;
            }
        }

        text = text.append(Component.literal(actionText).withStyle(actionColor));

        if (messageInfo.playerInfo != null) {
            Component coordText;

            if (_config.getIsJourneyMapEnabled()) {
                coordText = getJourneyMapCoord(messageInfo.playerInfo);
                text = text
                        .append(Component.literal(" at ").withStyle(actionColor))
                        .append(coordText);
            } else if (_config.getIsVoxelMapEnabled()) {
                coordText = getVoxelMapCoord(messageInfo.playerInfo);
                text = text
                        .append(Component.literal(" at ").withStyle(actionColor))
                        .append(coordText);
            } else {
                coordText = Component.literal(getChatCoordText(messageInfo.playerInfo, false, true, _config.getShowYLevel()))
                        .withStyle(actionColor);
                text = text
                        .append(Component.literal(" at ").withStyle(actionColor))
                        .append(coordText);
            }


        }

        minecraft.player.displayClientMessage(text, false);
    }

    private enum MessageReason {Login, Logout, Appeared, Disappeared}

    private static class PlayerInfo {
        public String playerName;
        public double posX;
        public double posY;
        public double posZ;

        public PlayerInfo(AbstractClientPlayer player) {
            this.playerName = player.getScoreboardName();
            this.posX = player.getX();
            this.posY = player.getY();
            this.posZ = player.getZ();
        }
    }

    private static class MessageInfo {
        public String playerName;
        public PlayerInfo playerInfo;
        public MessageReason reason;
        public boolean log;

        public MessageInfo(String playerName, MessageReason reason) {
            this.playerName = playerName;
            this.playerInfo = null;
            this.reason = reason;
            this.log = true;
        }

        public MessageInfo(PlayerInfo playerInfo, MessageReason reason, boolean log) {
            this.playerName = playerInfo.playerName;
            this.playerInfo = playerInfo;
            this.reason = reason;
            this.log = log;
        }
    }

    private static class PlayerSoundInfo {
        public String soundEventName;
        public UUID playerKey;

        public PlayerSoundInfo(String soundEventName, UUID playerKey) {
            this.soundEventName = soundEventName;
            this.playerKey = playerKey;
        }
    }
}