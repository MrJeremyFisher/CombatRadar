package com.aleksey.combatradar.entities;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

/**
 * @author Aleksey Terzi
 */
public class LiveRadarEntity extends RadarEntity {
    private static final String[] HORSE_VARIANTS = {"white", "creamy", "chestnut", "brown", "black", "gray", "darkbrown"};

    private ResourceLocation _resourceLocation;

    public LiveRadarEntity(Entity entity, EntitySettings settings, ResourceLocation icon) {
        super(entity, settings);

        _resourceLocation = icon;
    }

    @Override
    protected void renderInternal(GuiGraphics guiGraphics, double displayX, double displayY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        PoseStack poseStack = guiGraphics.pose();
        float iconScale = getSettings().iconScale;
        float rotationYaw = minecraft.player.getViewYRot(partialTicks);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, getSettings().iconOpacity);

        poseStack.pushPose();
        poseStack.translate(displayX, displayY, 0);
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotationYaw));
        poseStack.scale(iconScale, iconScale, iconScale);

        RenderSystem.setShaderTexture(0, Minecraft.getInstance().getTextureManager().getTexture(_resourceLocation).getTexture());

        guiGraphics.blit(RenderType::guiTextured, _resourceLocation, -8, -8, 0, 0, 16, 16, 16, 16);

        poseStack.popPose();

    }
}
