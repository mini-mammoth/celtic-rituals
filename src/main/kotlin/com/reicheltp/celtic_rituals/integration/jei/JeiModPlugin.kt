package com.reicheltp.celtic_rituals.integration.jei

import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.init.ModItems
import com.reicheltp.celtic_rituals.init.ModRecipes
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

@JeiPlugin
class JeiModPlugin : IModPlugin {
    override fun getPluginUid(): ResourceLocation {
        return ResourceLocation(MOD_ID, "default")
    }

    /**
     * Associate JEI recipe category with a gui.
     */
    override fun registerCategories(registry: IRecipeCategoryRegistration) {
        val guiHelper = registry.jeiHelpers.guiHelper
        registry.addRecipeCategories(RitualRecipeCategory(guiHelper))
    }

    /**
     * Specify how sub items should be handled.
     */
    override fun registerItemSubtypes(registration: ISubtypeRegistration) {
        // Trust the ritual bag. Same name == Same item
        registration.registerSubtypeInterpreter(ModItems.RITUAL_BAG!!) { stack ->
            ModItems.RITUAL_BAG!!.getTranslationKey(
                stack
            )
        }
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        registration.addRecipeCatalyst(ItemStack(ModItems.RITUAL_BOWL!!), RitualRecipeCategory.ID)
    }

    /**
     * Associate items with a JEI recipe category
     */
    override fun registerRecipes(registration: IRecipeRegistration) {
        registration.addRecipes(ModRecipes.bowlRitualRecipes, RitualRecipeCategory.ID)
    }
}
