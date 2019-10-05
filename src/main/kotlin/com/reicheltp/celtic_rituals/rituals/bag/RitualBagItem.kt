package com.reicheltp.celtic_rituals.rituals.bag

import com.reicheltp.celtic_rituals.MOD_ID
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.SplashPotionItem
import net.minecraft.stats.Stats
import net.minecraft.util.ActionResult
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import net.minecraft.world.World
import org.apache.logging.log4j.LogManager

class RitualBagItem : SplashPotionItem(Properties().setNoRepair().group(ItemGroup.MISC)) {
    companion object {
        private val LOGGER = LogManager.getLogger()
    }

    init {
        registryName = ResourceLocation(MOD_ID, "ritual_bag")
    }

    override fun onItemRightClick(worldIn: World, playerIn: PlayerEntity, handIn: Hand): ActionResult<ItemStack> {
        val itemStack = playerIn.getHeldItem(handIn)

        worldIn.playSound(
            playerIn,
            playerIn.posX,
            playerIn.posY,
            playerIn.posZ,
            SoundEvents.ENTITY_SPLASH_POTION_THROW,
            SoundCategory.PLAYERS,
            0.5f,
            0.4f / (Item.random.nextFloat() * 0.4f + 0.8f)
        )
        if (!worldIn.isRemote) {
            val ritualBagEntity = RitualBagEntity(worldIn, playerIn)

            ritualBagEntity.setItem(itemStack)
            ritualBagEntity.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, -20.0f, 0.7f, 1.0f)
            worldIn.addEntity(ritualBagEntity)
        }

        playerIn.addStat(Stats.ITEM_USED.get(this))
        return ActionResult(ActionResultType.SUCCESS, itemStack)
    }
}
