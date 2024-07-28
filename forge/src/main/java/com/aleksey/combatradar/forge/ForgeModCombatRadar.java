package com.aleksey.combatradar.neoforge;

import com.aleksey.combatradar.ModHelper;
import com.mojang.logging.LogUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
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

    private final ModHelper _modHelper;


    public ForgeModCombatRadar() {
        _modHelper = new ModHelper();

        _modHelper.init(LOGGER);

        LOGGER.info("[CombatRadar]: mod enabled");

        MinecraftForge.EVENT_BUS.register(this);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerBindings);
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
    public void onRender(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type())
            _modHelper.render(event.getGuiGraphics(), event.getPartialTick());
    }

    @SubscribeEvent
    public void onClientChat(ClientChatReceivedEvent event) {
        if (_modHelper.processChat(event.getMessage()))
            event.setCanceled(true);
    }
}