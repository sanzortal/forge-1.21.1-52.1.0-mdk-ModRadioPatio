package com.zortas.radiopatiom.screen.custom;

import com.zortas.radiopatiom.block.ModBlock;
import com.zortas.radiopatiom.block.entity.custom.RadioBlockEntity;
import com.zortas.radiopatiom.screen.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class RadioMenu extends AbstractContainerMenu {

    public final RadioBlockEntity blockEntity;
    public final Level level;

    public RadioMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public RadioMenu(int pContainerId, Inventory inv, BlockEntity blockEntity) {
        super(ModMenuTypes.RADIO_MENU.get(), pContainerId);
        this.blockEntity = (RadioBlockEntity) blockEntity;
        this.level = inv.player.level();
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, ModBlock.RADIO_BLOCK.get());
    }
}
