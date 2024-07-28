package com.aleksey.combatradar.gui.screens;

import com.aleksey.combatradar.Speedometer;
import com.aleksey.combatradar.config.RadarConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.awt.*;

/**
 * @author Aleksey Terzi
 */
public class MainScreen extends Screen {
    private final RadarConfig _config;
    private final Speedometer _speedometer;
    private final Screen _parent;
    private Button _playerStatusButton;
    private Button _speedometerButton;
    private Button _enableButton;
    private Button _showYLevelButton;
    private int _keyHintY;

    public MainScreen(Screen parent, RadarConfig config, Speedometer speedometer) {
        super(Component.literal("Combat Radar Settings"));
        _parent = parent;
        _config = config;
        _speedometer = speedometer;
    }

    @Override
    public void init() {
        int y = this.height / 4 - 16;
        int x = this.width / 2 - 100;

        final Screen screen = this;

        addRenderableWidget(Button.builder(Component.literal("Location and Color"), (btn) -> this.minecraft.setScreen(new LocationAndColorScreen(screen, _config))).bounds(x, y, 200, 20).build());

        y += 24;

        addRenderableWidget(Button.builder(Component.literal("Player Settings"), (btn) -> this.minecraft.setScreen(new PlayerSettingsScreen(screen, _config))).bounds(x, y, 200, 20).build());

        y += 24;

        addRenderableWidget(Button.builder(Component.literal("Radar Entities"), (btn) -> this.minecraft.setScreen(new EntityScreen(screen, _config))).bounds(x, y, 100, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Manage Players"), (btn) -> this.minecraft.setScreen(new ManagePlayersScreen(screen, _config))).bounds(x + 101, y, 100, 20).build());

        y += 24;

        _playerStatusButton = Button.builder(Component.literal("Log Players Statuses:"), (btn) -> {
            _config.setLogPlayerStatus(!_config.getLogPlayerStatus());
            _config.save();
        }).bounds(x, y, 200, 20).build();

        addRenderableWidget(_playerStatusButton);

        y += 24;

        _enableButton = Button.builder(Component.literal("Radar:"), (btn) -> {
            _config.setEnabled(!_config.getEnabled());
            _config.save();
        }).bounds(x, y, 100, 20).build();

        addRenderableWidget(_enableButton);

        _speedometerButton = Button.builder(Component.literal("Speed:"), (btn) -> {
            _config.setSpeedometerEnabled(!_config.getSpeedometerEnabled());
            _config.save();

            if (!_config.getSpeedometerEnabled())
                _speedometer.clearSpeed();
        }).bounds(x + 101, y, 100, 20).build();

        addRenderableWidget(_speedometerButton);

        y += 24;

        addRenderableWidget(_showYLevelButton = Button.builder(Component.literal("Use Y Levels:"), (btn) -> {
            _config.setShowYLevel(!_config.getShowYLevel());
            _config.save();
        }).bounds(x + 201, y, 100, 20).build());

        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (btn) -> this.minecraft.setScreen((_parent))).bounds(x, y, 200, 20).build());

        _keyHintY = y + 24;
    }

    @Override
    public void tick() {
        _playerStatusButton.setMessage(Component.literal("Log Players Statuses: " + (_config.getLogPlayerStatus() ? "On" : "Off")));
        _enableButton.setMessage(Component.literal("Radar: " + (_config.getEnabled() ? "On" : "Off")));
        _speedometerButton.setMessage(Component.literal("Speed: " + (_config.getSpeedometerEnabled() ? "On" : "Off")));
        _showYLevelButton.setMessage(Component.literal("Use Y Levels: " + (_config.getShowYLevel() ? "On" : "Off")));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        String keyName = _config.getSettingsKey().getTranslatedKeyMessage().getString().toUpperCase();

        RenderSystem.setShaderColor(1, 1, 1, 1);

        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 4 - 40, Color.WHITE.getRGB());
        guiGraphics.drawCenteredString(this.font, "Ctrl+Alt+" + keyName + " - enable/disable radar", this.width / 2, _keyHintY, Color.LIGHT_GRAY.getRGB());
        guiGraphics.drawCenteredString(this.font, "Ctrl+" + keyName + " - enable/disable mobs", this.width / 2, _keyHintY + 12, Color.LIGHT_GRAY.getRGB());

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderTransparentBackground(guiGraphics);
    }

    @Override
    public void renderTransparentBackground(GuiGraphics guiGraphics) {
        guiGraphics.fillGradient(0, 0, this.width, this.height, 0, 0);
    }
}
