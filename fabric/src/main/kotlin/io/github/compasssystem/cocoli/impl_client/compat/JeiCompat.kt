package io.github.compasssystem.cocoli.impl_client.compat

import io.github.compasssystem.cocoli.api_client.screen.AbstractScreen
import io.github.compasssystem.cocoli.impl.Utils
import io.github.compasssystem.cocoli.impl_client.screen.FakePickScreen
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.gui.handlers.IGuiContainerHandler
import mezz.jei.api.registration.IGuiHandlerRegistration
import net.minecraft.client.renderer.Rect2i

@JeiPlugin
object JeiCompat : IModPlugin {
    override fun getPluginUid() = Utils.id("jei_plugin")

    override fun registerGuiHandlers(registration: IGuiHandlerRegistration) {
        registration.addGuiContainerHandler(AbstractScreen::class.java, object : IGuiContainerHandler<AbstractScreen> {
            override fun getGuiExtraAreas(screen: AbstractScreen): List<Rect2i> {
                return screen.getExclusionZones()
            }
        })

        registration.addGuiScreenHandler(FakePickScreen::class.java) { null }
    }
}