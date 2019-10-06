package com.reicheltp.celtic_rituals.rituals.sacrifice

import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.init.ModItemGroups
import com.reicheltp.celtic_rituals.init.ModItems
import com.reicheltp.celtic_rituals.init.ModSoundEvents
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityClassification
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Rarity
import net.minecraft.item.SpawnEggItem
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.registries.ForgeRegistries

class HeartItem : Item(
    Properties()
        .group(ModItemGroups.DEFAULT)
        .rarity(Rarity.UNCOMMON)
        .maxStackSize(4)
) {
    companion object {
        @OnlyIn(Dist.CLIENT)
        fun getColor(item: ItemStack, layer: Int): Int {
            if (layer != 0) {
                return -1
            }

            val name = ResourceLocation(item.orCreateTag.getString("Entity"))
            val eggName = ResourceLocation(name.namespace, name.path + "_spawn_egg")
            val egg = ForgeRegistries.ITEMS.getValue(eggName)
            if (egg is SpawnEggItem) {
                return egg.getColor(0)
            }

            return 0xFF0000
        }

        fun from(type: EntityType<*>): ItemStack {
            val heart = ItemStack(ModItems.HEART!!)
            heart.orCreateTag.putString("Entity", type.registryName.toString())
            return heart
        }
    }

    init {
        registryName = ResourceLocation(MOD_ID, "heart")
    }

    /**
     * Used to add tooltips
     */
    override fun addInformation(
      stack: ItemStack,
      worldIn: World?,
      tooltip: MutableList<ITextComponent>,
      flagIn: ITooltipFlag
    ) {
        val name = ResourceLocation(stack.orCreateTag.getString("Entity"))
        val entityType = ForgeRegistries.ENTITIES.getValue(name)

        if (entityType != null) {
            tooltip.add(TranslationTextComponent(entityType.translationKey))
        }
    }

    override fun fillItemGroup(group: ItemGroup, items: NonNullList<ItemStack>) {
        if (!isInGroup(group)) {
            return
        }

        for (entity in ForgeRegistries.ENTITIES.values) {
            if (entity.classification === EntityClassification.MISC) {
                // Skip all misc entities as there is nothing like a heart of a minecart or
                // a heart of a lighting bold.
                continue
            }

            items.add(from(entity))
        }
    }

    override fun inventoryTick(
      stack: ItemStack,
      worldIn: World,
      entityIn: Entity,
      itemSlot: Int,
      isSelected: Boolean
    ) {
        if (!isSelected) {
            return
        }

        if (worldIn.gameTime.toInt() % 50 == 0) {
            entityIn.playSound(ModSoundEvents.HEARTBEAT_SLOW, 1.0f, 1.0f)
        }
    }
}
