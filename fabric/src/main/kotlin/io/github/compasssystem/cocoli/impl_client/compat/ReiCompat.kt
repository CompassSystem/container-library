package io.github.compasssystem.cocoli.impl_client.compat

import io.github.compasssystem.cocoli.api_client.screen.AbstractScreen
import io.github.compasssystem.cocoli.impl_client.screen.FakePickScreen
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.client.plugins.REIClientPlugin
import me.shedaniel.rei.api.client.registry.screen.ExclusionZones
import me.shedaniel.rei.api.client.registry.screen.OverlayDecider
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.renderer.Rect2i
import net.minecraft.world.InteractionResult

object ReiCompat : REIClientPlugin {
    override fun registerExclusionZones(zones: ExclusionZones) {
        zones.register(AbstractScreen::class.java) { screen ->
            screen.getExclusionZones().map { it.asReiRectangle() }
        }
    }

    override fun registerScreens(registry: ScreenRegistry) {
        registry.registerDecider(object : OverlayDecider {
            override fun <R : Screen> isHandingScreen(screen: Class<R>): Boolean {
                return screen == FakePickScreen::class.java
            }

            override fun <R : Screen> shouldScreenBeOverlaid(screen: R): InteractionResult {
                return InteractionResult.FAIL
            }
        })
    }

    private fun Rect2i.asReiRectangle(): Rectangle {
        return Rectangle(x, y, width, height)
    }
}