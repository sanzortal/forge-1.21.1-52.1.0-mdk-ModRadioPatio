package com.zortas.radiopatiom.block.custom;


import com.mojang.serialization.MapCodec;
import com.zortas.radiopatiom.block.entity.custom.RadioBlockEntity;
import com.zortas.radiopatiom.item.ModItems;
import com.zortas.radiopatiom.sound.ModSounds;
import com.zortas.radiopatiom.sound.RadioMusicHandler;
import net.minecraft.core.BlockPos;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.stream.Stream;

public class RadioBlock extends BaseEntityBlock {
    private static final Set<RegistryObject<Item>> ALLOWED_ITEMS = Set.of(
            ModItems.CLASSIC_CASSETTE,
            ModItems.REDPINK_CASSETTE,
            ModItems.SMILE_CASSETTE,
            ModItems.SNOW_CASSETTE
    );
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public RadioBlock(Properties properties) {
        super(properties);

    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    private static final VoxelShape SHAPE = Stream.of(
            // Cuerpo principal
            Block.box(0, 0, 4, 16, 8, 12),
            // Manija derecha
            Block.box(12, 8, 7, 13, 10, 8),
            // Manija izquierda
            Block.box(3, 8, 7, 4, 10, 8),
            // Asa superior
            Block.box(3, 10, 7, 13, 11, 8)
    ).reduce((v1,v2) -> Shapes.join(v1,v2, BooleanOp.OR)).get();

    public VoxelShape getShape(BlockState pState, BlockGetter level, BlockPos pPos, CollisionContext pContext){
        return  SHAPE;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RadioBlockEntity(pPos, pState);
    }

    /* FACING */

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext){
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation){
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror){
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder){
        pBuilder.add(FACING);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if(pLevel.getBlockEntity(pPos) instanceof RadioBlockEntity radioBlockEntity) {

            Item item = pStack.getItem();
            boolean isAllowed = ALLOWED_ITEMS.stream().anyMatch(reg -> reg.get() == item);
                if (pStack.isEmpty() && !radioBlockEntity.inventory.getStackInSlot(0).isEmpty()) {
                    RadioMusicHandler.stop();
                    ItemStack stackOnRadio = radioBlockEntity.inventory.extractItem(0, 1, false);
                    pPlayer.setItemInHand(InteractionHand.MAIN_HAND, stackOnRadio);
                    radioBlockEntity.clearContents();
                    pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                if (isAllowed) {
                    if(radioBlockEntity.inventory.getStackInSlot(0).isEmpty() && !pStack.isEmpty()) {
                        radioBlockEntity.inventory.insertItem(0, pStack.copy(), false);
                        pStack.shrink(1);
                        RadioMusicHandler.play(ModSounds.CLASSIC_CASSETTE_SOUND.get(), pPos);
                    } else {
                        pLevel.playSound(pPlayer, pPos, SoundEvents.VILLAGER_NO, SoundSource.BLOCKS, 1.0F, 1.0F);
                        return ItemInteractionResult.FAIL;
                    }
                }
        }
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    protected RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
}
