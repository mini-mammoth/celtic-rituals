package com.reicheltp.celtic_rituals.rituals.sacrifice

import com.reicheltp.celtic_rituals.MOD_ID
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentType
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.inventory.InventoryHelper
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation

/**
 * A heart-breaking weapon has a chance to drop the heart of a mob.
 */
class HeartBreakerEnchantment : Enchantment(
    Rarity.VERY_RARE,
    EnchantmentType.WEAPON,
    arrayOf(EquipmentSlotType.MAINHAND)
) {
    init {
        registryName = ResourceLocation(MOD_ID, "heart_breaker")
    }

    override fun isAllowedOnBooks(): Boolean = false

    override fun getMaxLevel(): Int = 4

    override fun getMinLevel(): Int = 1

    override fun onEntityDamaged(user: LivingEntity, target: Entity, level: Int) {
        if (target is LivingEntity && target.health > 0) {
            return
        }

        if (user.world.random.nextFloat() < 0.2 * level.toDouble()) {
            val heart = HeartItem.from(target.type)

            if (user !is PlayerEntity || !user.addItemStackToInventory(heart)) {
                InventoryHelper.dropItems(user.world, user.position, NonNullList.from(heart))
            }
        }
    }
}
