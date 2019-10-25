package com.reicheltp.celtic_rituals.init

import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.entities.kobold.KoboldEntity
import com.reicheltp.celtic_rituals.entities.wraith.WraithEntity
import com.reicheltp.celtic_rituals.rituals.bag.RitualBagEntity
import com.reicheltp.celtic_rituals.utils.RegistrationHelper
import net.minecraft.entity.EntityClassification
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.registries.ObjectHolder

object ModEntities {
    @ObjectHolder("celtic_rituals:ritual_bag_entity")
    var RITUAL_BAG_ENTITY: EntityType<RitualBagEntity>? = null

    @ObjectHolder("celtic_rituals:wraith_entity")
    var WRAITH_ENTITY: EntityType<*>? = EntityType.Builder.create(WraithEntity.WraithFactory(), EntityClassification.MONSTER)
        .setShouldReceiveVelocityUpdates(true).build("wraith_entity")
        .setRegistryName(
            MOD_ID,
            "wraith_entity"
        )

    @ObjectHolder("celtic_rituals:kobold_entity")
    var KOBOLD_ENTITY: EntityType<*>? = EntityType.Builder.create(KoboldEntity.KoboldFactory(), EntityClassification.MONSTER)
        .setShouldReceiveVelocityUpdates(true).build("kobold_entity")
        .setRegistryName(
            MOD_ID,
            "kobold_entity"
        )

    fun registerEntitySpawnEggs(event: RegistryEvent.Register<Item>){
        event.registry.registerAll(
            RegistrationHelper.createEntitySpawnEgg(WRAITH_ENTITY!!, 0x000000, 0xFFFFFF, "wraith_spawnegg"),
            RegistrationHelper.createEntitySpawnEgg(KOBOLD_ENTITY!!, 0x000000, 0xFFFFFF, "kobold_spawnegg")
        )
    }

    fun registerEntityWorldSpawns()
    {
        registerEntityWorldSpawn(WRAITH_ENTITY!!, EntityClassification.MONSTER, biomes = *arrayOf(Biomes.FOREST, Biomes.BIRCH_FOREST, Biomes.DARK_FOREST, Biomes.FLOWER_FOREST, Biomes.BIRCH_FOREST_HILLS, Biomes.DARK_FOREST_HILLS))
        registerEntityWorldSpawn(KOBOLD_ENTITY!!, EntityClassification.MONSTER, biomes = *arrayOf(Biomes.FOREST, Biomes.BIRCH_FOREST, Biomes.DARK_FOREST, Biomes.FLOWER_FOREST, Biomes.BIRCH_FOREST_HILLS, Biomes.DARK_FOREST_HILLS))
    }

    fun registerEntityWorldSpawn(entityType: EntityType<*>, classification: EntityClassification, weight: Int = 10, minGroupCount: Int = 1, maxGroupCount: Int = 4, vararg biomes: Biome){
        for (biome in biomes)
        {
            if (biome == null)
                continue

            biome.getSpawns(classification).add(Biome.SpawnListEntry(entityType, weight,minGroupCount,maxGroupCount))
        }
    }
}
