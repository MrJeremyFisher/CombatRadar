package com.aleksey.combatradar.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.awt.Color;

/**
 * @author Aleksey Terzi
 */
public class CheckButton extends Button {
    public static final int BUTTON_HEIGHT = 14;
    private static final Identifier _texture = Identifier.fromNamespaceAndPath("combatradar", "textures/gui/checkbox.png");
    private static final int TEXTURE_SIZE = 7;
    private static final int CHECKED_TEXTURE_X = 8;
    private static final int UNCHECKED_TEXTURE_X = 0;
    private static final int INDENT = 9;

    private boolean _checked;

    public CheckButton(int x, int y, int width, String name, OnPress onPress) {
        super(x, y, width, BUTTON_HEIGHT, Component.literal(name), onPress, (btn) -> Component.literal(name));
    }

    public void setChecked(boolean value) {
        _checked = value;
    }

    @Override
    public void playDownSound(SoundManager p_93665_) {
    }

    @Override
    public void renderContents(GuiGraphics guiGraphics, int xPos, int yPos, float p_93846_) {
        Minecraft minecraft = Minecraft.getInstance();

        int textureX = _checked ? CHECKED_TEXTURE_X : UNCHECKED_TEXTURE_X;

//        RenderSystem.setShaderTexture(0, Minecraft.getInstance().getTextureManager().getTexture(_texture).getTextureView());

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, _texture, this.getX(), this.getY() + (this.getHeight() - TEXTURE_SIZE) / 2, textureX, 0, TEXTURE_SIZE, TEXTURE_SIZE, 256, 256);

        int textColor = this.isHovered ? 16777120 : Color.LIGHT_GRAY.getRGB();

        guiGraphics.drawString(minecraft.font, this.getMessage(), this.getX() + INDENT, (int) (this.getY() + (this.getHeight() - 8f) / 2f), textColor);
    }
}
