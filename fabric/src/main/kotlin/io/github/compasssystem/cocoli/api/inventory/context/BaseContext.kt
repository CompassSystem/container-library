package io.github.compasssystem.cocoli.api.inventory.context

import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer

open class BaseContext(val level: ServerLevel, val player: ServerPlayer)
