package com.reicheltp.celtic_rituals.rituals.bag

import com.reicheltp.celtic_rituals.init.ModEntities
import com.reicheltp.celtic_rituals.init.ModItems
import net.minecraft.entity.EntityType
import net.minecraft.entity.IRendersAsItem
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ThrowableEntity
import net.minecraft.item.ItemStack
import net.minecraft.network.IPacket
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import net.minecraftforge.fml.network.FMLPlayMessages
import net.minecraftforge.fml.network.NetworkHooks

class RitualBagEntity : ThrowableEntity, IRendersAsItem {
    companion object {
        private val ITEM = EntityDataManager.createKey(RitualBagEntity::class.java, DataSerializers.ITEMSTACK)
    }

    constructor(world: World) : super(ModEntities.RITUAL_BAG_ENTITY, world)
    constructor(packet: FMLPlayMessages.SpawnEntity, world: World) : super(ModEntities.RITUAL_BAG_ENTITY, world)
    constructor(
        world: World,
        entity: LivingEntity
    ) : super(ModEntities.RITUAL_BAG_ENTITY as EntityType<out ThrowableEntity>, entity, world)

    constructor(
        world: World,
        posX: Double,
        posY: Double,
        posZ: Double
    ) : super(ModEntities.RITUAL_BAG_ENTITY as EntityType<out ThrowableEntity>, posX, posY, posZ, world)

    override fun onImpact(result: RayTraceResult) {
        if (!this.world.isRemote) {
            this.world.playSound(
                null as PlayerEntity?,
                this.position,
                SoundEvents.BLOCK_WOOL_BREAK,
                SoundCategory.PLAYERS,
                1f,
                1f
            )
        }

        // removes the entity
        remove()
    }

    override fun registerData() {
        this.getDataManager().register(ITEM, ItemStack.EMPTY)
    }

    fun setItem(stack: ItemStack) {
        this.getDataManager().set(ITEM, stack.copy())
    }

    override fun getItem(): ItemStack {
        val itemstack = this.getDataManager().get<ItemStack>(ITEM)

        if (itemstack.item !== ModItems.RITUAL_BAG) {
            if (this.world != null) {
                LOGGER.error("ThrownRitualBag entity {} has no item?!", this.entityId)
            }

            return ItemStack(ModItems.RITUAL_BAG)
        } else {
            return itemstack
        }
    }

    // this needs to be overriden for custom non living entities to render also see:
    // https://www.minecraftforge.net/forum/topic/72657-solved-1143-entity-renderer-not-rendering/
    // https://www.minecraftforge.net/forum/topic/75827-solved1144-problem-with-custom-entity-rendering-i-guess/
    // https://www.minecraftforge.net/forum/topic/71717-solved-1142-register-entities-and-rendering/
    override fun createSpawnPacket(): IPacket<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }

    class RitualBagFactory : EntityType.IFactory<RitualBagEntity> {
        override fun create(p_create_1_: EntityType<RitualBagEntity>, world: World): RitualBagEntity {
            return RitualBagEntity(world)
        }
    }
}
