package compass_system.cocoli.test

import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod

@Mod("cocoli_test")
class TestMain(modEventBus: IEventBus) {
    init {
        println("Hello, NeoForge!")
    }
}