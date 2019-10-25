package com.reicheltp.celtic_rituals.entities.kobold

import com.reicheltp.celtic_rituals.init.ModEntities
import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.MeleeAttackGoal
import net.minecraft.entity.monster.MonsterEntity
import net.minecraft.world.World

class KoboldEntity(world: World) :
    MonsterEntity(ModEntities.KOBOLD_ENTITY as EntityType<out MonsterEntity>, world) {

    override fun registerGoals() {
        this.goalSelector.addGoal(0, MeleeAttackGoal(this, 10.0, true))
    }

    override fun registerAttributes() {
        super.registerAttributes()
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
