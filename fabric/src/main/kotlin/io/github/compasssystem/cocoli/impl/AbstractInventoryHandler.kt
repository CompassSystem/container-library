package io.github.compasssystem.cocoli.impl

import io.github.compasssystem.cocoli.api.block.OpenableInventoryProvider
import io.github.compasssystem.cocoli.impl.inventory.InventoryOrigins
import io.github.compasssystem.cocoli.impl_client.ClientInit
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class AbstractInventoryHandler(
    inventoryId: Int,
    private val inventory: Container,
    private val playerInventory: Inventory
) : AbstractContainerMenu(CommonInit.inventoryScreenProviderType, inventoryId) {

    init {
        inventory.startOpen(playerInventory.player)
    }

    override fun quickMoveStack(player: Player, index: Int): ItemStack {
        val slot = slots[index]

        if (!slot.hasItem()) {
            return ItemStack.EMPTY
        }

        val newStack = slot.item
        val originalStack = newStack.copy()

        if (index < inventory.containerSize) { // Moving from container to player
            if (!this.moveItemStackTo(newStack, inventory.containerSize, inventory.containerSize + 36, true)) {
                return ItemStack.EMPTY
            }
        } else if (!this.moveItemStackTo(newStack, 0, inventory.containerSize, false)) { // Moving from player to container
            return ItemStack.EMPTY
        }

        if (newStack.isEmpty) {
            slot.set(ItemStack.EMPTY)
        } else {
            slot.setChanged()
        }

        return originalStack
    }

    override fun removed(player: Player) {
        super.removed(player)
        inventory.stopOpen(player)
    }

    override fun stillValid(player: Player) = inventory.stillValid(player)

    fun placeClientSlots() {
        placeServerSlots()
    }

    fun placeServerSlots() {
        for (i in 0 until inventory.containerSize) { // Container Slots
            this.addSlot(Slot(inventory, i, -Utils.SLOT_SIZE, 0))
        }

        for (y in 0 until 3) { // Player Inventory Slots
            for (x in 0 until 9) {
                this.addSlot(Slot(playerInventory, y * 9 + x + 9, Utils.SLOT_SIZE * x, y * Utils.SLOT_SIZE))
            }
        }

        for (x in 0 until 9) { // Player Hotbar Slots
            this.addSlot(Slot(playerInventory, x, x * Utils.SLOT_SIZE, 3 * Utils.SLOT_SIZE))
        }
    }

    companion object {
        fun createInventoryHandler(inventoryId: Int, playerInventory: Inventory, buffer: FriendlyByteBuf): AbstractInventoryHandler {
            val origins = buffer.readEnum(InventoryOrigins::class.java)
            val slots = buffer.readInt()

            return when (origins) {
                InventoryOrigins.BLOCK -> createBlockInventoryHandler(inventoryId, playerInventory, slots, buffer)
                else -> throw IllegalStateException("Unknown InventoryOrigins: $origins")
            }
        }

        private fun createBlockInventoryHandler(inventoryId: Int, playerInventory: Inventory, slots: Int, buffer: FriendlyByteBuf): AbstractInventoryHandler {
            val pos = buffer.readBlockPos()
            val level = playerInventory.player.level()

            val block = level.getBlockState(pos).block

            if (block is OpenableInventoryProvider<*>) {
                ClientInit.activeInventoryType = block.getInventoryType()
                ClientInit.activeForcedScreenType = block.getForcedScreenType()

                return AbstractInventoryHandler(inventoryId, SimpleContainer(slots), playerInventory)
            }

            throw IllegalStateException("Block at $pos is not an OpenableInventoryProvider")
        }
    }
}