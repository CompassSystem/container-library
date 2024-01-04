package compass_system.cocoli.test.cart

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.dispenser.BlockSource
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseRailBlock
import net.minecraft.world.level.block.DispenserBlock
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.Vec3

class BarrelMinecartItem(properties: Properties, val cartId: ResourceLocation) : Item(properties) {
    init {
        DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOUR)
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        val pos = context.clickedPos
        val state = level.getBlockState(pos)

        if (!BaseRailBlock.isRail(state)) return InteractionResult.FAIL

        val handStack = context.itemInHand

        if (!level.isClientSide()) {
            val railShape = state.getValue((state.block as BaseRailBlock).shapeProperty)
            val railHeight = if (railShape.isAscending) 0.5 else 0.0
            val spawnPos = Vec3(pos.x + 0.5, pos.y + railHeight + 0.0625, pos.z + 0.5) // 1/16th of a block above the rail
            val cart = BarrelMinecartEntity.create(level, spawnPos, cartId)

            if (handStack.hasCustomHoverName()) cart.customName = handStack.hoverName

            level.addFreshEntity(cart)
            level.gameEvent(GameEvent.ENTITY_PLACE, pos, GameEvent.Context.of(context.player, level.getBlockState(pos.below())))
        }

        handStack.shrink(1)

        return InteractionResult.sidedSuccess(level.isClientSide())
    }

    companion object {
        private val DISPENSER_BEHAVIOUR = object : DefaultDispenseItemBehavior() {
            private val defaultDispenserBehaviour = DefaultDispenseItemBehavior()

            override fun execute(source: BlockSource, stack: ItemStack): ItemStack {
                val forward: Direction = source.state.getValue(DispenserBlock.FACING)
                val x: Double = source.pos().x + forward.stepX * 1.125
                val y = source.pos().y + forward.stepY
                val z: Double = source.pos.z + forward.stepZ * 1.125
                val frontPos: BlockPos = source.pos.relative(forward)
                val level: Level = source.level
                val frontState = level.getBlockState(frontPos)

                var railHeight = -2.0

                if (BaseRailBlock.isRail(frontState)) {
                    val railShape = frontState.getValue((frontState.block as BaseRailBlock).shapeProperty)

                    railHeight = if (railShape.isAscending) 0.5 else 0.0
                } else if (frontState.isAir) {
                    val belowFrontState = level.getBlockState(frontPos.below())

                    if (BaseRailBlock.isRail(belowFrontState)) {
                        val railShape = belowFrontState.getValue((belowFrontState.block as BaseRailBlock).shapeProperty)

                        railHeight = if (forward != Direction.DOWN && railShape.isAscending) -0.5 else -1.0
                    }
                }

                if (railHeight == -2.0) { // No rail detected
                    defaultDispenserBehaviour.dispense(source, stack)
                } else {
                    val posVec = Vec3(x, y + railHeight + 0.0625, z) // 1/16th of a block above the rail
                    val cart = BarrelMinecartEntity.create(level, posVec, (stack.item as BarrelMinecartItem).cartId)

                    if (stack.hasCustomHoverName()) {
                        cart.customName = stack.hoverName
                    }

                    level.addFreshEntity(cart)
                }

                stack.shrink(1)

                return stack
            }
        }
    }
}