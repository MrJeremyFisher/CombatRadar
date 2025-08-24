package com.aleksey.combatradar.entities;

import com.aleksey.combatradar.config.PlayerType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix3x2fStack;

import java.awt.*;

/**
 * @author Aleksey Terzi
 */
public class PlayerRadarEntity extends RadarEntity {
    private final PlayerType _playerType;

    public PlayerRadarEntity(Entity entity, EntitySettings settings, PlayerType playerType) {
        super(entity, settings);
        _playerType = playerType;
    }

    @Override
    protected void renderInternal(GuiGraphics guiGraphics, float displayX, float displayY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Matrix3x2fStack poseStack = guiGraphics.pose();
        RemotePlayer player = (RemotePlayer) getEntity();
        float rotationYaw = minecraft.player.getViewYRot(partialTicks);
        float scale = getSettings().iconScale * 1.7f;

        poseStack.pushMatrix();
        poseStack.translate(displayX, displayY);
        poseStack.rotate(org.joml.Math.toRadians(rotationYaw));

        poseStack.pushMatrix();
        poseStack.scale(scale, scale);
        renderPlayerIcon(guiGraphics, player);
        poseStack.popMatrix();

        if (getSettings().showPlayerNames)
            renderPlayerName(guiGraphics, player);

        poseStack.popMatrix();
    }

    private void renderPlayerIcon(GuiGraphics guiGraphics, RemotePlayer player) {
        ResourceLocation skin = player.getSkin().texture();

        RenderSystem.setShaderTexture(0, Minecraft.getInstance().getTextureManager().getTexture(skin).getTextureView());

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, skin, -4, -4, 8, 8, 8, 8, 8, 8, 64, 64,
                ARGB.colorFromFloat(getSettings().iconOpacity, 1.0F, 1.0F, 1.0F));
    }

    private void renderPlayerName(GuiGraphics guiGraphics, RemotePlayer player) {
        Minecraft minecraft = Minecraft.getInstance();
        Matrix3x2fStack poseStack = guiGraphics.pose();

        Color color = _playerType == PlayerType.Ally
                ? getSettings().allyPlayerColor
                : (_playerType == PlayerType.Enemy ? getSettings().enemyPlayerColor : getSettings().neutralPlayerColor);

        poseStack.scale(getSettings().fontScale, getSettings().fontScale, poseStack.pushMatrix());

        String playerName = player.getScoreboardName();
        if (getSettings().showExtraPlayerInfo && getSettings().showYLevel) {
            playerName += " (" + (int) minecraft.player.distanceTo(player) + "m)(Y" + player.getBlockY() + ")";
        } else if (getSettings().showExtraPlayerInfo && !getSettings().showYLevel) {

            double dx = player.getX() - minecraft.player.getX();
            double dz = player.getZ() - minecraft.player.getZ();

            playerName += " (" + (int) Math.round(Math.hypot(dx, dz)) + "m)";
        }


        Font font = minecraft.font;
        int yOffset = -4 + (int) ((getSettings().iconScale * getSettings().radarScale + 8));
        int xOffset = -font.width(playerName) / 2;

        guiGraphics.drawString(font, playerName, xOffset, yOffset, color.getRGB());

        poseStack.popMatrix();
    }
}
