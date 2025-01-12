package com.aleksey.combatradar.fabric;

import static com.aleksey.combatradar.fabric.FabricModCombatRadar.getModHelper;
import com.aleksey.combatradar.gui.screens.MainScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuEntrypoint implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (parent) -> new MainScreen(parent, getModHelper().getConfig(), getModHelper().getSpeedometer());
    }
}
