package com.reicheltp.celtic_rituals.init

import com.reicheltp.celtic_rituals.rituals.bag.RitualBagEntity
import net.minecraft.entity.EntityType
import net.minecraftforge.registries.ObjectHolder

object ModEntities {
    @ObjectHolder("celtic_rituals:ritual_bag_entity")
    var RITUAL_BAG_ENTITY: EntityType<RitualBagEntity>? = null
}
