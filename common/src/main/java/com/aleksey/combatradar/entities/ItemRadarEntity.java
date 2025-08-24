package com.aleksey.combatradar.entities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;

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
    protected void renderInternal(GuiGraphics guiGraphics, float displayX, float displayY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Matrix3x2fStack poseStack = guiGraphics.pose();
        float iconScale = getSettings().iconScale;
        float rotationYaw = minecraft.player.getViewYRot(partialTicks);

        poseStack.pushMatrix();
        poseStack.translate(displayX, displayY);
        poseStack.rotate(org.joml.Math.toRadians(rotationYaw));
        poseStack.scale(iconScale, iconScale);

        guiGraphics.renderFakeItem(_item, -8, -8);

        poseStack.popMatrix();
    }
}
