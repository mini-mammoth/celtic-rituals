package com.reicheltp.celtic_rituals.utils

import net.minecraft.block.IGrowable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

object Grow {
    /**
     * Grow around a target with given radius
     */
    fun aroundTarget(world: World, center: BlockPos, radius: Int) {
        val from = center.add(radius, radius, 1)
        val to = center.add(-radius, -radius, -1)

        val rand = Random()

        for (pos in BlockPos.getAllInBox(from, to)) {
            val state = world.getBlockState(pos)
            val block = state.block

            if (block is IGrowable && block.canGrow(world, pos, state, world.isRemote)) {
                block.grow(world, rand, pos, state)
            }
        }
    }
}