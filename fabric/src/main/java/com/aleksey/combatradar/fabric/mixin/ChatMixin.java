package com.aleksey.combatradar.fabric.mixin;

import com.aleksey.combatradar.fabric.ChatCallback;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ChatComponent.class)
public class ChatMixin {
    @Inject(method = "addMessage(Lnet/minecraft/network/chat/Component;)V", at = @At(value = "HEAD"), cancellable = true)
    private void onAddChat(Component component, CallbackInfo ci) {
        if (ChatCallback.EVENT.invoker().interact(component)) {
            ci.cancel();
        }
    }
}



