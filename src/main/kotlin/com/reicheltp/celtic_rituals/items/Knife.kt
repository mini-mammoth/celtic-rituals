package com.reicheltp.celtic_rituals.items

import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.init.ModEnchantments
import com.reicheltp.celtic_rituals.init.ModItemGroups
import com.reicheltp.celtic_rituals.rituals.sacrifice.HeartItem
import com.reicheltp.celtic_rituals.utils.addItemOrDrop
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.ChickenEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemTier
import net.minecraft.item.Rarity
import net.minecraft.item.SwordItem
import net.minecraft.util.ResourceLocation

/**
 * This knife is used to sacrifice mobs to trigger rituals.
 *
 * Right-click a mob to sacrifice it.
 */
class Knife : SwordItem(
    ItemTier.WOOD, 1, 1f, Properties()
        .setNoRepair()
        .rarity(Rarity.COMMON)
        .group(ModItemGroups.DEFAULT)
) {
    init {
        registryName = ResourceLocation(MOD_ID, "knife")
    }

    override fun hitEntity(
      stack: ItemStack,
      target: LivingEntity,
      attacker: LivingEntity
    ): Boolean {
        val validTarget = target is ChickenEntity && target.health <= 0
        val lvl = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.HEART_BREAKER!!, stack)

        if (validTarget && lvl == 0) {
            stack.addEnchantment(ModEnchantments.HEART_BREAKER!!, 1)
            attacker.addItemOrDrop(HeartItem.from(target.type))
        }

        return super.hitEntity(stack, target, attacker)
    }
}
