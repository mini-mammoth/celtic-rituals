package com.reicheltp.celtic_rituals.events

import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.ModBlocks
import com.reicheltp.celtic_rituals.blocks.BoneStandBlock
import net.alexwells.kottle.KotlinEventBusSubscriber
import net.minecraft.block.Blocks
import net.minecraft.block.FarmlandBlock
import net.minecraft.block.GrassBlock
import net.minecraft.block.SnowyDirtBlock
import net.minecraft.item.Items
import net.minecraft.util.Hand
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.Tags
import java.util.*

@KotlinEventBusSubscriber(modid = MOD_ID)
object PlayerEvents {

    @SubscribeEvent
    fun BoneRightClickOnDirtOrGrassEvent(event: PlayerInteractEvent.RightClickBlock) {
        if (event.hand == Hand.OFF_HAND)
            return

        val heldItemStackInMainHand = event.player.heldItemMainhand
        val block = event.world.getBlockState(event.pos).block

        val bonePos = BlockPos(event.pos.x, event.pos.y + 1, event.pos.z)
        Items.STONE_SHOVEL
        if (heldItemStackInMainHand != null && heldItemStackInMainHand.item == Items.BONE && block is GrassBlock || block == Blocks.DIRT)
        {
            event.world.setBlockState(bonePos, ModBlocks.BONE_STAND?.defaultState)
            event.world.playSound(event.player, event.pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0f, 1.0f)
        }
    }
}