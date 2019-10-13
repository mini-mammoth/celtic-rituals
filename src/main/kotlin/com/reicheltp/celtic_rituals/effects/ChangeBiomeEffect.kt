package com.reicheltp.celtic_rituals.effects

import com.google.gson.JsonObject
import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.init.ModPacketHandler
import com.reicheltp.celtic_rituals.utils.IMessenger
import com.reicheltp.celtic_rituals.utils.voxelCircle
import net.minecraft.client.Minecraft
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import net.minecraft.world.chunk.Chunk
import net.minecraftforge.fml.network.PacketDistributor
import net.minecraftforge.registries.ForgeRegistries

/**
 * Changes the current biome to [biome] in a [radius] around the center of the ritual.
 */
class ChangeBiomeEffect(
  private val biome: Biome,
  private val radius: Int
) : IEffect {
    /**
     *  Message for [BIOMES_CHANGED_MESSENGER]
     */
    class BiomesChangedMessage(
      val chunkPos: ChunkPos,
      val changedBiomes: List<Int>,
      val targetBiome: Biome
    )

    override fun apply(world: World, pos: BlockPos, inv: IInventory, special: ItemStack) {
        val dirtyChunks = mutableMapOf<Chunk, MutableList<Int>>()

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

            val dirtyBiomes = dirtyChunks.getOrPut(chunk, { mutableListOf() })
            dirtyBiomes.add(idx)
        }

        // As of Protocol 1.14 biome information is not updated beside complete chunk unload/load
        // See: https://wiki.vg/Chunk_Format#Biomes
        //
        // Therefore, we send a custom biome changed info to all clients tracking the related chunks.
        for ((chunk, biomes) in dirtyChunks.entries) {
            ModPacketHandler.CHANNEL.send(
                PacketDistributor.TRACKING_CHUNK.with { chunk },
                BiomesChangedMessage(chunk.pos, biomes, biome)
            )
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

        /**
         * Serializes and handles [BiomesChangedMessage]
         */
        val BIOMES_CHANGED_MESSENGER = object : IMessenger<BiomesChangedMessage> {
            override val messageType: Class<BiomesChangedMessage> = BiomesChangedMessage::class.java

            override fun write(buffer: PacketBuffer, message: BiomesChangedMessage) {
                buffer.writeInt(message.chunkPos.x)
                buffer.writeInt(message.chunkPos.z)
                buffer.writeInt(message.changedBiomes.size)
                message.changedBiomes.forEach { buffer.writeInt(it) }
                buffer.writeResourceLocation(message.targetBiome.registryName!!)
            }

            override fun read(buffer: PacketBuffer): BiomesChangedMessage {
                return BiomesChangedMessage(
                    ChunkPos(buffer.readInt(), buffer.readInt()),
                    (0 until buffer.readInt()).map { buffer.readInt() },
                    buffer.readResourceLocation()
                        .let { ForgeRegistries.BIOMES.getValue(it) }
                        ?: Biomes.PLAINS
                )
            }

            override fun handle(message: BiomesChangedMessage) {
                val world = Minecraft.getInstance().world
                val chunk =
                    world.chunkProvider.getChunk(message.chunkPos.x, message.chunkPos.z, false)
                        ?: return

                for (idx in message.changedBiomes) {
                    chunk.biomes[idx] = message.targetBiome
                }

                chunk.markDirty()
            }
        }
    }
}
