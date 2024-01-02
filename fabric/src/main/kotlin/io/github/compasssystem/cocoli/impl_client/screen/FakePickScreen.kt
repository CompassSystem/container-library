package io.github.compasssystem.cocoli.impl_client.screen

import io.github.compasssystem.cocoli.api_client.screen.AbstractScreen
import io.github.compasssystem.cocoli.impl.AbstractInventoryHandler
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class FakePickScreen(
    handler: AbstractInventoryHandler,
    playerInventory: Inventory,
    title: Component
) : AbstractScreen(handler, playerInventory, title) {
    override fun renderBg(graphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {

    }
}