package com.aleksey.combatradar.forge.mixin;

import com.aleksey.combatradar.forge.ForgeModCombatRadar;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayeredDraw.class)
public class PostRenderGuiOverlayMixin {
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/LayeredDraw;renderInner(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void onRenderHealth(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) { // Hacky way to add a Layer because Forge sucks
        ForgeModCombatRadar.getModHelper().render(guiGraphics, deltaTracker);
    }
}