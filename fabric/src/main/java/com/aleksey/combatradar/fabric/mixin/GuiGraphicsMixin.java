package com.aleksey.combatradar.fabric.mixin;

import com.aleksey.combatradar.GuiState;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import net.minecraft.client.gui.render.state.GuiRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin implements GuiState {

    @Shadow
    @Final
    private GuiRenderState guiRenderState;

    @Override
    public void cr$submit(GuiElementRenderState renderState) {
        this.guiRenderState.submitGuiElement(renderState);
    }

    @Shadow @Final private GuiGraphics.ScissorStack scissorStack;

    @Override
    public ScreenRectangle cr$peekScissorStack() {
        return this.scissorStack.peek();
    }
}