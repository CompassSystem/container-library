package compass_system.cocoli.test.block

import compass_system.cocoli.test.TestMain
import io.github.compasssystem.cocoli.api.inventory.OpenableInventory
import io.github.compasssystem.cocoli.api.inventory.context.BlockContext
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties

class SimpleBarrelBlock(properties: Properties, slots: Int) : OpenableBlock(properties, slots), EntityBlock {
    init {
        registerDefaultState(stateDefinition.any().setValue(BlockStateProperties.FACING, Direction.NORTH))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(BlockStateProperties.FACING)
    }

    override fun getOpenableInventory(context: BlockContext): OpenableInventory {
        return context.level.getBlockEntity(context.pos) as OpenableInventory
    }

    override fun onInitialOpen(player: ServerPlayer) {

    }

    override fun getForcedScreenType(): ResourceLocation? {
        return null
    }

    override fun getInventoryType(): ResourceLocation {
        return BuiltInRegistries.BLOCK.getKey(this)
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
        return this.defaultBlockState().setValue(BlockStateProperties.FACING, context.nearestLookingDirection.opposite)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        return TestMain.barrelBlockEntityType.create(pos, state)
    }
}