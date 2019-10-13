package com.reicheltp.celtic_rituals.effects

import com.google.gson.JsonObject
import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.utils.voxelCircle
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import net.minecraftforge.registries.ForgeRegistries

/**
 * Changes the current biome to [biome] in a [radius] around the center of the ritual.
 */
class ChangeBiomeEffect(
  private val biome: Biome,
  private val radius: Int
) : IEffect {
    override fun apply(world: World, pos: BlockPos, inv: IInventory, special: ItemStack) {
        for ((i, j) in voxelCircle(radius)) {
            val x = pos.x + i
            val z = pos.z + j

            val chunk = world.chunkProvider.getChunk(x shr 4, z shr 4, false) ?: continue

            /**
             * Minecraft magic indexing.
             *
             * See [net.minecraft.world.chunk.IChunk.getBiome]
             */
            val idx = (z and 15) shl 4 or (x and 15)

            chunk.biomes[idx] = biome
        }
    }

    override val serializer: EffectSerializer<*> = SERIALIZER

    companion object {
        val SERIALIZER = object : EffectSerializer<ChangeBiomeEffect>() {
            init {
                registryName = ResourceLocation(MOD_ID, "change_biome")
            }

            override fun read(element: JsonObject): ChangeBiomeEffect {

                return ChangeBiomeEffect(
                    element.get("biome")?.asString
                        ?.let { ResourceLocation(it) }
                        ?.let { ForgeRegistries.BIOMES.getValue(it) }
                        ?: Biomes.PLAINS,
                    element.get("radius")?.asInt ?: 5
                )
            }

            override fun read(buffer: PacketBuffer): ChangeBiomeEffect {
                return ChangeBiomeEffect(
                    buffer.readResourceLocation()
                        .let { ForgeRegistries.BIOMES.getValue(it) }
                        ?: Biomes.PLAINS,
                    buffer.readVarInt()
                )
            }

            override fun write(buffer: PacketBuffer, effect: ChangeBiomeEffect) {
                buffer.writeResourceLocation(effect.biome.registryName!!)
                buffer.writeVarInt(effect.radius)
            }
        }
    }
}
