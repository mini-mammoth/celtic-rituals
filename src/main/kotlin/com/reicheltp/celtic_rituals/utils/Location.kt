package com.reicheltp.celtic_rituals.utils

import javax.vecmath.Vector2d
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

/**
 * Generates a sequence of offsets with given [radius].
 *
 * Use this with `for((i,j) in voxelCircle(radius) {...}`
 */
fun voxelCircle(radius: Int): Sequence<Pair<Int, Int>> {
    return sequence {
        // Center
        yield(Pair(0, 0))

        // Quadrant
        for (i in 1 until radius + 1) {
            for (j in 1 until radius + 1) {
                if (Vector2d(i.toDouble(), j.toDouble()).length() > (radius + 0.5)) {
                    continue
                }

                yield(Pair(i, j))
                yield(Pair(i, -j))
                yield(Pair(-i, j))
                yield(Pair(-i, -j))
            }
        }

        // Cross
        for (k in 1 until radius + 1) {
            yield(Pair(k, 0))
            yield(Pair(-k, 0))
            yield(Pair(0, k))
            yield(Pair(0, -k))
        }
    }
}
