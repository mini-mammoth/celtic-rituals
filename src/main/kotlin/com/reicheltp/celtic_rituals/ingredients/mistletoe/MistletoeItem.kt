package com.reicheltp.celtic_rituals.ingredients.mistletoe

import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.init.ModBlocks
import com.reicheltp.celtic_rituals.init.ModItemGroups
import net.minecraft.block.LeavesBlock
import net.minecraft.item.Item
import net.minecraft.item.ItemUseContext
import net.minecraft.item.Rarity
import net.minecraft.tags.BlockTags
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundCategory

/**
 * Mistletoe is dropped when breaking a [MistletoeLeavesBlock].
 *
 * A mistletoe is a common ingredient for rituals.
 * You can use a mistletoe to infect leaves.
 */
class MistletoeItem : Item(
    Properties()
        .rarity(Rarity.COMMON)
        .group(ModItemGroups.DEFAULT)
) {
    init {
        registryName = ResourceLocation(MOD_ID, "mistletoe")
    }

    override fun onItemUse(context: ItemUseContext): ActionResultType {
        val state = context.world.getBlockState(context.pos)
        val targetIsLeave = state.isIn(BlockTags.LEAVES)

        if (context.hand == Hand.MAIN_HAND && targetIsLeave) {
            val newState = ModBlocks.MISTLETOE_LEAVES!!
                .defaultState
                .with(LeavesBlock.DISTANCE, state.get(LeavesBlock.DISTANCE))

            context.world.setBlockState(context.pos, newState)
            context.item.shrink(1)

            val soundType = ModBlocks.MISTLETOE_LEAVES!!.getSoundType(newState)
            context.world.playSound(
                null,
                context.pos,
                soundType.placeSound,
                SoundCategory.BLOCKS,
                soundType.volume,
                soundType.pitch
            )

            return ActionResultType.SUCCESS
        }

        return super.onItemUse(context)
    }
}
