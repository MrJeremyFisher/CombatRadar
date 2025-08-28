package com.aleksey.combatradar.gui;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

public record LineElementRenderState(
        RenderPipeline pipeline,
        Matrix3x2f pose,
        ScreenRectangle scissorArea,
        float radius,
        int color
) implements GuiElementRenderState {
    @Override
    public void buildVertices(VertexConsumer vertices, float depth) {
        final float cos45 = 0.7071f;
        final float a = 0.25f;
        float length = radius - a;
        float d = cos45 * length;
        float c = d + a / cos45;

        vertices.addVertexWith2DPose(this.pose, -a, -length, depth).setColor(color);
        vertices.addVertexWith2DPose(this.pose, -a, length, depth).setColor(color);
        vertices.addVertexWith2DPose(this.pose, a, length, depth).setColor(color);
        vertices.addVertexWith2DPose(this.pose, a, -length, depth).setColor(color);

        vertices.addVertexWith2DPose(this.pose, -length, a, depth).setColor(color);
        vertices.addVertexWith2DPose(this.pose, length, a, depth).setColor(color);
        vertices.addVertexWith2DPose(this.pose, length, -a, depth).setColor(color);
        vertices.addVertexWith2DPose(this.pose, -length, -a, depth).setColor(color);

        vertices.addVertexWith2DPose(this.pose, -c, -d, depth).setColor(color);
        vertices.addVertexWith2DPose(this.pose, d, c, depth).setColor(color);
        vertices.addVertexWith2DPose(this.pose, c, d, depth).setColor(color);
        vertices.addVertexWith2DPose(this.pose, -d, -c, depth).setColor(color);

        vertices.addVertexWith2DPose(this.pose, -d, c, depth).setColor(color);
        vertices.addVertexWith2DPose(this.pose, c, -d, depth).setColor(color);
        vertices.addVertexWith2DPose(this.pose, d, -c, depth).setColor(color);
        vertices.addVertexWith2DPose(this.pose, -c, d, depth).setColor(color);
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
                (int) Math.ceil(this.radius * 2),
                (int) Math.ceil(this.radius * 2)
        ).transformMaxBounds(this.pose);

        return this.scissorArea != null ? this.scissorArea.intersection(screenRect) : screenRect;
    }
}