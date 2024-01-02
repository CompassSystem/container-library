package compass_system.cocoli.test.block

import io.github.compasssystem.cocoli.api.CoCoLi
import io.github.compasssystem.cocoli.api.block.OpenableInventoryProvider
import io.github.compasssystem.cocoli.api.inventory.context.BlockContext
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

abstract class OpenableBlock(properties: Properties, val slots: Int) : Block(properties), OpenableInventoryProvider<BlockContext> {
    override fun use(state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, hit: BlockHitResult): InteractionResult {
        val isClient = level.isClientSide()

        if (!isClient) {
            CoCoLi.openInventory(this, BlockContext(level as ServerLevel, player as ServerPlayer, pos))
        }

        return InteractionResult.sidedSuccess(isClient)
    }
}