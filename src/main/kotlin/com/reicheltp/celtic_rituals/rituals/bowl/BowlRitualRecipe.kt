package com.reicheltp.celtic_rituals.rituals.bowl

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.effects.CraftItemEffect
import com.reicheltp.celtic_rituals.effects.EffectSerializer
import com.reicheltp.celtic_rituals.effects.IEffect
import com.reicheltp.celtic_rituals.effects.readEffectList
import com.reicheltp.celtic_rituals.effects.writeEffectList
import com.reicheltp.celtic_rituals.init.ModItems
import com.reicheltp.celtic_rituals.init.ModRecipes
import com.reicheltp.celtic_rituals.rituals.bag.RitualBagItem
import com.reicheltp.celtic_rituals.utils.asColor
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.item.crafting.IRecipeType
import net.minecraft.item.crafting.Ingredient
import net.minecraft.network.PacketBuffer
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.crafting.CraftingHelper
import net.minecraftforge.registries.ForgeRegistryEntry

class BowlRitualRecipe(
  private val id: ResourceLocation,
  val ingredients: List<Ingredient>,
  val result: List<IEffect>,
  /**
   * Duration in ticks, this recipe has to burn. Defaults to 60 (3s)
   */
  val duration: Int,
  val color: Int,
  /**
   * If true, you are not allowed to pick this recipe with a ritual bag.
   */
  val preventBagging: Boolean
) : IRecipe<RitualBowlTile> {
    companion object {
        const val DEFAULT_DURATION = 60
    }

    override fun canFit(width: Int, height: Int): Boolean = false

    override fun getId(): ResourceLocation = id

    override fun getType(): IRecipeType<*> {
        return ModRecipes.BOWL_RITUAL_TYPE!!
    }

    override fun getRecipeOutput(): ItemStack {
        val bag = ItemStack(ModItems.RITUAL_BAG!!)
        RitualBagItem.setRecipe(bag, this)

        return bag
    }

    override fun getSerializer(): IRecipeSerializer<*> = ModRecipes.BOWL_RITUAL!!

    override fun getCraftingResult(tile: RitualBowlTile): ItemStack {
        // Remove all ingredients
        tile.clear()

        applyEffects(tile.world!!, tile.pos, tile, tile.specialItem)

        return ItemStack.EMPTY
    }

    fun applyEffects(world: World, pos: BlockPos, inv: IInventory, special: ItemStack) {
        for (r in result) {
            r.apply(world, pos, inv, special)
        }
    }

    override fun matches(inv: RitualBowlTile, worldIn: World): Boolean {
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

    class Serializer : ForgeRegistryEntry<IRecipeSerializer<*>>(),
        IRecipeSerializer<BowlRitualRecipe> {
        init {
            registryName = ResourceLocation(MOD_ID, "bowl_ritual")
        }

        override fun read(recipeId: ResourceLocation, json: JsonObject): BowlRitualRecipe {
            val ingredients = json.getAsJsonArray("ingredients")
                .map { CraftingHelper.getIngredient(it) }
            val result = parseResults(json.get("result"))

            val duration = json.get("duration")?.asInt ?: DEFAULT_DURATION
            val color = json.get("color")?.asColor ?: -1

            val preventBagging = json.get("prevent_bagging")?.asBoolean ?: false

            return BowlRitualRecipe(recipeId, ingredients, result, duration, color, preventBagging)
        }

        override fun write(buffer: PacketBuffer, recipe: BowlRitualRecipe) {
            buffer.writeInt(recipe.ingredients.size)
            recipe.ingredients.forEach { it.write(buffer) }

            buffer.writeEffectList(recipe.result)
            buffer.writeInt(recipe.duration)
            buffer.writeInt(recipe.color)
            buffer.writeBoolean(recipe.preventBagging)
        }

        override fun read(recipeId: ResourceLocation, buffer: PacketBuffer): BowlRitualRecipe? {
            val size = buffer.readInt()
            val ingredients = NonNullList.withSize(size, Ingredient.EMPTY)
            for (i in 0 until size) {
                ingredients[i] = Ingredient.read(buffer)
            }

            val result = buffer.readEffectList()
            val duration = buffer.readInt()
            val color = buffer.readInt()
            val preventBagging = buffer.readBoolean()

            return BowlRitualRecipe(recipeId, ingredients, result, duration, color, preventBagging)
        }

        private fun parseResults(element: JsonElement): List<IEffect> {
            fun parse(element: JsonObject): IEffect {
                if (element.has("effect")) {
                    return EffectSerializer.deserialize(element)
                }

                // A non specified effect should treated as CraftItemEffect
                return CraftItemEffect.SERIALIZER.read(element)
            }

            return when {
                element.isJsonObject -> listOf(parse(element.asJsonObject))
                element.isJsonArray -> element.asJsonArray.map { parse(it.asJsonObject) }
                else -> throw JsonParseException("result has to be array or object")
            }
        }
    }
}
