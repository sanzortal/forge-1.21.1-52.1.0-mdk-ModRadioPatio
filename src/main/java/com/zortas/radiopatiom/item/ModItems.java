package com.zortas.radiopatiom.item;

import com.zortas.radiopatiom.RadioPatioM;
import com.zortas.radiopatiom.sound.ModSounds;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, RadioPatioM.MOD_ID);

    // 1. REGISTRAR LOS ITEMS TIPO CASETE
    public static final RegistryObject<Item> CLASSIC_CASSETTE = ITEMS.register("classic_cassette",
            () -> new Item(new Item.Properties().jukeboxPlayable(ModSounds.CLASSIC_CASSETTE_SOUND_KEY).stacksTo(1)));
    public static final RegistryObject<Item> SMILE_CASSETTE = ITEMS.register("smile_cassette",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SNOW_CASSETTE = ITEMS.register("snow_cassette",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> REDPINK_CASSETTE = ITEMS.register("redpink_cassette",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MUSICAL_NOTE = ITEMS.register("musical_note",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
