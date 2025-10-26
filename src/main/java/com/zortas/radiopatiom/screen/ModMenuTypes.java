package com.zortas.radiopatiom.screen;

import com.zortas.radiopatiom.RadioPatioM;
import com.zortas.radiopatiom.screen.custom.RadioMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, RadioPatioM.MOD_ID);

    public static final RegistryObject<MenuType<RadioMenu>> RADIO_MENU =
            MENUS.register("radio_name", () -> IForgeMenuType.create(RadioMenu::new));
    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
