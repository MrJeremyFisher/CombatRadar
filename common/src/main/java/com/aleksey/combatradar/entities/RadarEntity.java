package com.aleksey.combatradar.entities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

/**
 * @author Aleksey Terzi
 */
public abstract class RadarEntity {
    private final Entity _entity;
    private final EntitySettings _settings;

    public RadarEntity(Entity entity, EntitySettings settings) {
        _entity = entity;
        _settings = settings;
    }

    private static double getPartialX(Entity entity, float partialTicks) {
        return getPartial(entity.xOld, entity.getX(), partialTicks);
    }

    private static double getPartialZ(Entity entity, float partialTicks) {
        return getPartial(entity.zOld, entity.getZ(), partialTicks);
    }

    private static double getPartial(double oldValue, double newValue, float partialTicks) {
        return oldValue + (newValue - oldValue) * partialTicks;
    }

    protected Entity getEntity() {
        return _entity;
    }

    protected EntitySettings getSettings() {
        return _settings;
    }

    public final void render(GuiGraphics guiGraphics, float partialTicks) {
        Player player = Minecraft.getInstance().player;

        double displayX = getPartialX(player, partialTicks) - getPartialX(_entity, partialTicks);
        double displayZ = getPartialZ(player, partialTicks) - getPartialZ(_entity, partialTicks); // Convert to 2D where Z is Y
        double distanceSq = Mth.lengthSquared(displayX, displayZ);

        if (distanceSq > _settings.radarDistanceSq)
            return;

        renderInternal(guiGraphics, displayX, displayZ, partialTicks);
    }

    protected abstract void renderInternal(GuiGraphics guiGraphics, double displayX, double displayY, float partialTicks);
}
