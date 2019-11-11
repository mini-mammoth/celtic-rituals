package com.reicheltp.celtic_rituals.entities.kobold

import javax.vecmath.Vector3d
import net.minecraft.entity.MobEntity
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World

class FollowPlayerGoal : Goal {
    private var _world: World
    private var _entity: MobEntity
    private var _maxFollowRange: Double
    private var _minDistanceToPlayer: Double
    private var _speedTowardsTarget: Double

    private var _nearestPlayer: PlayerEntity? = null

    constructor(
      entity: MobEntity,
      world: World,
      maxFollowRange: Double,
      minDistanceToPlayer: Double,
      speedTowardsTarget: Double
    ) : super() {
        _entity = entity
        _world = world
        _maxFollowRange = maxFollowRange
        _minDistanceToPlayer = minDistanceToPlayer
        _speedTowardsTarget = speedTowardsTarget
    }

    override fun shouldExecute(): Boolean {
        return true
    }

    override fun startExecuting() {
        _entity.setAggroed(true)
        super.startExecuting()
    }

    override fun shouldContinueExecuting(): Boolean {
        _nearestPlayer = _world.getClosestPlayer(_entity, _maxFollowRange)
        return _nearestPlayer != null
    }

    override fun tick() {
        if (_nearestPlayer == null)
            return

        val distanceToNearestPlayer = _entity.getDistance(_nearestPlayer)

        // Nothing to be done here, player is already close enough
        if (distanceToNearestPlayer <= _minDistanceToPlayer)
            return

        var directionToPlayerVector = Vector3d(
            _nearestPlayer!!.posX - _entity.posX,
            _nearestPlayer!!.posY - _entity.posY,
            _nearestPlayer!!.posZ - _entity.posZ)
        directionToPlayerVector.normalize()
        directionToPlayerVector.scale(distanceToNearestPlayer - _minDistanceToPlayer)

        val followPoint = Vector3d(_entity.posX, _entity.posY, _entity.posZ)
        followPoint.add(directionToPlayerVector)

        _entity.navigator.tryMoveToXYZ(
            followPoint.x,
            followPoint.y,
            followPoint.z,
            _speedTowardsTarget)
    }
}
