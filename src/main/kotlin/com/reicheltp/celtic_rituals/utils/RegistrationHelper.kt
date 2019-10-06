package com.reicheltp.celtic_rituals.utils

import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.init.ModItemGroups
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.item.SpawnEggItem
import net.minecraft.util.ResourceLocation

object RegistrationHelper {
    fun createEntitySpawnEgg(entityType: EntityType<*>,primaryColor: Int, secondaryColor: Int, registryName: String): SpawnEggItem{
        val spawnEggItem = SpawnEggItem(entityType, primaryColor, secondaryColor, Item.Properties().group(ModItemGroups.DEFAULT))
        spawnEggItem.setRegistryName(ResourceLocation(MOD_ID, registryName))

        return spawnEggItem
    }
}
