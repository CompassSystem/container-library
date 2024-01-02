package io.github.compasssystem.cocoli.api.block

import io.github.compasssystem.cocoli.api.inventory.OpenableInventory
import io.github.compasssystem.cocoli.api.inventory.context.BaseContext
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer

interface OpenableInventoryProvider<T : BaseContext> {
    fun getOpenableInventory(context: T): OpenableInventory

    /**
     * Callback for running code when an inventory is initially opened, can be used to award opening stats.
     */
    fun onInitialOpen(player: ServerPlayer)

    /**
     * @return A screen type to force instead of allowing the user to decide.
     * Return value is only used on the client side but should be consistent.
     */
    fun getForcedScreenType(): ResourceLocation?

    /**
     * @return The inventory type, used for configuration of screen types, should return block id, item id, ect.
     */
    fun getInventoryType(): ResourceLocation
}