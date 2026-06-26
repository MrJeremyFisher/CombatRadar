package com.aleksey.combatradar;

import com.aleksey.combatradar.config.RadarConfig;
import com.aleksey.combatradar.gui.screens.MainScreen;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import java.io.File;
import java.util.List;

public class ModHelper {
    private Logger _logger;
    private RadarConfig _config;
    private Radar _radar;
    private Speedometer _speedometer;
    private KeyMapping.Category _category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("combatradar", "keybind"));
    private static final RenderPipeline.Snippet UNIFORM_SNIPPET =
            RenderPipeline.builder()
                    .withVertexShader("core/position_color")
                    .withFragmentShader("core/position_color")
                    .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
                    .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
                    .buildSnippet();
    public static final RenderPipeline TRIANGLES =
            RenderPipeline.builder(UNIFORM_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath("combatradar", "pipelines/triangles"))
                    .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
                    .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
                    .build();
    public static final RenderPipeline LINES =
            RenderPipeline.builder(UNIFORM_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath("combatradar", "pipelines/lines"))
                    .withPrimitiveTopology(PrimitiveTopology.QUADS)
                    .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
                    .build();
    public static final RenderPipeline CIRCLE =
            RenderPipeline.builder(UNIFORM_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath("combatradar", "pipelines/circle"))
                    .withPrimitiveTopology(PrimitiveTopology.TRIANGLE_FAN)
                    .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
                    .build();
    public static final RenderPipeline BORDER =
            RenderPipeline.builder(UNIFORM_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath("combatradar", "pipelines/border"))
                    .withPrimitiveTopology(PrimitiveTopology.TRIANGLE_STRIP)
                    .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
                    .build();

    private final KeyMapping _settingsKey;

    public ModHelper() {
        _settingsKey = new KeyMapping("Combat Radar Settings", GLFW.GLFW_KEY_R, _category);
    }

    public KeyMapping getSettingsKey() {
        return _settingsKey;
    }

    public KeyMapping.Category getKeyBindCategory() {
        return _category;
    }

    public void init(Logger logger) {
        _logger = logger;

        File gameDirectory = Minecraft.getInstance().gameDirectory;
        File configDir = new File(gameDirectory, "/combatradar/");
        if (!configDir.isDirectory())
            configDir.mkdir();

        File configFile = new File(configDir, "config.json");

        _config = new RadarConfig(configFile, _settingsKey);

        if (!configFile.isFile()) {
            try {
                configFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            _config.save();
        } else {
            if (!_config.load())
                _config.save();
        }

        _config.setIsJourneyMapEnabled(isJourneyMapEnabled());
        _config.setIsVoxelMapEnabled(isVoxelMapEnabled());
        _config.setIsXaerosEnabled(isXaerosEnabled());

        _radar = new Radar(_config);
        _speedometer = new Speedometer();
    }

    public void tick() {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level == null)
            return;

        if (_config.getPingsEnabled() || _config.getEnabled()) {
            _radar.scanEntities();
        }

        if (_config.getEnabled()) {
            _radar.calcSettings();

            if (_config.getSpeedometerEnabled())
                _speedometer.calc();
        }

        if (_config.getPingsEnabled()) {
            _radar.sendMessages();
            _radar.playSounds();
        }

        if (!minecraft.gui.hud.isHidden() && minecraft.gui.screen() == null && _config.getSettingsKey().consumeClick()) {
            var windowId = minecraft.getWindow();

            if (InputConstants.isKeyDown(windowId, GLFW.GLFW_KEY_LEFT_CONTROL)
                    || InputConstants.isKeyDown(windowId, GLFW.GLFW_KEY_RIGHT_CONTROL)
            ) {
                if (InputConstants.isKeyDown(windowId, GLFW.GLFW_KEY_LEFT_ALT)
                        || InputConstants.isKeyDown(windowId, GLFW.GLFW_KEY_RIGHT_ALT)
                ) {
                    _config.setEnabled(!_config.getEnabled());
                    _config.save();
                } else {
                    _config.revertNeutralAggressive();
                    _config.save();
                }
            } else {
                minecraft.gui.setScreen(new MainScreen(minecraft.gui.screen(), _config, _speedometer));
            }
        }
    }

    public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();

        if (!_config.getEnabled()
                || minecraft.level == null
                || minecraft.gui.hud.isHidden()
                || minecraft.debugEntries.isOverlayVisible()
        ) {
            return;
        }

        _radar.render(guiGraphics, deltaTracker);

        if (_config.getSpeedometerEnabled())
            _speedometer.render(guiGraphics, _radar.getRadarDisplayX(), _radar.getRadarDisplayY(), _radar.getRadarRadius());
    }

    public boolean processChat(Component message) {
        if (!_config.getLogPlayerStatus() || message == null)
            return false;

        TextColor color1 = message.getStyle().getColor();

        List<Component> siblings = message.getSiblings();
        TextColor color2 = !siblings.isEmpty() ? siblings.getFirst().getStyle().getColor() : null;

        TextColor yellow = TextColor.fromLegacyFormat(ChatFormatting.YELLOW);

        if (yellow.equals(color1) || yellow.equals(color2)) {
            String messageText = message.getString();
            if (!messageText.contains("[CR]")) {
                return messageText.contains(" joined the game") || messageText.contains(" left the game");
            }
        }

        return false;
    }

    private boolean isJourneyMapEnabled() {
        try {
            Class.forName("journeymap.common.Journeymap");
        } catch (ClassNotFoundException ex) {
            _logger.info("[CombatRadar]: JourneyMap not found");
            return false;
        }

        _logger.info("[CombatRadar]: JourneyMap found");

        return true;
    }

    private boolean isVoxelMapEnabled() {
        try {
            Class.forName("com.mamiyaotaru.voxelmap.VoxelMap");
        } catch (ClassNotFoundException ex) {
            _logger.info("[CombatRadar]: VoxelMap not found");
            return false;
        }

        _logger.info("[CombatRadar]: VoxelMap found");

        return true;
    }

    private boolean isXaerosEnabled() {
        try {
            Class.forName("xaero.minimap.XaeroMinimap");
        } catch (ClassNotFoundException ex) {
            _logger.info("[CombatRadar]: Xaero's Minimap not found");
            return false;
        }

        _logger.info("[CombatRadar]: Xaero's Minimap found");

        return true;
    }

    public RadarConfig getConfig() {
        return _config;
    }

    public Speedometer getSpeedometer() {
        return _speedometer;
    }
}
