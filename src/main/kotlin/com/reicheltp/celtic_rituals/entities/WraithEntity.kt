package com.reicheltp.celtic_rituals.entities

import com.reicheltp.celtic_rituals.init.ModEntities
import com.reicheltp.celtic_rituals.rituals.bag.RitualBagEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.MeleeAttackGoal
import net.minecraft.entity.ai.goal.SwimGoal
import net.minecraft.entity.monster.MonsterEntity
import net.minecraft.world.World

class WraithEntity(world: World) :
    MonsterEntity(ModEntities.WRAITH_ENTITY as EntityType<out MonsterEntity>, world) {

    override fun registerGoals() {
        this.goalSelector.addGoal(0, MeleeAttackGoal(this, 10.0, true))
    }

    override fun registerAttributes() {
        super.registerAttributes()
    }

    class WraithFactory : EntityType.IFactory<WraithEntity> {
        override fun create(
          entityType: EntityType<WraithEntity>,
          world: World
        ): WraithEntity {
            return WraithEntity(world)
        }
    }
}
