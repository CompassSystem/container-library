package io.github.compasssystem.cocoli.impl

import net.minecraft.resources.ResourceLocation

object Utils {
    private const val MOD_ID = "cocoli"
    const val SLOT_SIZE = 18

    fun id(path: String) = ResourceLocation(MOD_ID, path)
}