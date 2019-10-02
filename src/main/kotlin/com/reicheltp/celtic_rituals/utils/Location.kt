package com.reicheltp.celtic_rituals.utils

import net.minecraft.block.Block
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * Looks in a given radius around you to find a Block of given type.
 */
fun findBlockInRadius(world: World, center: BlockPos, radius: Int, type: Block): BlockPos? {
    val from = center.add(radius, radius, 1)
    val to = center.add(-radius, -radius, -1)

    for (pos in BlockPos.getAllInBox(from, to)) {
        if (world.getBlockState(pos).block == type) {
            return pos
        }
    }

    return null
}
