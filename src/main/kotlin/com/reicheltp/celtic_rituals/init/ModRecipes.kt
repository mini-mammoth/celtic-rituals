package com.reicheltp.celtic_rituals.init

import com.reicheltp.celtic_rituals.rituals.bowl.BowlRitualRecipe
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.item.crafting.IRecipeType
import net.minecraftforge.registries.ObjectHolder

object ModRecipes {
    @ObjectHolder("celtic_rituals:bowl_ritual")
    var BOWL_RITUAL: IRecipeSerializer<*>? = null

    var BOWL_RITUAL_TYPE: IRecipeType<BowlRitualRecipe>? = null
}
