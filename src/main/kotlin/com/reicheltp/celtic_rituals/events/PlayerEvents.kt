package com.reicheltp.celtic_rituals.events

import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.init.ModBlocks
import net.alexwells.kottle.KotlinEventBusSubscriber
import net.minecraft.block.Blocks
import net.minecraft.block.GrassBlock
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Items
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.util.Direction
import net.minecraft.util.Hand
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

@KotlinEventBusSubscriber(modid = MOD_ID)
object PlayerEvents {

    @SubscribeEvent
    fun boneRightClickOnDirtOrGrassEvent(event: PlayerInteractEvent.RightClickBlock) {
        if (event.hand == Hand.OFF_HAND)
            return

        val heldItemStackInMainHand = event.player.heldItemMainhand
        val block = event.world.getBlockState(event.pos).block

        val bonePos = BlockPos(event.pos.x, event.pos.y + 1, event.pos.z)

        if (heldItemStackInMainHand.item == Items.BONE && block is GrassBlock || block == Blocks.DIRT) {
            val boneStandState = ModBlocks.BONE_STAND!!.defaultState

            event.world.setBlockState(
                bonePos,
                boneStandState.with(BlockStateProperties.FACING, getFacingFromEntity(bonePos, event.player))
            )
            event.world.playSound(
                event.player,
                event.pos,
                SoundEvents.ITEM_SHOVEL_FLATTEN,
                SoundCategory.BLOCKS,
                1.0f,
                1.0f
            )
        }
    }

    private fun getFacingFromEntity(pos: BlockPos, entity: LivingEntity): Direction {
        val direction = Direction.getFacingFromVector(entity.posX - pos.x, 0.0, entity.posZ - pos.z)

        if (direction != Direction.DOWN && direction != Direction.UP)
            return direction
        else
            return Direction.WEST
    }
}
