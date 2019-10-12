package com.reicheltp.celtic_rituals.ingredients.mistletoe

import com.reicheltp.celtic_rituals.MOD_ID
import java.util.Random
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.FireBlock
import net.minecraft.block.LeavesBlock
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.tags.BlockTags
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * Mistletoe leaves are parasites which slowly eats all leaves of a tree.
 * If there are no leaves left they die. Only those directly attached to a log will survive.
 *
 * Right-click a mistletoe leave to collect one mistletoe branch.
 *
 * You can use a mistletoe branch to infect leaves of a tree.
 */
class MistletoeLeavesBlock : LeavesBlock(
    Properties
        .create(Material.LEAVES)
        .hardnessAndResistance(0.2f)
        .tickRandomly()
        .sound(SoundType.PLANT)
) {

    init {
        registryName = ResourceLocation(MOD_ID, "mistletoe_leaves")

        // Fire can spread on mistletoe leaves.
        // Encouragement and flammability values are copied from oak leaves.
        (Blocks.FIRE as FireBlock).setFireInfo(this, 30, 60)
    }

    override fun ticksRandomly(state: BlockState): Boolean {
        return !state.get(PERSISTENT)
    }

    override fun randomTick(state: BlockState, worldIn: World, pos: BlockPos, random: Random) {
        if (state.get(DISTANCE) == 7 || state.get(PERSISTENT)) {
            super.randomTick(state, worldIn, pos, random)
        }

        fun isValid(blockPos: BlockPos): Boolean {
            val targetState = worldIn.getBlockState(blockPos)
            return targetState.isIn(BlockTags.LEAVES) && targetState.block != this
        }

        // Pick a direction to occupy another leave
        val targetPos = when {
            isValid(pos.up()) -> pos.up()
            isValid(pos.down()) -> pos.down()
            isValid(pos.north()) -> pos.north()
            isValid(pos.west()) -> pos.west()
            isValid(pos.south()) -> pos.south()
            isValid(pos.east()) -> pos.east()
            else -> BlockPos.ZERO
        }

        if (targetPos == BlockPos.ZERO) {
            if (state.get(DISTANCE) == 1) {
                // Leave those which are directly attached to a log
                return
            } else {
                // Nothing left to consume
                Block.spawnDrops(state, worldIn, pos)
                worldIn.removeBlock(pos, false)
            }
        } else {
            val spread = random.nextInt(10) <= 4
            if (spread) {
                // Substitute the target location
                val targetState = worldIn.getBlockState(targetPos)
                val newState = this.defaultState.with(DISTANCE, targetState.get(DISTANCE))
                worldIn.setBlockState(targetPos, newState)
            } else {
                // Consume neighbor
                worldIn.removeBlock(targetPos, false)
            }
        }
    }
}
