package com.aleksey.combatradar.forge;

import com.aleksey.combatradar.ModHelper;
import com.aleksey.combatradar.gui.screens.MainScreen;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.Identifier;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.AddGuiOverlayLayersEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.gui.overlay.ForgeLayeredDraw;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
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

        AddGuiOverlayLayersEvent.BUS.addListener(this::initOverlays);

        MinecraftForge.EVENT_BUS.register(this);

        RegisterKeyMappingsEvent.BUS.addListener(this::registerBindings);

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
    public void onClientTick(TickEvent.ClientTickEvent.Pre event) {
        _modHelper.tick();
    }

    @SubscribeEvent
    public boolean onClientChat(ClientChatReceivedEvent event) {
        return _modHelper.processChat(event.getMessage()); // Forge handles setCancelled as a return now. True cancels the event, false does not.
    }

    public static ModHelper getModHelper() {
        return _modHelper;
    }

    public void initOverlays(AddGuiOverlayLayersEvent event) {
        ForgeLayeredDraw lDraw = new ForgeLayeredDraw(Identifier.fromNamespaceAndPath("combatradar", "forgelayer"));
        lDraw.add(lDraw.getName(),
                (arg, arg2) -> ForgeModCombatRadar.getModHelper().render(arg, arg2)
        );

        event.getLayeredDraw().add(lDraw.getName(), lDraw, () -> true);
        event.getLayeredDraw().move(lDraw.getName(), ForgeLayeredDraw.SUBTITLE_OVERLAY, ForgeLayeredDraw.LayerOffset.ABOVE);
    }
}