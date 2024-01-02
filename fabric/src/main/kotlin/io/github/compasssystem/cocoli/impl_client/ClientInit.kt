package io.github.compasssystem.cocoli.impl_client

import io.github.compasssystem.cocoli.api_client.screen.AbstractScreen
import io.github.compasssystem.cocoli.impl.AbstractInventoryHandler
import io.github.compasssystem.cocoli.impl.CommonInit
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

object ClientInit : ClientModInitializer {
    var activeInventoryType: ResourceLocation? = null
    var activeForcedScreenType: ResourceLocation? = null

    override fun onInitializeClient() {
        MenuScreens.register(CommonInit.inventoryScreenProviderType, ::createScreen)
    }

    private fun createScreen(handler: AbstractInventoryHandler, playerInventory: Inventory, title: Component): AbstractScreen {
        handler.placeClientSlots()
        return AbstractScreen(handler, playerInventory, title)
    }
}