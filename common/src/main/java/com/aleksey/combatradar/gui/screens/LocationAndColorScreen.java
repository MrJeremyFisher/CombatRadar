package com.aleksey.combatradar.gui.screens;

import com.aleksey.combatradar.config.RadarConfig;
import com.aleksey.combatradar.gui.components.SliderButton;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

/**
 * @author Aleksey Terzi
 */
public class LocationAndColorScreen extends Screen {
    private final RadarConfig _config;
    private final Screen _parent;
    private SliderButton _redSlider;
    private SliderButton _greenSlider;
    private SliderButton _blueSlider;
    private SliderButton _opacitySlider;
    private SliderButton _sizeSlider;
    private SliderButton _rangeSlider;
    private SliderButton _iconScaleSlider;
    private SliderButton _fontScaleSlider;

    public LocationAndColorScreen(Screen parent, RadarConfig config) {
        super(CommonComponents.EMPTY);
        _parent = parent;
        _config = config;
    }

    @Override
    public void init() {
        int y = this.height / 4 - 16;
        int x = this.width / 2 - 100;

        addRenderableWidget(_redSlider = new SliderButton(x, y, 66, 1, 0, "Red", _config.getRadarColor().getRed() / 255f, false));
        addRenderableWidget(_greenSlider = new SliderButton(x + 66 + 1, y, 66, 1, 0, "Green", _config.getRadarColor().getGreen() / 255f, false));
        addRenderableWidget(_blueSlider = new SliderButton(x + 66 + 1 + 66 + 1, y, 66, 1, 0, "Blue", _config.getRadarColor().getBlue() / 255f, false));
        y += 24;
        addRenderableWidget(_opacitySlider = new SliderButton(x, y, 200, 1, 0, "Radar Opacity", _config.getRadarOpacity(), false));
        y += 24;
        addRenderableWidget(_sizeSlider = new SliderButton(x, y, 100, 1, 0.1f, "Radar Size", _config.getRadarSize(), false));
        addRenderableWidget(_rangeSlider = new SliderButton(x + 101, y, 100, 8f, 3f, "Radar Range", _config.getRadarDistance() / 16f, true));
        y += 24;
        addRenderableWidget(_iconScaleSlider = new SliderButton(x, y, 100, 1f, 0.1f, "Icon Size", _config.getIconScale() / 3f, false));
        addRenderableWidget(_fontScaleSlider = new SliderButton(x + 101, y, 100, 1f, 0.2f, "Font Size", _config.getFontScale() / 3f, false));
        y += 24 + 24;
        addRenderableWidget(Button.builder(net.minecraft.network.chat.Component.literal("Snap top left"), (btn) -> setRadar(0, 0)).bounds(x, y, 100, 20).build());
        addRenderableWidget(Button.builder(net.minecraft.network.chat.Component.literal("Snap top right"), (btn) -> setRadar(1, 0)).bounds(x + 101, y, 100, 20).build());
        y += 24;
        addRenderableWidget(Button.builder(net.minecraft.network.chat.Component.literal("Snap bottom left"), (btn) -> setRadar(0, 1)).bounds(x, y, 100, 20).build());
        addRenderableWidget(Button.builder(net.minecraft.network.chat.Component.literal("Snap bottom right"), (btn) -> setRadar(1, 1)).bounds(x + 101, y, 100, 20).build());
        y += 24;
        addRenderableWidget(Button.builder(net.minecraft.network.chat.Component.literal("Done"), (btn) -> this.minecraft.setScreen(_parent)).bounds(x, y, 200, 20).build());
    }

    private void setRadar(float x, float y) {
        _config.setRadarX(x);
        _config.setRadarY(y);
        _config.save();
    }

    @Override
    public void tick() {
        var isChanged = false;

        var window = this.minecraft.getWindow();
        var windowId = window.getWindow();
        var xSpeed = 1.f / window.getGuiScaledWidth();
        var ySpeed = 1.f / window.getGuiScaledHeight();

        if (InputConstants.isKeyDown(windowId, GLFW.GLFW_KEY_LEFT)) {
            _config.setRadarX(_config.getRadarX() - xSpeed);
            isChanged = true;
        }
        if (InputConstants.isKeyDown(windowId, GLFW.GLFW_KEY_RIGHT)) {
            _config.setRadarX(_config.getRadarX() + xSpeed);
            isChanged = true;
        }
        if (InputConstants.isKeyDown(windowId, GLFW.GLFW_KEY_UP)) {
            _config.setRadarY(_config.getRadarY() - ySpeed);
            isChanged = true;
        }
        if (InputConstants.isKeyDown(windowId, GLFW.GLFW_KEY_DOWN)) {
            _config.setRadarY(_config.getRadarY() + ySpeed);
            isChanged = true;
        }

        Color radarColor = new Color(_redSlider.getValue(), _greenSlider.getValue(), _blueSlider.getValue());

        isChanged = _config.setRadarColor(radarColor) || isChanged;
        isChanged = _config.setRadarOpacity(_opacitySlider.getValue()) || isChanged;
        isChanged = _config.setRadarSize(_sizeSlider.getValue()) || isChanged;
        isChanged = _config.setRadarDistance((int) (_rangeSlider.getValue() * 16)) || isChanged;
        isChanged = _config.setIconScale(_iconScaleSlider.getValue() * 3f) || isChanged;
        isChanged = _config.setFontScale(_fontScaleSlider.getValue() * 3f) || isChanged;

        if (isChanged)
            _config.save();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        guiGraphics.drawCenteredString(this.font, "Location and Color", this.width / 2, this.height / 4 - 40, Color.WHITE.getRGB());
        guiGraphics.drawCenteredString(this.font, "Use arrow keys to reposition radar", this.width / 2, _iconScaleSlider.getY() + 24 + 12, Color.WHITE.getRGB());

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