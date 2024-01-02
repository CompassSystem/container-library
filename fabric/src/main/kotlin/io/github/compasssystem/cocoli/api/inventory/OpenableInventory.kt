package io.github.compasssystem.cocoli.api.inventory

import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.Container

interface OpenableInventory {
    fun canPlayerOpen(player: ServerPlayer): Boolean

    fun inventory(): Container

    fun title(): Component
}