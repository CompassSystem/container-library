package compass_system.cocoli.test

import com.google.common.base.Suppliers
import compass_system.cocoli.test.cart.BarrelMinecartEntity
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.Minecraft
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.renderer.entity.MinecartRenderer
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType

object TestClient : ClientModInitializer {
    override fun onInitializeClient() {
        for (i in 9..135 step 9) {
            val cartId = ResourceLocation("cocoli", "barrel_${i}_minecart")
            val entityType = BuiltInRegistries.ENTITY_TYPE.get(cartId) as EntityType<BarrelMinecartEntity>
            val cartItem = BuiltInRegistries.ITEM.get(cartId)

            EntityRendererRegistry.register(entityType) {
                MinecartRenderer(it, ModelLayers.CHEST_MINECART)
            }

            val renderEntity = Suppliers.memoize { entityType.create(Minecraft.getInstance().level!!) }

            BuiltinItemRendererRegistry.INSTANCE.register(cartItem) { _, _, stack, source, light, _ ->
                Minecraft.getInstance().entityRenderDispatcher.render(renderEntity.get()!!, 0.0, 0.0, 0.0, 0.0F, 0.0F, stack, source, light)
            }
        }
    }
}