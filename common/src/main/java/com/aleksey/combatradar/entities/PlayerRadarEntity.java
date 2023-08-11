package com.aleksey.combatradar.entities;

import com.aleksey.combatradar.config.PlayerType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

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
    protected void renderInternal(GuiGraphics guiGraphics, double displayX, double displayY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        PoseStack poseStack = guiGraphics.pose();
        RemotePlayer player = (RemotePlayer) getEntity();
        float rotationYaw = minecraft.player.getViewYRot(partialTicks);
        float scale = getSettings().iconScale * 1.7f;

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, getSettings().iconOpacity);
        RenderSystem.enableBlend();

        poseStack.pushPose();
        poseStack.translate(displayX, displayY, 0);
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotationYaw));

        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        renderPlayerIcon(guiGraphics, player);
        poseStack.popPose();

        RenderSystem.disableBlend();

        if (getSettings().showPlayerNames)
            renderPlayerName(guiGraphics, player);

        poseStack.popPose();
    }

    private void renderPlayerIcon(GuiGraphics guiGraphics, RemotePlayer player) {
        ResourceLocation skin = player.getSkinTextureLocation();

        RenderSystem.setShaderTexture(0, skin);

        guiGraphics.blit(skin, -4, -4, 8, 8, 8, 8, 8, 8, 64, 64);
    }

    private void renderPlayerName(GuiGraphics guiGraphics, RemotePlayer player) {
        Minecraft minecraft = Minecraft.getInstance();
        PoseStack poseStack = guiGraphics.pose();

        Color color = _playerType == PlayerType.Ally
                ? getSettings().allyPlayerColor
                : (_playerType == PlayerType.Enemy ? getSettings().enemyPlayerColor : getSettings().neutralPlayerColor);

        poseStack.pushPose();
        poseStack.scale(getSettings().fontScale, getSettings().fontScale, getSettings().fontScale);

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

        poseStack.popPose();
    }
}
