package compass_system.cocoli.test.block

import compass_system.cocoli.test.TestMain
import io.github.compasssystem.cocoli.api.inventory.OpenableInventory
import net.minecraft.core.BlockPos
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.Container
import net.minecraft.world.ContainerHelper
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class SimpleBarrelBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(TestMain.barrelBlockEntityType, pos, state), OpenableInventory {
    private val slots = (state.block as SimpleBarrelBlock).slots
    private val items: NonNullList<ItemStack> = NonNullList.withSize(slots, ItemStack.EMPTY)
    private val inventory = object : Container {
        override fun clearContent() {
            items.clear()
        }

        override fun getContainerSize(): Int {
            return slots
        }

        override fun isEmpty(): Boolean {
            return items.all(ItemStack::isEmpty)
        }

        override fun getItem(slot: Int): ItemStack {
            return items[slot]
        }

        override fun removeItem(slot: Int, amount: Int): ItemStack {
            val stack = ContainerHelper.removeItem(items, slot, amount)

            if (!stack.isEmpty) {
                this.setChanged()
            }

            return stack
        }

        override fun removeItemNoUpdate(slot: Int): ItemStack {
            return ContainerHelper.takeItem(items, slot)
        }

        override fun setItem(slot: Int, stack: ItemStack) {
            items[slot] = stack
            if (stack.count > this.maxStackSize) {
                stack.count = this.maxStackSize
            }
            this.setChanged()
        }

        override fun setChanged() {
            this@SimpleBarrelBlockEntity.setChanged()
        }

        override fun stillValid(player: Player): Boolean {
            return true
        }

    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        ContainerHelper.loadAllItems(tag, items)
    }

    override fun saveAdditional(tag: CompoundTag) {
        super.saveAdditional(tag)
        ContainerHelper.saveAllItems(tag, items)
    }

    override fun canPlayerOpen(player: ServerPlayer): Boolean {
        return inventory.stillValid(player)
    }

    override fun inventory(): Container {
        return inventory
    }

    override fun title(): Component {
        return Component.translatable(blockState.block.descriptionId)
    }
}