package com.aleksey.combatradar.gui.screens;

import com.aleksey.combatradar.config.PlayerType;
import com.aleksey.combatradar.config.RadarConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

/**
 * @author Aleksey Terzi
 */
public class AddPlayerScreen extends Screen {
    private RadarConfig _config;
    private Screen _parent;
    private PlayerType _playerType;
    private EditBox _playerNameEditBox;

    public AddPlayerScreen(Screen parent, RadarConfig config, PlayerType playerType) {
        super(CommonComponents.EMPTY);

        _parent = parent;
        _config = config;
        _playerType = playerType;
    }

    @Override
    protected void init() {
        int y = this.height / 3;
        int x = this.width / 2 - 100;

        _playerNameEditBox = new EditBox(this.font, x, y, 200, 20, CommonComponents.EMPTY);
        addRenderableWidget(_playerNameEditBox);

        y += 24;



        addRenderableWidget(Button.builder(Component.literal("Add"), (button) -> actionAdd()).bounds(x, y, 200, 20).build());

        y += 24;
        addRenderableWidget(Button.builder(Component.literal("Cancel"), (button) -> actionCancel()).bounds(x, y, 200, 20).build());

        setInitialFocus(_playerNameEditBox);
    }

    @Override
    public boolean keyPressed(int keyCode, int p_96553_, int p_96554_) {
        switch (keyCode) {
            case GLFW.GLFW_KEY_ENTER -> {
                actionAdd();
                return true;
            }
            case GLFW.GLFW_KEY_ESCAPE -> {
                actionCancel();
                return true;
            }
        }

        return super.keyPressed(keyCode, p_96553_, p_96554_);
    }

    private void actionAdd() {
        String playerName = _playerNameEditBox.getValue().trim();

        if(playerName.length() == 0)
            return;

        _config.setPlayerType(playerName, _playerType);
        _config.save();

        this.minecraft.setScreen(_parent);
    }

    private void actionCancel() {
        this.minecraft.setScreen(_parent);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        String playerTypeName = _playerType == PlayerType.Ally ? "Ally" : "Enemy";

        renderDirtBackground(poseStack);

        drawCenteredString(poseStack, this.font, "Add " + playerTypeName + " Player", this.width / 2, this.height / 4 - 40, Color.WHITE.getRGB());
        this.font.drawShadow(poseStack, "Player username", this.width / 2f - 100, _playerNameEditBox.getY() - 12, Color.LIGHT_GRAY.getRGB());

        super.render(poseStack, mouseX, mouseY, partialTicks);
    }
}