package com.reicheltp.celtic_rituals.items

import com.reicheltp.celtic_rituals.MOD_ID
import net.minecraft.item.ItemGroup
import net.minecraft.item.PotionItem
import net.minecraft.util.ResourceLocation
import org.apache.logging.log4j.LogManager

class RitualBagItem: PotionItem(Properties().setNoRepair().group(ItemGroup.MISC)) {
    companion object {
        private val LOGGER = LogManager.getLogger()
    }

    init {
        registryName = ResourceLocation(MOD_ID, "ritual_bag")
    }
}