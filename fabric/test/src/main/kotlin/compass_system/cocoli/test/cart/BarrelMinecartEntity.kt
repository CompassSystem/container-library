package compass_system.cocoli.test.cart

import compass_system.cocoli.test.block.SimpleBarrelBlock
import io.github.compasssystem.cocoli.api.CoCoLi
import io.github.compasssystem.cocoli.api.block.OpenableInventoryProvider
import io.github.compasssystem.cocoli.api.inventory.OpenableInventory
import io.github.compasssystem.cocoli.api.inventory.context.BaseContext
import net.minecraft.core.Direction
import net.minecraft.core.NonNullList
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.*
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.vehicle.AbstractMinecart
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.Vec3

class BarrelMinecartEntity(
    entityType: EntityType<*>,
    level: Level,
    private val dropItem: Item,
    barrelBlock: SimpleBarrelBlock
) : AbstractMinecart(entityType, level), OpenableInventoryProvider<BaseContext>, OpenableInventory {
    private var renderState: BlockState = barrelBlock.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP)
    private var title: Component = dropItem.description
    var items: NonNullList<ItemStack> = NonNullList.withSize(barrelBlock.slots, ItemStack.EMPTY)
    private var barrelItem: Item = barrelBlock.asItem()
    private val inventory = object : Container {
        override fun clearContent() {
            items.clear()
        }

        override fun getContainerSize(): Int = items.size

        override fun isEmpty(): Boolean = items.all(ItemStack::isEmpty)

        override fun getItem(slot: Int): ItemStack = items[slot]

        override fun removeItem(slot: Int, amount: Int): ItemStack {
            val stack = ContainerHelper.removeItem(items, slot, amount)

            if (!stack.isEmpty) {
                this.setChanged()
            }

            return stack
        }

        override fun removeItemNoUpdate(slot: Int): ItemStack {
            return ContainerHelper.takeItem(items, slot)
        }

        override fun setItem(slot: Int, stack: ItemStack) {
            items[slot] = stack

            if (stack.count > this.maxStackSize) {
                stack.count = this.maxStackSize
            }

            this.setChanged()
        }

        override fun setChanged() {

        }

        override fun stillValid(player: Player): Boolean {
            return this@BarrelMinecartEntity.isAlive && player.distanceToSqr(this@BarrelMinecartEntity) <= 36.0
        }
    }

    override fun getPickResult() = ItemStack(dropItem)

    override fun getDisplayBlockState(): BlockState = renderState

    override fun getMinecartType() = Type.CHEST

    override fun getDropItem() = dropItem

    override fun getForcedScreenType() = null

    override fun getInventoryType() = BuiltInRegistries.ENTITY_TYPE.getKey(type)

    override fun canPlayerOpen(player: ServerPlayer): Boolean = inventory().stillValid(player)

    override fun title(): Component = if (this.hasCustomName()) customName!! else title

    override fun getOpenableInventory(context: BaseContext): OpenableInventory = this

    override fun onInitialOpen(player: ServerPlayer) {

    }

    override fun inventory(): Container = inventory

    override fun remove(reason: RemovalReason) {
        if (!level().isClientSide() && reason.shouldDestroy()) {
            Containers.dropContents(level(), this, inventory())
        }

        super.remove(reason)
    }

    override fun destroy(source: DamageSource) {
        this.kill()

        //noinspection resource
        if (level().gameRules.getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            val breaker = source.directEntity

            if (breaker != null && breaker.isShiftKeyDown) {
                val stack = ItemStack(barrelItem)

                if (this.hasCustomName()) {
                    stack.setHoverName(customName)
                }

                this.spawnAtLocation(stack)
                this.spawnAtLocation(Items.MINECART)
            } else {
                val stack = ItemStack(getDropItem())

                if (this.hasCustomName()) {
                    stack.setHoverName(customName)
                }

                this.spawnAtLocation(stack)
            }
        }
    }

    override fun interact(player: Player, hand: InteractionHand): InteractionResult {
        val isClient = level().isClientSide()

        if (!isClient) {
            CoCoLi.openInventory(this, BaseContext(level() as ServerLevel, player as ServerPlayer))
        }

        return InteractionResult.sidedSuccess(isClient)
    }

    override fun addAdditionalSaveData(tag: CompoundTag) {
        super.addAdditionalSaveData(tag)
        ContainerHelper.saveAllItems(tag, items)
    }

    override fun readAdditionalSaveData(tag: CompoundTag) {
        super.readAdditionalSaveData(tag)
        ContainerHelper.loadAllItems(tag, items)
    }

    override fun getDisplayOffset(): Int = 12

    companion object {
        fun create(level: Level, posVec: Vec3, cartId: ResourceLocation) : BarrelMinecartEntity {
            return level.registryAccess().registry(Registries.ENTITY_TYPE).map {
                val cart = it[cartId]!!.create(level)!!
                cart.setPos(posVec)

                cart as BarrelMinecartEntity
            }.orElseThrow()
        }
    }
}