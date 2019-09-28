package com.reicheltp.mymod.items

import com.reicheltp.mymod.MyMod
import com.reicheltp.mymod.utils.Grow
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.ChickenEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.world.World
import org.apache.logging.log4j.LogManager

class Knife : Item(Properties()
        .maxStackSize(1)
        .maxDamage(1)
        .group(ItemGroup.MISC)) {
    companion object {
        private val LOGGER = LogManager.getLogger()
    }

    init {
        registryName = ResourceLocation(MyMod.MOD_ID, "knife")
    }

    override fun itemInteractionForEntity(stack: ItemStack, playerIn: PlayerEntity, target: LivingEntity, hand: Hand): Boolean {
        LOGGER.info("INTERACTING with {} {}", target, hand)

        val validTarget = hand == Hand.MAIN_HAND && target is ChickenEntity

        if (validTarget) {
            target.attackEntityFrom(DamageSource.MAGIC, target.maxHealth)
            Grow.aroundTarget(target.world, target.position, 3)

            return true
        }

        return super.itemInteractionForEntity(stack, playerIn, target, hand)
    }

    override fun onItemRightClick(worldIn: World, playerIn: PlayerEntity, handIn: Hand): ActionResult<ItemStack> {
        if (handIn != Hand.MAIN_HAND) {
            return super.onItemRightClick(worldIn, playerIn, handIn)
        }

        LOGGER.info("RIGHT CLICK WITH with {}", handIn)


        return ActionResult(ActionResultType.PASS, playerIn.getHeldItem(handIn))
    }
}