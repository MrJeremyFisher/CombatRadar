package com.aleksey.combatradar.fabric;

import com.aleksey.combatradar.ModHelper;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class FabricModCombatRadar implements ClientModInitializer {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static ModHelper _modHelper;

    @Override
    public void onInitializeClient() {
        _modHelper = new ModHelper();

        KeyBindingHelper.registerKeyBinding(_modHelper.getSettingsKey());
        HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> layeredDrawer.attachLayerAfter(IdentifiedLayer.EXPERIENCE_LEVEL,
                ResourceLocation.fromNamespaceAndPath("combatradar", "radar"),
                _modHelper::render));
        ClientLifecycleEvents.CLIENT_STARTED.register(e -> init());
    }

    private void init() {
        _modHelper.init(LOGGER);

        ClientTickEvents.START_CLIENT_TICK.register(e -> _modHelper.tick());
        ChatCallback.EVENT.register((component) -> _modHelper.processChat(component));

        LOGGER.info("[CombatRadar]: Enabled");
    }

    public static ModHelper getModHelper() {
        return _modHelper;
    }
}
