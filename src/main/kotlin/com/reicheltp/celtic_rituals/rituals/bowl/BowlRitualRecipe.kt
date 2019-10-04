package com.reicheltp.celtic_rituals.rituals.bowl

import com.google.gson.JsonObject
import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.init.ModRecipes
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.item.crafting.IRecipeType
import net.minecraft.item.crafting.Ingredient
import net.minecraft.network.PacketBuffer
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.common.crafting.CraftingHelper
import net.minecraftforge.registries.ForgeRegistryEntry

class BowlRitualRecipe(
    private val id: ResourceLocation,
    val ingredients: List<Ingredient>,
    val result: ItemStack,
    /**
     * Duration in ticks, this recipe has to burn. Defaults to 60 (3s)
     */
    val duration: Int
) : IRecipe<IInventory> {
    companion object {
        const val DEFAULT_DURATION = 60
    }

    override fun canFit(width: Int, height: Int): Boolean = false

    override fun getId(): ResourceLocation = id

    override fun getType(): IRecipeType<*> {
        return ModRecipes.BOWL_RITUAL_TYPE!!
    }

    override fun getRecipeOutput(): ItemStack = result

    override fun getSerializer(): IRecipeSerializer<*> = ModRecipes.BOWL_RITUAL!!

    override fun getCraftingResult(inv: IInventory): ItemStack = result.copy()

    override fun matches(inv: IInventory, worldIn: World): Boolean {
        val matches = NonNullList.withSize(ingredients.size, false)

        for (i in 0 until inv.sizeInventory) {
            val item = inv.getStackInSlot(i)

            for (k in ingredients.indices) {
                if (ingredients[k].test(item)) {
                    matches[k] = true
                    break
                }
            }
        }

        return matches.all { it }
    }

    class Serializer : ForgeRegistryEntry<IRecipeSerializer<*>>(), IRecipeSerializer<BowlRitualRecipe> {
        init {
            registryName = ResourceLocation(MOD_ID, "bowl_ritual")
        }

        override fun read(recipeId: ResourceLocation, json: JsonObject): BowlRitualRecipe {
            val ingredients = json.getAsJsonArray("ingredients").map { Ingredient.deserialize(it) }
            val result = CraftingHelper.getItemStack(json.getAsJsonObject("result"), true)

            val duration = json.get("duration")?.asInt ?: DEFAULT_DURATION

            return BowlRitualRecipe(recipeId, ingredients, result, duration)
        }

        override fun write(buffer: PacketBuffer, recipe: BowlRitualRecipe) {
            buffer.writeInt(recipe.ingredients.size)
            recipe.ingredients.forEach { it.write(buffer) }

            buffer.writeItemStack(recipe.result)
            buffer.writeInt(recipe.duration)
        }

        override fun read(recipeId: ResourceLocation, buffer: PacketBuffer): BowlRitualRecipe? {
            val size = buffer.readInt()
            val ingredients = NonNullList.withSize(size, Ingredient.EMPTY)
            for (i in 0 until size) {
                ingredients[i] = Ingredient.read(buffer)
            }

            val result = buffer.readItemStack()
            val duration = buffer.readInt()

            return BowlRitualRecipe(recipeId, ingredients, result, duration)
        }
    }
}
