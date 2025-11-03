package com.zortas.radiopatiom.block.entity;

import com.zortas.radiopatiom.RadioPatioM;
import com.zortas.radiopatiom.block.ModBlock;
import com.zortas.radiopatiom.block.entity.custom.RadioBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RadioPatioM.MOD_ID);

    public static final RegistryObject<BlockEntityType<RadioBlockEntity>> RADIO_BE =
            BLOCK_ENTITIES.register("radio_be", () -> BlockEntityType.Builder.of(
                    RadioBlockEntity::new, ModBlock.RADIO_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
