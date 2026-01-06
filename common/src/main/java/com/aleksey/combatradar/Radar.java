package com.aleksey.combatradar;

import com.aleksey.combatradar.config.PlayerType;
import com.aleksey.combatradar.config.PlayerTypeInfo;
import com.aleksey.combatradar.config.RadarConfig;
import com.aleksey.combatradar.entities.CustomRadarEntity;
import com.aleksey.combatradar.entities.EntitySettings;
import com.aleksey.combatradar.entities.ItemRadarEntity;
import com.aleksey.combatradar.entities.LiveRadarEntity;
import com.aleksey.combatradar.entities.PlayerRadarEntity;
import com.aleksey.combatradar.entities.RadarEntity;
import com.aleksey.combatradar.gui.CircleBorderElementRenderState;
import com.aleksey.combatradar.gui.CircleElementRenderState;
import com.aleksey.combatradar.gui.LineElementRenderState;
import com.aleksey.combatradar.gui.TriangleElementRenderState;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author Aleksey Terzi
 */
public class Radar {
    private static final Pattern MinecraftSpecialCodes = Pattern.compile("(?i)§[0-9A-FK-OR]");
    private static final Logger log = LoggerFactory.getLogger(Radar.class);
    private static RadarConfig _config;
    private final List<RadarEntity> _entities = new ArrayList<>();
    private final HashMap<UUID, MessageInfo> _messages = new HashMap<>();
    private final List<PlayerSoundInfo> _sounds = new ArrayList<>();
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
    }

    private static Radar instance;

    public static @NotNull Radar getInstance() {
        return Objects.requireNonNull(instance);
    }

    public static @NotNull RadarConfig getConfig() {
        return Objects.requireNonNull(_config);
    }

    private static Component getJourneyMapCoord(PlayerInfo playerInfo) {
        Component hover = Component.literal("Click to add/edit waypoint.")
                .withStyle(ChatFormatting.WHITE);

        HoverEvent hoverEvent = new HoverEvent.ShowText(hover);
        ClickEvent clickEvent = new ClickEvent.RunCommand(
                "/jm waypoint create "
                        + playerInfo.playerName + " "
                        + Minecraft.getInstance().player.level().dimension().identifier() + " "
                        + (int) playerInfo.posX + " "
                        + (_config.getShowYLevel() ? (int) playerInfo.posY : "~") + " "
                        + (int) playerInfo.posZ + " "
                        + "aqua" + " "
                        + Minecraft.getInstance().player.getName().getString() + " "
                        + "false"
        );

        Style coordStyle = Style.EMPTY
                .withClickEvent(clickEvent)
                .withHoverEvent(hoverEvent)
                .withColor(ChatFormatting.AQUA);

        return Component.literal(getChatCoordText(playerInfo, false, true, _config.getShowYLevel())).setStyle(coordStyle);
    }

    private static Component getVoxelMapCoord(PlayerInfo playerInfo) {
        Component hover = Component.literal("Click to add/edit waypoint.")
                .withStyle(ChatFormatting.WHITE);

        HoverEvent hoverEvent = new HoverEvent.ShowText(hover);
        ClickEvent clickEvent = new ClickEvent.RunCommand("/newWaypoint "
                + "name:" + playerInfo.playerName + ", "
                + "x:" + (int) playerInfo.posX + ", "
                + "y:" + (int) (_config.getShowYLevel() ? playerInfo.posY : Minecraft.getInstance().player.getY()) + ", "
                + "z:" + (int) playerInfo.posZ + ", "
                + "dim:" + Minecraft.getInstance().player.level().dimension().identifier());

        Style coordStyle = Style.EMPTY
                .withClickEvent(clickEvent)
                .withHoverEvent(hoverEvent)
                .withColor(ChatFormatting.AQUA);

        return Component.literal(getChatCoordText(playerInfo, false, true, _config.getShowYLevel())).setStyle(coordStyle);
    }

    private static Component getXaerosCoord(PlayerInfo playerInfo) {
        Component hover = Component.literal("Click to add/edit waypoint.")
                .withStyle(ChatFormatting.WHITE);

        HoverEvent hoverEvent = new HoverEvent.ShowText(hover);
        ClickEvent clickEvent = new ClickEvent.RunCommand(
                "/xaero_waypoint_add:"
                        + playerInfo.playerName + ":"
                        + playerInfo.playerName.substring(0, 2) + ":"
                        + (int) playerInfo.posX + ":"
                        + (_config.getShowYLevel() ? (int) playerInfo.posY : "~") + ":"
                        + (int) playerInfo.posZ + ":"
                        + 0 + ":" // color
                        + false + ":" // should set rotation?
                        + 0 // yaw
        );

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

        if (_config.getIsJourneyMapEnabled() || _config.getIsVoxelMapEnabled()) {
            coordText.append("§'x:"); // §' Renders as nothing. Stupid hack for journey and voxel which are overzealous with their text replacement and modification
        } else {
            coordText.append("x:");
        }

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

    private static float getPartialX(Entity entity, float partialTicks) {
        return getPartial((float) entity.xOld, (float) entity.getX(), partialTicks);
    }

    private static float getPartialZ(Entity entity, float partialTicks) {
        return getPartial((float) entity.zOld, (float) entity.getZ(), partialTicks);
    }

    private static float getPartial(float oldValue, float newValue, float partialTicks) {
        return oldValue + (newValue - oldValue) * partialTicks;
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

    public void render(GuiGraphics guiGraphics, DeltaTracker partialTicks) {
        Matrix3x2fStack poseStack = guiGraphics.pose();

        if (_radarRadius == 0)
            return;

        float rotationYaw = Minecraft.getInstance().player.getViewYRot(partialTicks.getRealtimeDeltaTicks());

        poseStack.pushMatrix();
        poseStack.translate(_radarDisplayX, _radarDisplayY);

        poseStack.rotate(org.joml.Math.toRadians(-rotationYaw));

        renderCircleBg(guiGraphics, _radarRadius);
        renderCircleBorder(guiGraphics, _radarRadius);
        renderLines(guiGraphics, _radarRadius);

        renderNonPlayerEntities(guiGraphics, partialTicks.getRealtimeDeltaTicks());

        poseStack.rotate(org.joml.Math.toRadians(rotationYaw));
        renderTriangle(guiGraphics);

        poseStack.rotate(org.joml.Math.toRadians(-rotationYaw));
        renderPlayerEntities(guiGraphics, partialTicks.getRealtimeDeltaTicks());

        poseStack.popMatrix();
    }

    private void renderNonPlayerEntities(GuiGraphics guiGraphics, float partialTicks) {
        Matrix3x2fStack poseStack = guiGraphics.pose();
        Player player = Minecraft.getInstance().player;

        poseStack.pushMatrix();
        poseStack.scale(_radarScale, _radarScale);

        int i = 0;
        for (RadarEntity radarEntity : _entities) {
            if (!(radarEntity instanceof PlayerRadarEntity)) {

                renderEntity(guiGraphics, partialTicks, player, radarEntity);
                if (radarEntity instanceof ItemRadarEntity) {
                    i++;
                    if (i > 10000) { // TODO: configurable and maybe make per item type?
                        return;
                    }
                }
            }
        }

        poseStack.popMatrix();
    }

    private void renderPlayerEntities(GuiGraphics guiGraphics, float partialTicks) {
        Matrix3x2fStack poseStack = guiGraphics.pose();
        Player player = Minecraft.getInstance().player;

        poseStack.pushMatrix();
        poseStack.scale(_radarScale, _radarScale);

        for (RadarEntity radarEntity : _entities) {
            if (radarEntity instanceof PlayerRadarEntity) {
                renderEntity(guiGraphics, partialTicks, player, radarEntity);
            }
        }

        poseStack.popMatrix();
    }

    private void renderEntity(GuiGraphics guiGraphics, float partialTicks, Player player, RadarEntity radarEntity) {
        float displayX = getPartialX(player, partialTicks) - getPartialX(radarEntity.getEntity(), partialTicks);
        float displayZ = getPartialZ(player, partialTicks) - getPartialZ(radarEntity.getEntity(), partialTicks); // Convert to 2D where Z is Y
        double distance = Mth.length(displayX, displayZ);
        double scale = 1;

        if (distance > 0.1 && _config.getLogScaleEnabled()) {
            scale = (1 / distance) * (Math.log1p(distance) / Math.log1p(_config.getRadarDistance())) * _config.getRadarDistance();
        }
        radarEntity.render(guiGraphics, partialTicks, (float) (displayX * scale), (float) (displayZ * scale), Math.pow(distance, 2));
    }

    private void renderTriangle(GuiGraphics graphics) {
        graphics.pose().rotate(org.joml.Math.toRadians(180));

        GL11.glEnable(GL11.GL_POLYGON_SMOOTH);

        renderTriangle(graphics, 0xFF000000, 0);
        renderTriangle(graphics, 0xFFFFFFFF, 0.5f);

        GL11.glDisable(GL11.GL_POLYGON_SMOOTH);

        graphics.pose().rotate(org.joml.Math.toRadians(-180));
    }

    private void renderTriangle(GuiGraphics graphics, int color, float offset) {
        graphics.guiRenderState.submitGuiElement(new TriangleElementRenderState(
                ModHelper.TRIANGLES,
                new Matrix3x2f(graphics.pose()),
                graphics.scissorStack.peek(), offset, color
        ));
    }

    private void renderLines(GuiGraphics graphics, float radius) {
        GL11.glEnable(GL11.GL_POLYGON_SMOOTH);

        int color = ARGB.colorFromFloat(Math.clamp(_config.getRadarOpacity() + 0.5f, 0, 1), _config.getRadarColor().getRed() / 255.0f, _config.getRadarColor().getGreen() / 255.0f, _config.getRadarColor().getBlue() / 255.0f);
        graphics.guiRenderState.submitGuiElement(new LineElementRenderState(
                ModHelper.LINES,
                new Matrix3x2f(graphics.pose()),
                graphics.scissorStack.peek(), radius, color
        ));

        GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
    }

    private void renderCircleBg(GuiGraphics graphics, float radius) {
        int color = ARGB.colorFromFloat(_config.getRadarOpacity(), _config.getRadarColor().getRed() / 255.0f, _config.getRadarColor().getGreen() / 255.0f, _config.getRadarColor().getBlue() / 255.0f);
        graphics.guiRenderState.submitGuiElement(new CircleElementRenderState(
                ModHelper.CIRCLE,
                new Matrix3x2f(graphics.pose()),
                graphics.scissorStack.peek(), radius, color
        ));
    }

    private void renderCircleBorder(GuiGraphics graphics, float radius) {
        GL11.glEnable(GL11.GL_POLYGON_SMOOTH);

        int color = ARGB.colorFromFloat(Math.clamp(_config.getRadarOpacity() + 0.5f, 0, 1), _config.getRadarColor().getRed() / 255.0f, _config.getRadarColor().getGreen() / 255.0f, _config.getRadarColor().getBlue() / 255.0f);

        graphics.guiRenderState.submitGuiElement(new CircleBorderElementRenderState(
                ModHelper.BORDER,
                new Matrix3x2f(graphics.pose()),
                graphics.scissorStack.peek(), radius, color
        ));

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

            Identifier icon = _config.getEnabledIcon(entity);
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

    private void addEntity(Entity entity, EntitySettings settings, Map<UUID, PlayerInfo> oldPlayers, Identifier icon) {
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
            UUID playerKey = profile.id();

            if (playerKey.equals(currentPlayerId))
                continue;

            String playerName = profile.name();
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
            } else if (_config.getIsXaerosEnabled()) {
                coordText = getXaerosCoord(messageInfo.playerInfo);
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