package compass_system.cocoli.test.block

import compass_system.cocoli.test.TestMain
import net.minecraft.core.BlockPos
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.ContainerHelper
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class SimpleBarrelBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(TestMain.barrelBlockEntityType, pos, state) {
    private val slots = (state.block as SimpleBarrelBlock).slots
    private val items: NonNullList<ItemStack> = NonNullList.withSize(slots, ItemStack.EMPTY)

    override fun load(tag: CompoundTag) {
        super.load(tag)
        ContainerHelper.loadAllItems(tag, items)
    }

    override fun saveAdditional(tag: CompoundTag) {
        super.saveAdditional(tag)
        ContainerHelper.saveAllItems(tag, items)
    }
}