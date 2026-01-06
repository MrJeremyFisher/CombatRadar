package com.aleksey.combatradar.entities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix3x2fStack;

/**
 * @author Aleksey Terzi
 */
public class LiveRadarEntity extends RadarEntity {
    private static final String[] HORSE_VARIANTS = {"white", "creamy", "chestnut", "brown", "black", "gray", "darkbrown"};

    private Identifier _resourceLocation;

    public LiveRadarEntity(Entity entity, EntitySettings settings, Identifier icon) {
        super(entity, settings);

        _resourceLocation = icon;
    }

    @Override
    protected void renderInternal(GuiGraphics guiGraphics, float displayX, float displayY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Matrix3x2fStack poseStack = guiGraphics.pose();
        float iconScale = getSettings().iconScale;
        float rotationYaw = minecraft.player.getViewYRot(partialTicks);

        poseStack.pushMatrix();
        poseStack.translate(displayX, displayY);
        poseStack.rotate(org.joml.Math.toRadians(rotationYaw));
        poseStack.scale(iconScale, iconScale);

//        RenderSystem.setShaderTexture(0, Minecraft.getInstance().getTextureManager().getTexture(_resourceLocation).getTextureView());
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, _resourceLocation, -8, -8, 0, 0, 16, 16, 16, 16,
                ARGB.colorFromFloat(getSettings().iconOpacity, 1.0F, 1.0F, 1.0F));

        poseStack.popMatrix();
    }
}
