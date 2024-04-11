package com.aleksey.combatradar.gui.components;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * @author Aleksey Terzi
 */
public class SmallButton extends Button {
    public SmallButton(int x, int y, int width, int height, Component text, Button.OnPress onPress) {
        super(x, y, width, height, text, onPress, (btn) -> MutableComponent.create(text.getContents()));
    }
}
