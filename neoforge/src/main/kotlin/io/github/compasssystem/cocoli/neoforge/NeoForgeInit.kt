package io.github.compasssystem.cocoli.neoforge

import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod

@Mod("cocoli")
class NeoForgeInit(modEventBus: IEventBus) {
    init {
        println("Hello, NeoForge!")
    }
}