package compass_system.cocoli.test

import compass_system.cocoli.test.block.SimpleBarrelBlock
import compass_system.cocoli.test.block.SimpleBarrelBlockEntity
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour

object TestMain : ModInitializer {
    lateinit var barrelBlockEntityType: BlockEntityType<SimpleBarrelBlockEntity>

    override fun onInitialize() {
        val barrels = buildMap {
            for (i in 9..135 step 9) {
                put(i, SimpleBarrelBlock(BlockBehaviour.Properties.of(), i))
            }
        }

        barrels.forEach {
            val id = ResourceLocation("cocoli", "barrel_${it.key}")

            Registry.register(BuiltInRegistries.BLOCK, id, it.value)
            Registry.register(BuiltInRegistries.ITEM, id, BlockItem(it.value, Item.Properties()))
        }

        barrelBlockEntityType = BlockEntityType.Builder.of(::SimpleBarrelBlockEntity, *barrels.values.toTypedArray()).build(null)
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation("cocoli", "barrel"), barrelBlockEntityType)

        val tab = FabricItemGroup.builder()
            .icon { Items.BARREL.defaultInstance }
            .displayItems { _, output ->
                output.acceptAll(barrels.values.map { it.asItem().defaultInstance })
            }
            .title(Component.translatable("itemGroup.cocoli.tab"))
            .build()
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceLocation("cocoli", "tab"), tab)
    }
}