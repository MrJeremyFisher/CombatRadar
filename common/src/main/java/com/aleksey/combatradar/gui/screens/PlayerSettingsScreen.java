package com.aleksey.combatradar.gui.screens;

import com.aleksey.combatradar.config.PlayerType;
import com.aleksey.combatradar.config.PlayerTypeInfo;
import com.aleksey.combatradar.config.RadarConfig;
import com.aleksey.combatradar.config.SoundInfo;
import com.aleksey.combatradar.gui.components.SliderButton;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.*;

/**
 * @author Aleksey Terzi
 */
public class PlayerSettingsScreen extends Screen {
    private final RadarConfig _config;
    private final Screen _parent;
    private SliderButton _neutralRedSlider;
    private SliderButton _neutralGreenSlider;
    private SliderButton _neutralBlueSlider;
    private SliderButton _allyRedSlider;
    private SliderButton _allyGreenSlider;
    private SliderButton _allyBlueSlider;
    private SliderButton _enemyRedSlider;
    private SliderButton _enemyGreenSlider;
    private SliderButton _enemyBlueSlider;
    private Button _neutralPingButton;
    private Button _neutralSoundButton;
    private Button _allyPingButton;
    private Button _allySoundButton;
    private Button _enemyPingButton;
    private Button _enemySoundButton;


    public PlayerSettingsScreen(Screen parent, RadarConfig config) {
        super(Component.literal("Player Settings"));
        _parent = parent;
        _config = config;
    }

    @Override
    public void init() {
        int y = this.height / 4 - 16 + 12;
        int x = this.width / 2 - 100;

        PlayerTypeInfo neutralInfo = _config.getPlayerTypeInfo(PlayerType.Neutral);
        PlayerTypeInfo allyInfo = _config.getPlayerTypeInfo(PlayerType.Ally);
        PlayerTypeInfo enemyInfo = _config.getPlayerTypeInfo(PlayerType.Enemy);

        addRenderableWidget(_neutralRedSlider = new SliderButton(x, y, 66, 1, 0, "Red", neutralInfo.color.getRed() / 255f, false));
        addRenderableWidget(_neutralGreenSlider = new SliderButton(x + 66 + 1, y, 66, 1, 0, "Green", neutralInfo.color.getGreen() / 255f, false));
        addRenderableWidget(_neutralBlueSlider = new SliderButton(x + 66 + 1 + 66 + 1, y, 66, 1, 0, "Blue", neutralInfo.color.getBlue() / 255f, false));
        y += 24 + 12;
        addRenderableWidget(_allyRedSlider = new SliderButton(x, y, 66, 1, 0, "Red", allyInfo.color.getRed() / 255f, false));
        addRenderableWidget(_allyGreenSlider = new SliderButton(x + 66 + 1, y, 66, 1, 0, "Green", allyInfo.color.getGreen() / 255f, false));
        addRenderableWidget(_allyBlueSlider = new SliderButton(x + 66 + 1 + 66 + 1, y, 66, 1, 0, "Blue", allyInfo.color.getBlue() / 255f, false));
        y += 24 + 12;
        addRenderableWidget(_enemyRedSlider = new SliderButton(x, y, 66, 1, 0, "Red", enemyInfo.color.getRed() / 255f, false));
        addRenderableWidget(_enemyGreenSlider = new SliderButton(x + 66 + 1, y, 66, 1, 0, "Green", enemyInfo.color.getGreen() / 255f, false));
        addRenderableWidget(_enemyBlueSlider = new SliderButton(x + 66 + 1 + 66 + 1, y, 66, 1, 0, "Blue", enemyInfo.color.getBlue() / 255f, false));

        y += 24;

        addRenderableWidget(_neutralPingButton = Button.builder(Component.literal("Neutral Player Ping"), (btn) -> changePing(PlayerType.Neutral)).bounds(x, y, 133, 20).build());

        addRenderableWidget(_neutralSoundButton = Button.builder(Component.literal("Sound"), (btn) -> changePingSound(PlayerType.Neutral)).bounds(x + 133 + 1, y, 66, 20).build());

        y += 24;

        addRenderableWidget(_allyPingButton = Button.builder(Component.literal("Ally Player Ping"), (btn) -> changePing(PlayerType.Ally)).bounds(x, y, 133, 20).build());

        addRenderableWidget(_allySoundButton = Button.builder(Component.literal("Sound"), (btn) -> changePingSound(PlayerType.Ally)).bounds(x + 133 + 1, y, 66, 20).build());

        y += 24;

        addRenderableWidget(_enemyPingButton = Button.builder(Component.literal("Enemy Player Ping"), (btn) -> changePing(PlayerType.Enemy)).bounds(x, y, 133, 20).build());

        addRenderableWidget(_enemySoundButton = Button.builder(Component.literal("Sound"), (btn) -> changePingSound(PlayerType.Enemy)).bounds(x + 133 + 1, y, 66, 20).build());

        y += 24;

        addRenderableWidget(Button.builder(Component.literal("Done"), (btn) -> this.minecraft.setScreen(_parent)).bounds(x, y, 200, 20).build());
    }

