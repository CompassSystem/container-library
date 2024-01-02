package io.github.compasssystem.cocoli.impl_client.compat

import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.widget.Bounds
import io.github.compasssystem.cocoli.api_client.screen.AbstractScreen
import net.minecraft.client.renderer.Rect2i

object EmiCompat : EmiPlugin {
    override fun register(registry: EmiRegistry) {
        registry.addGenericExclusionArea { screen, consumer ->
            if (screen is AbstractScreen) {
                screen.getExclusionZones().map { it.asEmiRect() }.forEach(consumer)
            }
        }
    }

    private fun Rect2i.asEmiRect(): Bounds {
        return Bounds(x, y, width, height)
    }
}