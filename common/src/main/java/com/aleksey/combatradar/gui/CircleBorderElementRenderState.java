package com.aleksey.combatradar.gui;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

public record CircleBorderElementRenderState(
        RenderPipeline pipeline,
        Matrix3x2f pose,
        ScreenRectangle scissorArea,
        float radius,
        int color
) implements GuiElementRenderState {
    @Override
    public void buildVertices(VertexConsumer vertices, float depth) {

        for (int i = 0; i <= 360; i++) {
            double theta = Math.toRadians(i);

            vertices.addVertexWith2DPose(this.pose, (float) (Math.cos(theta) * (radius + 0.5f)), (float) (Math.sin(theta) * (radius + 0.5f)), depth)
                    .setColor(color);
            vertices.addVertexWith2DPose(this.pose, (float) (Math.cos(theta) * (radius)), (float) (Math.sin(theta) * (radius)), depth)
                    .setColor(color);
        }

    }

    @Override
    public @NotNull RenderPipeline pipeline() {
        return this.pipeline;
    }

    @Override
    public @NotNull TextureSetup textureSetup() {
        return TextureSetup.noTexture();
    }

    @Override
    public @Nullable ScreenRectangle scissorArea() {
        return this.scissorArea;
    }

    @Override
    public ScreenRectangle bounds() {
        var screenRect = new ScreenRectangle(
                new ScreenPosition((int) (this.radius), (int) (this.radius)),
                (int) Math.ceil(this.radius * 2),
                (int) Math.ceil(this.radius * 2)
        ).transformMaxBounds(this.pose);

        return this.scissorArea != null ? this.scissorArea.intersection(screenRect) : screenRect;
    }
}
