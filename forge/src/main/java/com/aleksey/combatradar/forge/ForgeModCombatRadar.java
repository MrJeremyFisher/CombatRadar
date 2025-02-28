package com.aleksey.combatradar.forge;

import com.aleksey.combatradar.ModHelper;
import com.aleksey.combatradar.gui.screens.MainScreen;
import com.mojang.logging.LogUtils;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

/**
 * @author Aleksey Terzi
 */
@Mod("combatradar")
public class ForgeModCombatRadar {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static ModHelper _modHelper;

    public ForgeModCombatRadar(FMLJavaModLoadingContext fmlJavaModLoadingContext) {
        _modHelper = new ModHelper();

        _modHelper.init(LOGGER);

        LOGGER.info("[CombatRadar]: mod enabled");

        MinecraftForge.EVENT_BUS.register(this);

        fmlJavaModLoadingContext.getModEventBus().addListener(this::registerBindings);

        fmlJavaModLoadingContext.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (parent) -> new MainScreen(parent, _modHelper.getConfig(), _modHelper.getSpeedometer())
                )
        );
    }

    @SubscribeEvent
    public void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(_modHelper.getSettingsKey());
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            _modHelper.tick();
    }

    @SubscribeEvent
    public void onClientChat(ClientChatReceivedEvent event) {
        if (_modHelper.processChat(event.getMessage()))
            event.setCanceled(true);
    }

    public static ModHelper getModHelper() {
        return _modHelper;
    }
}