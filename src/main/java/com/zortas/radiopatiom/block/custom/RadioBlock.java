package com.zortas.radiopatiom.block.custom;

import com.zortas.radiopatiom.item.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class RadioBlock extends BaseEntityBlock {

    public static final MapCodec<RadioBlock> CODEC = simpleCodec(RadioBlock::new);
    public static final BooleanProperty HAS_RECORD = BlockStateProperties.HAS_RECORD;

    @Override
    public MapCodec<RadioBlock> codec() {
        return CODEC;
    }

    public RadioBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HAS_RECORD, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HAS_RECORD);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new JukeboxBlockEntity(pos, state);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        CustomData data = stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY);
        if (data.contains("RecordItem")) {
            level.setBlock(pos, state.setValue(HAS_RECORD, true), 2);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (state.getValue(HAS_RECORD) && level.getBlockEntity(pos) instanceof JukeboxBlockEntity jukebox) {
            jukebox.popOutTheItem();
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(HAS_RECORD)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        ItemStack itemInHand = player.getItemInHand(hand);

        if (itemInHand.is(ModItems.CLASSIC_CASSETTE.get()) ||
                itemInHand.is(ModItems.SMILE_CASSETTE.get()) ||
                itemInHand.is(ModItems.SNOW_CASSETTE.get()) ||
                itemInHand.is(ModItems.REDPINK_CASSETTE.get())) {

            itemInHand.shrink(1);
            if (level.getBlockEntity(pos) instanceof JukeboxBlockEntity jukebox) {
                // Reproducir sonido personalizado aquí
            }
            level.setBlock(pos, state.setValue(HAS_RECORD, true), 3);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!oldState.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof JukeboxBlockEntity jukebox) {
                jukebox.popOutTheItem();
            }
            super.onRemove(oldState, level, pos, newState, isMoving);
        }
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if (level.getBlockEntity(pos) instanceof JukeboxBlockEntity jukebox && jukebox.getSongPlayer().isPlaying()) {
            return 15;
        }
        return 0;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof JukeboxBlockEntity jukebox) {
            return jukebox.getComparatorOutput();
        }
        return 0;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return state.getValue(HAS_RECORD) ? createTickerHelper(type, BlockEntityType.JUKEBOX, JukeboxBlockEntity::tick) : null;
    }
}