    private void changePing(PlayerType playerType) {
        PlayerTypeInfo playerTypeInfo = _config.getPlayerTypeInfo(playerType);
        playerTypeInfo.ping = !playerTypeInfo.ping;
        _config.save();
    }

    private void changePingSound(PlayerType playerType) {
        this.minecraft.setScreen(new ChooseSoundScreen(this, _config, playerType));
    }

    @Override
    public void tick() {
        boolean isChanged;

        Color neutralColor = new Color(_neutralRedSlider.getValue(), _neutralGreenSlider.getValue(), _neutralBlueSlider.getValue());
        Color allyColor = new Color(_allyRedSlider.getValue(), _allyGreenSlider.getValue(), _allyBlueSlider.getValue());
        Color enemyColor = new Color(_enemyRedSlider.getValue(), _enemyGreenSlider.getValue(), _enemyBlueSlider.getValue());

        isChanged = changeColor(PlayerType.Neutral, neutralColor);
        isChanged = changeColor(PlayerType.Ally, allyColor) || isChanged;
        isChanged = changeColor(PlayerType.Enemy, enemyColor) || isChanged;

        if (isChanged)
            _config.save();

        PlayerTypeInfo neutralPlayer = _config.getPlayerTypeInfo(PlayerType.Neutral);
        PlayerTypeInfo allyPlayer = _config.getPlayerTypeInfo(PlayerType.Ally);
        PlayerTypeInfo enemyPlayer = _config.getPlayerTypeInfo(PlayerType.Enemy);

        _neutralPingButton.setMessage(Component.literal("Neutral Player Ping: " + (neutralPlayer.ping ? "On" : "Off")));
        _neutralSoundButton.setMessage(Component.literal(SoundInfo.getByValue(neutralPlayer.soundEventName).name));
        _allyPingButton.setMessage(Component.literal("Ally Player Ping: " + (allyPlayer.ping ? "On" : "Off")));
        _allySoundButton.setMessage(Component.literal(SoundInfo.getByValue(allyPlayer.soundEventName).name));
        _enemyPingButton.setMessage(Component.literal("Enemy Player Ping: " + (enemyPlayer.ping ? "On" : "Off")));
        _enemySoundButton.setMessage(Component.literal(SoundInfo.getByValue(enemyPlayer.soundEventName).name));
    }

    private boolean changeColor(PlayerType playerType, Color color) {
        PlayerTypeInfo info = _config.getPlayerTypeInfo(playerType);

        if (info.color == color)
            return false;

        info.color = color;

        return true;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderColor(1, 1, 1, 1);

        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 4 - 40, Color.WHITE.getRGB());
        guiGraphics.drawCenteredString(this.font, "Neutral", this.width / 2, _neutralRedSlider.getY() - 12, _config.getPlayerTypeInfo(PlayerType.Neutral).color.getRGB());
        guiGraphics.drawCenteredString(this.font, "Ally", this.width / 2, _allyRedSlider.getY() - 12, _config.getPlayerTypeInfo(PlayerType.Ally).color.getRGB());
        guiGraphics.drawCenteredString(this.font, "Enemy", this.width / 2, _enemyRedSlider.getY() - 12, _config.getPlayerTypeInfo(PlayerType.Enemy).color.getRGB());

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
