package compass_system.cocoli.test

import com.google.common.collect.ImmutableSet
import compass_system.cocoli.test.block.SimpleBarrelBlock
import compass_system.cocoli.test.block.SimpleBarrelBlockEntity
import compass_system.cocoli.test.cart.BarrelMinecartEntity
import compass_system.cocoli.test.cart.BarrelMinecartItem
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityDimensions
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.flag.FeatureFlagSet
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

        val barrelCartItems = buildMap {
            for (i in 9..135 step 9) {
                put(i, BarrelMinecartItem(Item.Properties(), ResourceLocation("cocoli", "barrel_${i}_minecart")))
            }
        }

        val barrelMinecarts = buildMap {
            for (i in 9..135 step 9) {
                val minecartEntityType = EntityType({ type, level ->
                    BarrelMinecartEntity(type, level, barrelCartItems[i]!!, barrels[i]!!)
                }, MobCategory.MISC, true, true, false, false, ImmutableSet.of(), EntityDimensions.scalable(0.98F, 0.7F), 8, 3, FeatureFlagSet.of())

                put(i, minecartEntityType)
            }
        }

        barrels.forEach {
            val id = ResourceLocation("cocoli", "barrel_${it.key}")

            Registry.register(BuiltInRegistries.BLOCK, id, it.value)
            Registry.register(BuiltInRegistries.ITEM, id, BlockItem(it.value, Item.Properties()))
        }

        barrelCartItems.forEach {
            val id = ResourceLocation("cocoli", "barrel_${it.key}_minecart")

            Registry.register(BuiltInRegistries.ITEM, id, it.value)
        }

        barrelMinecarts.forEach {
            val id = ResourceLocation("cocoli", "barrel_${it.key}_minecart")

            Registry.register(BuiltInRegistries.ENTITY_TYPE, id, it.value)
        }

        barrelBlockEntityType = BlockEntityType.Builder.of(::SimpleBarrelBlockEntity, *barrels.values.toTypedArray()).build(null)
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation("cocoli", "barrel"), barrelBlockEntityType)

        val tab = FabricItemGroup.builder()
            .icon { Items.BARREL.defaultInstance }
            .displayItems { _, output ->
                output.acceptAll(barrels.values.map { it.asItem().defaultInstance })
                output.acceptAll(barrelCartItems.values.map { it.defaultInstance })
            }
            .title(Component.translatable("itemGroup.cocoli.tab"))
            .build()
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceLocation("cocoli", "tab"), tab)
    }
}