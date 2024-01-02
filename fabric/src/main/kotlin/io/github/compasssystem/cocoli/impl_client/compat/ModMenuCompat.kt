package io.github.compasssystem.cocoli.impl_client.compat

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import io.github.compasssystem.cocoli.impl_client.screen.PickScreen

object ModMenuCompat : ModMenuApi {
    override fun getModConfigScreenFactory() = ::PickScreen as ConfigScreenFactory<PickScreen>
}