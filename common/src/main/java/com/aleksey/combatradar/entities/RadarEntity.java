package com.aleksey.combatradar.entities;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;

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

    public Entity getEntity() {
        return _entity;
    }

    public EntitySettings getSettings() {
        return _settings;
    }

    public final void render(GuiGraphics guiGraphics, float partialTicks, float displayX, float displayZ, double distanceSq) {
        if (distanceSq > _settings.radarDistanceSq)
            return;

        guiGraphics.nextStratum();
        renderInternal(guiGraphics, displayX, displayZ, partialTicks);
    }

    protected abstract void renderInternal(GuiGraphics guiGraphics, float displayX, float displayY, float partialTicks);
}
