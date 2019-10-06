package com.reicheltp.celtic_rituals.effects

import com.google.gson.JsonObject
import com.reicheltp.celtic_rituals.MOD_ID
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.crafting.CraftingHelper

class CraftItemEffect(
  private val item: ItemStack
) : IEffect {
    override val serializer: EffectSerializer<*> = SERIALIZER

    override fun apply(world: World, pos: BlockPos, inv: IInventory, special: ItemStack): Boolean {
        for (i in 0 until inv.sizeInventory) {
            if (!inv.getStackInSlot(i).isEmpty) {
                continue
            }

            inv.setInventorySlotContents(i, item)
            return true
        }

        return false
    }

    companion object {
        val SERIALIZER = object : EffectSerializer<CraftItemEffect>() {
            init {
                registryName = ResourceLocation(MOD_ID, "craft_item")
            }

            override fun read(element: JsonObject): CraftItemEffect {
                return CraftItemEffect(
                    CraftingHelper.getItemStack(element, true)
                )
            }

            override fun read(buffer: PacketBuffer): CraftItemEffect {
                return CraftItemEffect(
                    buffer.readItemStack()
                )
            }

            override fun write(buffer: PacketBuffer, effect: CraftItemEffect) {
                buffer.writeItemStack(effect.item)
            }
        }
    }
}
