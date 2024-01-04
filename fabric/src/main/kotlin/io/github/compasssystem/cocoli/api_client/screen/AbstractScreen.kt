package io.github.compasssystem.cocoli.api_client.screen

import io.github.compasssystem.cocoli.impl.AbstractInventoryHandler
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.Rect2i
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

open class AbstractScreen(
    menu: AbstractInventoryHandler,
    playerInventory: Inventory,
    title: Component
) : AbstractContainerScreen<AbstractInventoryHandler>(menu, playerInventory, title) {
    fun getExclusionZones(): List<Rect2i> = emptyList()

    override fun renderBg(graphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {

    }
}