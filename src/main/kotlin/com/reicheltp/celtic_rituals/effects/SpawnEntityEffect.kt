package com.reicheltp.celtic_rituals.effects

import com.google.gson.JsonObject
import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.utils.Spawn
import net.minecraft.entity.EntityType
import net.minecraft.inventory.IInventory
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class SpawnEntityEffect(
  private val type: EntityType<*>,
  private val minDistance: Int,
  private val maxDistance: Int,
  private val count: Int
) : IEffect {

    override fun apply(world: World, pos: BlockPos, inv: IInventory): Boolean {
        return Spawn.spawnEntityAroundPosition(
            world,
            type,
            null,
            pos,
            minDistance,
            maxDistance,
            count
        )
    }

    override val serializer: EffectSerializer<*> = SERIALIZER

    companion object {
        val SERIALIZER = object : EffectSerializer<SpawnEntityEffect>() {
            init {
                registryName = ResourceLocation(MOD_ID, "spawn_entity")
            }

            override fun read(element: JsonObject): SpawnEntityEffect {
                return SpawnEntityEffect(
                    EntityType.byKey(element.get("entity").asString).orElse(EntityType.CHICKEN),
                    element.get("min_distance")?.asInt ?: 1,
                    element.get("max_distance")?.asInt ?: 3,
                    element.get("count")?.asInt ?: 1
                )
            }

            override fun write(buffer: PacketBuffer, effect: SpawnEntityEffect) {
                buffer.writeResourceLocation(EntityType.getKey(effect.type))

                buffer.writeInt(effect.minDistance)
                buffer.writeInt(effect.maxDistance)
                buffer.writeInt(effect.count)
            }

            override fun read(buffer: PacketBuffer): SpawnEntityEffect {
                return SpawnEntityEffect(
                    EntityType.byKey(buffer.readString()).orElse(EntityType.CHICKEN),
                    buffer.readInt(),
                    buffer.readInt(),
                    buffer.readInt()
                )
            }
        }
    }
}
