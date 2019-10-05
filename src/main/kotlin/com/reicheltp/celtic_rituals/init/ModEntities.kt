package com.reicheltp.celtic_rituals.init

import com.reicheltp.celtic_rituals.rituals.bag.RitualBagEntity
import net.minecraft.entity.EntityClassification
import net.minecraft.entity.EntityType
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.registries.ObjectHolder

object ModEntities {
    @ObjectHolder("celtic_rituals:ritual_bag_entity")
    var RITUAL_BAG_ENTITY: EntityType<RitualBagEntity> =
        EntityType.Builder.create(RitualBagEntity.RitualBagFactory(), EntityClassification.MISC)
            .setCustomClientFactory(::RitualBagEntity).setTrackingRange(128).setUpdateInterval(20)
            .setShouldReceiveVelocityUpdates(true).build("ritual_bag_entity")

    @ObjectHolder("celtic_rituals:ritual_bowl")
    var RITUAL_BOWL_TILE: TileEntityType<*>? = null
}
