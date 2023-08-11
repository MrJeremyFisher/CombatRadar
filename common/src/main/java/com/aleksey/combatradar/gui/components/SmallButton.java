package com.aleksey.combatradar.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.client.gui.components.Renderable;
/**
 * @author Aleksey Terzi
 */
public class SmallButton extends Button {
    public SmallButton(int x, int y, int width, int height, Component text, Button.OnPress onPress) {
        super(x, y, width, height, text, onPress, (btn) -> Component.literal(String.valueOf(text)));
    }
}
