package com.reicheltp.celtic_rituals.utils

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object Spawn {
    /**
     * Spawns the specified [numberOfEntites] of [entityType] randomly around the [blockPos].
     */
    fun <T : Entity> spawnEntityAroundPosition(
      world: World,
      entityType: EntityType<T>,
      playerIn: PlayerEntity?,
      blockPos: BlockPos,
      minDistance: Int,
      maxDistance: Int,
      numberOfEntites: Int = 1
    ): Boolean {
        require(minDistance < maxDistance) { "maxDistance must be greater than minDistance" }
        require(numberOfEntites > 0) { "numberOfEntites must be greater than 0" }
        val r = world.random

        var success = true
        for (i in 1..numberOfEntites) {
            val xDist = r.nextInt((maxDistance - minDistance) + 1) + minDistance
            val xOffset = if (r.nextBoolean()) xDist else (-1) * xDist

            val zDist = r.nextInt((maxDistance - minDistance) + 1) + minDistance
            val zOffset = if (r.nextBoolean()) zDist else (-1) * zDist

            var mobPos = BlockPos(blockPos.x + xOffset, blockPos.y, blockPos.z + zOffset)

            val blockState = world.getBlockState(mobPos)

            // update y position such that mob spawns on the ground
            if (blockState.getCollisionShape(world, mobPos).isEmpty) {
                var updatedBlockPos = mobPos.offset(Direction.DOWN)
                while (world.getBlockState(updatedBlockPos).getCollisionShape(
                        world,
                        updatedBlockPos
                    ).isEmpty
                ) {
                    mobPos = updatedBlockPos
                    updatedBlockPos = mobPos.offset(Direction.DOWN)
                }
            } else {
                var updatedBlockPos = mobPos.offset(Direction.UP)
                while (!world.getBlockState(updatedBlockPos).getCollisionShape(
                        world,
                        updatedBlockPos
                    ).isEmpty
                )
                    updatedBlockPos = updatedBlockPos.offset(Direction.UP)

                mobPos = updatedBlockPos
            }

            success = success && entityType.spawn(
                world,
                null,
                playerIn,
                mobPos,
                SpawnReason.MOB_SUMMONED,
                true,
                false
            ) != null
        }

        return success
    }
}
