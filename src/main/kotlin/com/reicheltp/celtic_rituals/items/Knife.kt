package com.reicheltp.celtic_rituals.items

import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.utils.Grow
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.ChickenEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import org.apache.logging.log4j.LogManager

/**
 * This knife is used to sacrifice mobs to trigger rituals.
 *
 * Right-click a mob to sacrifice it.
 */
class Knife : Item(Properties()
        .maxStackSize(1)
        .maxDamage(1)
        .group(ItemGroup.MISC)) {
    companion object {
        private val LOGGER = LogManager.getLogger()
    }

    init {
        registryName = ResourceLocation(MOD_ID, "knife")
    }

    override fun itemInteractionForEntity(stack: ItemStack, playerIn: PlayerEntity, target: LivingEntity, hand: Hand): Boolean {
        val validTarget = hand == Hand.MAIN_HAND && target is ChickenEntity

        if (validTarget) {
            target.attackEntityFrom(DamageSource.MAGIC, target.maxHealth)
            Grow.aroundTarget(target.world, target.position, 3)

            return true
        }

        return super.itemInteractionForEntity(stack, playerIn, target, hand)
    }
}