package com.reicheltp.celtic_rituals.entities.kobold

import com.reicheltp.celtic_rituals.init.ModEntities
import net.minecraft.client.renderer.texture.ITickable
import net.minecraft.entity.EntityType
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal
import net.minecraft.entity.monster.MonsterEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World

class KoboldEntity(world: World) :
    MonsterEntity(ModEntities.KOBOLD_ENTITY as EntityType<out MonsterEntity>, world), ITickable {

    var isDisguised = false

    override fun registerGoals() {
        applyEntityAI()
    }

    protected fun applyEntityAI() {
        this.goalSelector.addGoal(2, FollowPlayerGoal(this, world, 16.0, 5.0, 1.0))

        // this.goalSelector.addGoal(7, WaterAvoidingRandomWalkingGoal(this, 1.2))

        this.targetSelector.addGoal(
            2,
            NearestAttackableTargetGoal(this, PlayerEntity::class.java, true)
        )
    }

    override fun registerAttributes() {
        super.registerAttributes()
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).baseValue = 35.0
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).baseValue = 0.25f.toDouble()
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).baseValue = 3.0
        this.getAttribute(SharedMonsterAttributes.ARMOR).baseValue = 2.0
    }

    override fun onCollideWithPlayer(entityIn: PlayerEntity) {
        if (!isDisguised) {
            isDisguised = true
        }

        super.onCollideWithPlayer(entityIn)
    }

    class KoboldFactory : EntityType.IFactory<KoboldEntity> {
        override fun create(
          entityType: EntityType<KoboldEntity>,
          world: World
        ): KoboldEntity {
            return KoboldEntity(world)
        }
    }
}
