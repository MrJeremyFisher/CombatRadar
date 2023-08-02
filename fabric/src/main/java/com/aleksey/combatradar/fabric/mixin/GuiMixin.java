package com.aleksey.combatradar.fabric.mixin;

import com.aleksey.combatradar.fabric.ChatCallback;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Gui.class)
public class GuiMixin {
    private void onHandleChat(ChatType chatType, Component component, UUID uUID, CallbackInfo callbackInfo) {
        if (ChatCallback.EVENT.invoker().interact(component))
            callbackInfo.cancel();
    }
}
