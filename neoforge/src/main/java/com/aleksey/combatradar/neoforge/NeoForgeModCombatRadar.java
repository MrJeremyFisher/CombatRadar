package com.aleksey.combatradar.neoforge;

import com.aleksey.combatradar.ModHelper;
import com.aleksey.combatradar.gui.screens.MainScreen;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientChatReceivedEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

/**
 * @author Aleksey Terzi
 */
@Mod("combatradar")
public class NeoForgeModCombatRadar {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final ModHelper _modHelper;

    public NeoForgeModCombatRadar(IEventBus bus, ModContainer modContainer) {
        _modHelper = new ModHelper();

        NeoForge.EVENT_BUS.register(this);

        bus.addListener(this::registerBindings);

        bus.addListener(FMLClientSetupEvent.class, (clientSetupEvent) -> {
            _modHelper.init(LOGGER);
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, (container, parent) -> new MainScreen(parent, _modHelper.getConfig(), _modHelper.getSpeedometer()));

            LOGGER.info("[CombatRadar]: mod enabled");
        });
    }

    public void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(_modHelper.getSettingsKey());
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Pre event) {
        _modHelper.tick();
    }

    @SubscribeEvent
    public void onRender(RenderGuiLayerEvent.Post event) {
        if (event.getName() == VanillaGuiLayers.PLAYER_HEALTH)
            _modHelper.render(event.getGuiGraphics(), event.getPartialTick());
    }

    @SubscribeEvent
    public void onClientChat(ClientChatReceivedEvent event) {
        if (_modHelper.processChat(event.getMessage()))
            event.setCanceled(true);
    }
}