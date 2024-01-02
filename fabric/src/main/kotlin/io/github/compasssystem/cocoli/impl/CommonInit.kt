package io.github.compasssystem.cocoli.impl

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries

object CommonInit : ModInitializer {
    val inventoryScreenProviderType: ExtendedScreenHandlerType<AbstractInventoryHandler> = Registry.register(BuiltInRegistries.MENU, Utils.id("handler_type"), ExtendedScreenHandlerType(AbstractInventoryHandler::createInventoryHandler))

    override fun onInitialize() {

    }
}
