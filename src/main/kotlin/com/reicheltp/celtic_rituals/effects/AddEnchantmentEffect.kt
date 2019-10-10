package com.reicheltp.celtic_rituals.effects

import com.google.gson.JsonObject
import com.reicheltp.celtic_rituals.MOD_ID
import net.minecraft.client.util.JSONException
import net.minecraft.enchantment.Enchantment
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.registries.ForgeRegistries

class AddEnchantmentEffect(
  val enchantment: Enchantment,
  val lvl: Int
) : IEffect {
    override fun apply(world: World, pos: BlockPos, inv: IInventory, special: ItemStack) {
        if (!enchantment.canApply(special)) {
            return
        }

        special.addEnchantment(enchantment, lvl)
    }

    override val serializer: EffectSerializer<*> = SERIALIZER

    companion object {
        val SERIALIZER = object : EffectSerializer<AddEnchantmentEffect>() {
            init {
                registryName = ResourceLocation(MOD_ID, "add_enchantment")
            }

            override fun read(element: JsonObject): AddEnchantmentEffect {
                val enchantment = ResourceLocation(element.get("enchantment").asString)

                return AddEnchantmentEffect(
                    ForgeRegistries.ENCHANTMENTS.getValue(enchantment)
                        ?: throw JSONException("Enchantment $enchantment not found"),
                    element.get("lvl")?.asInt ?: 1
                )
            }

            override fun read(buffer: PacketBuffer): AddEnchantmentEffect {
                return AddEnchantmentEffect(
                    ForgeRegistries.ENCHANTMENTS.getValue(buffer.readResourceLocation())!!,
                    buffer.readVarInt()
                )
            }

            override fun write(buffer: PacketBuffer, effect: AddEnchantmentEffect) {
                buffer.writeResourceLocation(effect.enchantment.registryName)
                buffer.writeVarInt(effect.lvl)
            }
        }
    }
}
