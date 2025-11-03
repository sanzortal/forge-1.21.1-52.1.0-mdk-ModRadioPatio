package com.zortas.radiopatiom;

import com.mojang.logging.LogUtils;
import com.zortas.radiopatiom.block.ModBlock;
import com.zortas.radiopatiom.block.entity.ModBlockEntities;
import com.zortas.radiopatiom.item.ModItems;
import com.zortas.radiopatiom.screen.ModMenuTypes;
import com.zortas.radiopatiom.screen.custom.RadioScreen;
import com.zortas.radiopatiom.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(RadioPatioM.MOD_ID)
public class RadioPatioM {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "radiopatiom";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public RadioPatioM(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // 2. REGISTRAR A NUESTRA CARPETA / LISTA DE ITEMS
        ModItems.register(modEventBus);



        // REGISTRAR NUESTROS SONIDOS
        ModSounds.register(modEventBus);

        //Registrar entityBlock
        ModBlockEntities.register(modEventBus);
        // REGISTRAR NUESTRA CARPETA / LISTA DE BLOQUES
        ModBlock.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);


        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        // 3. MOSTRARLO EN LA TABLA CREATIVA
        // lo que hace es aceptar nuestro nuevo item en la tabla creativa
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS){
            event.accept(ModItems.CLASSIC_CASSETTE);
            event.accept(ModItems.SMILE_CASSETTE);
            event.accept(ModItems.SNOW_CASSETTE);
            event.accept(ModItems.REDPINK_CASSETTE);
            event.accept(ModItems.MUSICAL_NOTE);
        }

        if(event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS){
            event.accept(ModBlock.RADIO_BLOCK);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
            MenuScreens.register(ModMenuTypes.RADIO_MENU.get(), RadioScreen::new);
        }
    }
}
