package io.github.compasssystem.cocoli.api

import io.github.compasssystem.cocoli.api.block.OpenableInventoryProvider
import io.github.compasssystem.cocoli.api.inventory.context.BaseContext
import io.github.compasssystem.cocoli.api.inventory.context.BlockContext
import io.github.compasssystem.cocoli.impl.inventory.BlockInventoryScreenProvider
import io.github.compasssystem.cocoli.impl.inventory.EntityInventoryScreenProvider
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity

object CoCoLi {
    /**
     * Open an inventory provided by a block, should be called only on the logical server.
     */
    fun openInventory(provider: OpenableInventoryProvider<BlockContext>, context: BlockContext) {
        val player = context.player
        val inventory = provider.getOpenableInventory(context)
        val title = inventory.title()

        if (!inventory.canPlayerOpen(player)) {
            player.displayClientMessage(Component.translatable("container.isLocked", title), true)
            player.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F)
            return
        }

        if (!player.isSpectator) {
            provider.onInitialOpen(player)
        }

        player.openMenu(BlockInventoryScreenProvider(context.pos, title, inventory.inventory()))
    }

    fun <T> openInventory(provider: T, context: BaseContext) where T : OpenableInventoryProvider<BaseContext>, T : Entity {
        val player = context.player
        val inventory = provider.getOpenableInventory(context)
        val title = inventory.title()

        if (!inventory.canPlayerOpen(player)) {
            player.displayClientMessage(Component.translatable("container.isLocked", title), true)
            player.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F)
            return
        }

        if (!player.isSpectator) {
            provider.onInitialOpen(player)
        }

        player.openMenu(EntityInventoryScreenProvider(provider, title, inventory.inventory()))
    }
}