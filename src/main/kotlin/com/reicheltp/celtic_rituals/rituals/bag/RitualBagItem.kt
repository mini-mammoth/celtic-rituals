package com.reicheltp.celtic_rituals.rituals.bag

import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.init.ModBlocks
import com.reicheltp.celtic_rituals.init.ModItemGroups
import com.reicheltp.celtic_rituals.init.ModItems
import com.reicheltp.celtic_rituals.init.ModRecipes
import com.reicheltp.celtic_rituals.rituals.bowl.BowlRitualRecipe
import com.reicheltp.celtic_rituals.rituals.bowl.RitualBowlTile
import java.util.Optional
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.InventoryHelper
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUseContext
import net.minecraft.nbt.StringNBT
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.stats.Stats
import net.minecraft.util.ActionResult
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World

class RitualBagItem : Item(Properties().setNoRepair().group(ModItemGroups.DEFAULT)) {
    companion object {
        private const val EMPTY_COLOR = 0x70472D

        /**
         * Picks a tint color from contained recipe.
         */
        fun getColor(item: ItemStack): Int {
            val recipe = getRecipe(item)
            if (!recipe.isPresent) {
                return EMPTY_COLOR
            }

            val color = recipe.get().color
            return if (color > 0) color else 0xF800F8
        }

        fun getRecipe(item: ItemStack): Optional<BowlRitualRecipe> {
            val tag = item.getOrCreateTag()
            val name = ResourceLocation(tag.getString("recipe"))

            val recipe = Minecraft.getInstance().world.recipeManager.getRecipe(name)

            return recipe.map { it as BowlRitualRecipe }
        }

        fun setRecipe(item: ItemStack, recipe: BowlRitualRecipe) {
            item.setTagInfo("recipe", StringNBT(recipe.id.toString()))
        }
    }

    init {
        registryName = ResourceLocation(MOD_ID, "ritual_bag")
    }

    override fun onItemRightClick(
      worldIn: World,
      playerIn: PlayerEntity,
      handIn: Hand
    ): ActionResult<ItemStack> {
        val itemStack = playerIn.getHeldItem(handIn)

        if (!getRecipe(itemStack).isPresent) {
            // You can only throw filled bags
            return ActionResult(ActionResultType.PASS, itemStack)
        }

        worldIn.playSound(
            playerIn,
            playerIn.posX,
            playerIn.posY,
            playerIn.posZ,
            SoundEvents.ENTITY_SPLASH_POTION_THROW,
            SoundCategory.PLAYERS,
            0.5f,
            0.4f / (Item.random.nextFloat() * 0.4f + 0.8f)
        )

        if (!worldIn.isRemote) {
            val ritualBagEntity = RitualBagEntity(worldIn, playerIn)

            ritualBagEntity.setItem(itemStack)
            ritualBagEntity.shoot(
                playerIn,
                playerIn.rotationPitch,
                playerIn.rotationYaw,
                -20.0f,
                0.7f,
                1.0f
            )
            worldIn.addEntity(ritualBagEntity)
        }

        itemStack.shrink(1)

        playerIn.addStat(Stats.ITEM_USED.get(this))
        return ActionResult(ActionResultType.SUCCESS, itemStack)
    }

    /**
     * When right-click a Ritual Bowl, we copy the ritual into the bag.
     */
    override fun onItemUse(context: ItemUseContext): ActionResultType {
        if (getRecipe(context.item).isPresent) {
            // You can only fill empty bags
            return ActionResultType.PASS
        }

        val state = context.world.getBlockState(context.pos)

        if (state.block !== ModBlocks.RITUAL_BOWL) {
            return ActionResultType.PASS
        }

        if (!state.get(BlockStateProperties.ENABLED)) {
            return ActionResultType.FAIL
        }

        val tile = context.world.getTileEntity(context.pos) as RitualBowlTile
        val recipe = context.world.recipeManager.getRecipe(
            ModRecipes.BOWL_RITUAL_TYPE!!,
            tile,
            context.world
        )

        if (!recipe.isPresent || recipe.get().preventBagging) {
            return ActionResultType.FAIL
        }

        context.item.shrink(1)

        val withRitual = ItemStack(ModItems.RITUAL_BAG!!, 1)
        setRecipe(withRitual, recipe.get())

        if (context.player?.inventory?.addItemStackToInventory(withRitual) != true) {
            InventoryHelper.spawnItemStack(
                context.world,
                context.pos.x.toDouble(), context.pos.y.toDouble(), context.pos.z.toDouble(),
                withRitual
            )
        }

        tile.clear()
        context.world.setBlockState(tile.pos, state.with(BlockStateProperties.ENABLED, false))

        return ActionResultType.SUCCESS
    }

    override fun getItemStackLimit(stack: ItemStack?): Int {
        if (stack != null && getRecipe(stack).isPresent) {
            return 4
        }

        return 16
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    override fun getTranslationKey(item: ItemStack): String {
        if (item.tag?.contains("recipe") != true) {
            return this.translationKey
        }

        val name = ResourceLocation(item.tag!!.getString("recipe"))
        return this.translationKey + ".ritual." + name.path
    }

    /**
     * Fill the item group with all subtypes of this item.
     *
     * Required for creative tab and JEI
     */
    override fun fillItemGroup(group: ItemGroup, items: NonNullList<ItemStack>) {
        if (!isInGroup(group)) {
            return
        }

        items.add(ItemStack(this))

        for (recipe in ModRecipes.bowlRitualRecipes) {
            val bag = ItemStack(this)
            setRecipe(bag, recipe)

            if (recipe.preventBagging) {
                bag.orCreateTag.putBoolean("PreventBagging", true)
            }

            items.add(bag)
        }
    }

    override fun addInformation(
      stack: ItemStack,
      worldIn: World?,
      tooltip: MutableList<ITextComponent>,
      flagIn: ITooltipFlag
    ) {
        if (stack.orCreateTag.contains("PreventBagging")) {
            val key = stack.item.translationKey + ".tooltip.prevent_bagging"
            tooltip.add(TranslationTextComponent(key))
        }

        super.addInformation(stack, worldIn, tooltip, flagIn)
    }
}
