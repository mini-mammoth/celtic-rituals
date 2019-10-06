package com.reicheltp.celtic_rituals.init

import com.reicheltp.celtic_rituals.rituals.bowl.BowlRitualRecipe
import net.minecraft.client.Minecraft
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.item.crafting.IRecipeType
import net.minecraftforge.registries.ObjectHolder

object ModRecipes {
    @ObjectHolder("celtic_rituals:bowl_ritual")
    var BOWL_RITUAL: IRecipeSerializer<*>? = null

    var BOWL_RITUAL_TYPE: IRecipeType<BowlRitualRecipe>? = null

    /**
     * All registered bowl ritual recipes.
     *
     * TODO: Keep an eye on the performance and reliability of this.
     */
    val bowlRitualRecipes: Collection<BowlRitualRecipe>
        get() {
            if (Minecraft.getInstance().world?.recipeManager == null) {
                return emptyList()
            }

            val manager = Minecraft.getInstance().world.recipeManager
            return manager.recipes
                .filter { it.type === BOWL_RITUAL_TYPE }
                .map { it as BowlRitualRecipe }
        }
}
