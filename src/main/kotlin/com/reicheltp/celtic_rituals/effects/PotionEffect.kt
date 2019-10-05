package com.reicheltp.celtic_rituals.effects

import com.google.gson.JsonObject
import com.reicheltp.celtic_rituals.MOD_ID
import net.minecraft.entity.AreaEffectCloudEntity
import net.minecraft.inventory.IInventory
import net.minecraft.network.PacketBuffer
import net.minecraft.potion.Potion
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class PotionEffect(
    private val potion: Potion,
    private val duration: Int,
    private val radius: Float
) : IEffect {
    override fun apply(world: World, pos: BlockPos, inv: IInventory): Boolean {
        val entity = AreaEffectCloudEntity(world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())

        entity.duration = duration
        entity.setPotion(potion)
        entity.radius = radius

        world.addEntity(entity)

        return true
    }

    override val serializer: EffectSerializer<*> = SERIALIZER

    companion object {
        val SERIALIZER = object : EffectSerializer<PotionEffect>() {
            init {
                registryName = ResourceLocation(MOD_ID, "potion")
            }

            override fun read(element: JsonObject): PotionEffect {
                val potion = element.get("potion").asString

                return PotionEffect(
                    Potion.getPotionTypeForName(potion),
                    element.get("duration")?.asInt ?: 60,
                    element.get("radius")?.asFloat ?: 3.0f
                )
            }

            override fun read(buffer: PacketBuffer): PotionEffect {
                return PotionEffect(
                    Potion.getPotionTypeForName(buffer.readString()),
                    buffer.readInt(),
                    buffer.readFloat()
                )
            }

            override fun write(buffer: PacketBuffer, effect: PotionEffect) {
                buffer.writeString(effect.potion.registryName.toString())
                buffer.writeInt(effect.duration)
            }
        }
    }
}
