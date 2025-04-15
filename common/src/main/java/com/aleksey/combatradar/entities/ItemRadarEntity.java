package com.aleksey.combatradar.entities;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

/**
 * @author Aleksey Terzi
 */
public class ItemRadarEntity extends RadarEntity {
    private final ItemStack _item;

    public ItemRadarEntity(Entity entity, EntitySettings settings) {
        super(entity, settings);

        _item = ((ItemEntity) getEntity()).getItem();
    }

    public ItemRadarEntity(Entity entity, EntitySettings settings, ItemStack item) {
        super(entity, settings);

        _item = item;
    }

    @Override
    protected void renderInternal(GuiGraphics guiGraphics, double displayX, double displayY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        PoseStack poseStack = guiGraphics.pose();
        float iconScale = getSettings().iconScale;
        float rotationYaw = minecraft.player.getViewYRot(partialTicks);

        poseStack.pushPose();
        poseStack.translate(displayX, displayY, 100.0F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotationYaw));
        poseStack.scale(iconScale, iconScale, iconScale);

//        RenderSystem.enableBlend();
//        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        guiGraphics.renderFakeItem(_item, -8, -8);

//        RenderSystem.enableDepthTest();

        poseStack.popPose();
    }
}
