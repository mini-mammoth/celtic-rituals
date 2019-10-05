package com.reicheltp.celtic_rituals.rituals.bag

import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.init.ModBlocks
import com.reicheltp.celtic_rituals.init.ModItems
import com.reicheltp.celtic_rituals.init.ModRecipes
import com.reicheltp.celtic_rituals.rituals.bowl.BowlRitualRecipe
import com.reicheltp.celtic_rituals.rituals.bowl.RitualBowlTile
import java.util.Optional
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.InventoryHelper
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUseContext
import net.minecraft.item.SplashPotionItem
import net.minecraft.nbt.StringNBT
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.stats.Stats
import net.minecraft.util.ActionResult
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import net.minecraft.world.World

class RitualBagItem : SplashPotionItem(Properties().setNoRepair().group(ItemGroup.MISC)) {
    companion object {
        private const val EMPTY_COLOR = 0x70472D

        fun getColor(item: ItemStack): Int {
            // TODO: Pick color for ritual bag based on ritual
            val hasRecipe = item.hasTag() && item.tag?.get("recipe") !== null

            return if (hasRecipe) 0xF800F8 else EMPTY_COLOR
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
            return ActionResultType.FAIL
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

        if (!recipe.isPresent) {
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
}
