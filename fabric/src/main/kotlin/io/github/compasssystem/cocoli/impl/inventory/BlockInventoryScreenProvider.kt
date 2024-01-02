package io.github.compasssystem.cocoli.impl.inventory

import io.github.compasssystem.cocoli.impl.AbstractInventoryHandler
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.Container
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu

class BlockInventoryScreenProvider(
    private val pos: BlockPos,
    private val title: Component,
    private val inventory: Container
) : MenuProvider, ExtendedScreenHandlerFactory {
    override fun createMenu(inventoryId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
        val handler = AbstractInventoryHandler(inventoryId, inventory, playerInventory)
        handler.placeServerSlots()
        return handler
    }

    override fun getDisplayName(): Component {
        return title
    }

    override fun writeScreenOpeningData(player: ServerPlayer, buffer: FriendlyByteBuf) {
        buffer.writeEnum(InventoryOrigins.BLOCK)
        buffer.writeInt(inventory.containerSize)
        buffer.writeBlockPos(pos)
    }
}