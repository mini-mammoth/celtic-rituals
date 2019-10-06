package com.reicheltp.celtic_rituals.integration.jei

import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.init.ModItems
import com.reicheltp.celtic_rituals.rituals.bag.RitualBagItem
import com.reicheltp.celtic_rituals.rituals.bowl.BowlRitualRecipe
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.client.resources.I18n
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

class RitualRecipeCategory(guiHelper: IGuiHelper) :
    IRecipeCategory<BowlRitualRecipe> {
    companion object {
        val ID = ResourceLocation(MOD_ID, "bowl_ritual")
    }

    private val ritualIcon = guiHelper.createDrawableIngredient(ItemStack(ModItems.RITUAL_BOWL!!))
    private val bg = guiHelper.createBlankDrawable(100, 100)

    override fun getUid(): ResourceLocation = ID

    override fun getRecipeClass(): Class<out BowlRitualRecipe> = BowlRitualRecipe::class.java

    /**
     * Icon shown in the JEI tab
     */
    override fun getIcon(): IDrawable = ritualIcon

    /**
     * Layout ingredients and output on screen.
     */
    override fun setRecipe(layout: IRecipeLayout, recipe: BowlRitualRecipe, ing: IIngredients) {
        layout.itemStacks.init(0, true, 0, 0)
        layout.itemStacks.init(1, true, 20, 0)
        layout.itemStacks.init(2, true, 40, 0)
        layout.itemStacks.init(3, true, 60, 0)
        layout.itemStacks.init(4, true, 80, 0)
        layout.itemStacks.init(10, false, 40, 40)

        layout.itemStacks.set(ing)
    }

    /**
     * Returns the bg of this entry. keep this as compact as possible
     */
    override fun getBackground(): IDrawable = bg

    /**
     * Pass ingredients and output from recipe to JEI
     */
    override fun setIngredients(recipe: BowlRitualRecipe, ingredients: IIngredients) {
        ingredients.setInputIngredients(recipe.ingredients)

        val bag = ItemStack(ModItems.RITUAL_BAG!!)
        RitualBagItem.setRecipe(bag, recipe)

        ingredients.setOutput(VanillaTypes.ITEM, bag)
    }

    /**
     * Title shown in JEI tab
     */
    override fun getTitle(): String {
        return I18n.format("jei.recipes.bowl_ritual.title")
    }
}
