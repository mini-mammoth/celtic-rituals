package com.reicheltp.celtic_rituals.entities.kobold

import com.reicheltp.celtic_rituals.init.ModEntities
import com.reicheltp.celtic_rituals.init.ModSoundEvents
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.ITickable
import net.minecraft.entity.EntityType
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.goal.MeleeAttackGoal
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal
import net.minecraft.entity.monster.MonsterEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.SoundCategory
import net.minecraft.world.World

class KoboldEntity(world: World) :
    MonsterEntity(ModEntities.KOBOLD_ENTITY as EntityType<out MonsterEntity>, world), ITickable {

    var isDisguised = false
    var chuckleDelayInSeconds = 10

    private var chuckleTicks = 0

    override fun registerGoals() {
        applyEntityAI()
    }

    protected fun applyEntityAI() {
        this.goalSelector.addGoal(2, MeleeAttackGoal(this, 1.1, false))

        this.goalSelector.addGoal(7, WaterAvoidingRandomWalkingGoal(this, 1.2))

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

    override fun tick() {
        if (isDisguised) {
            chuckleTicks++
            if (chuckleTicks >= chuckleDelayInSeconds * 20) {
                Minecraft.getInstance().world.playSound(
                    position,
                    ModSoundEvents.KOBOLD_CHUCKLE,
                    SoundCategory.HOSTILE,
                    1f,
                    1f,
                    false)
                chuckleTicks = 0
            }
        } else
            chuckleTicks = 0

        super.tick()
    }

    override fun onCollideWithPlayer(entityIn: PlayerEntity) {
        isDisguised = true
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
