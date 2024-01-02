package io.github.compasssystem.cocoli.api.inventory.context

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer

class BlockContext(level: ServerLevel, player: ServerPlayer, val pos: BlockPos) : BaseContext(level, player)
