package com.reicheltp.celtic_rituals.items

import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.init.ModItemGroups
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation

class SigilItem : Item(Properties().group(ModItemGroups.DEFAULT)) {
    init {
        registryName = ResourceLocation(MOD_ID, "sigil")
    }
}
