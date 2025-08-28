package com.aleksey.combatradar.gui;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

public record TriangleElementRenderState(
        RenderPipeline pipeline,
        Matrix3x2f pose,
        ScreenRectangle scissorArea,
        float offset,
        int color
) implements GuiElementRenderState {
    @Override
    public void buildVertices(VertexConsumer vertices, float depth) {
        vertices.addVertexWith2DPose(this.pose, 0f, 3f - this.offset, depth).setColor(color);
        vertices.addVertexWith2DPose(this.pose, 3f - this.offset, -3f + this.offset, depth).setColor(color);
        vertices.addVertexWith2DPose(this.pose, -3f + this.offset, -3f + this.offset, depth).setColor(color);
    }

    @Override
    public RenderPipeline pipeline() {
        return this.pipeline;
    }

    @Override
    public TextureSetup textureSetup() {
        return TextureSetup.noTexture();
    }

    @Override
    public @Nullable ScreenRectangle scissorArea() {
        return this.scissorArea;
    }

    @Override
    public ScreenRectangle bounds() {
        var screenRect = new ScreenRectangle(
                new ScreenPosition(0, 0),
                (int) Math.ceil(6 + this.offset),
                (int) Math.ceil(6 + this.offset)
        ).transformMaxBounds(this.pose);

        return this.scissorArea != null ? this.scissorArea.intersection(screenRect) : screenRect;
    }
}
