package com.aleksey.combatradar;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.state.GuiElementRenderState;

public interface GuiState {
    void cr$submit(GuiElementRenderState renderState);

    static void submit(GuiGraphics graphics, GuiElementRenderState renderState) {
        ((GuiState) graphics).cr$submit(renderState);
    }

    ScreenRectangle cr$peekScissorStack();

    static ScreenRectangle peekScissorStack(GuiGraphics graphics) {
        return ((GuiState) graphics).cr$peekScissorStack();
    }
}
