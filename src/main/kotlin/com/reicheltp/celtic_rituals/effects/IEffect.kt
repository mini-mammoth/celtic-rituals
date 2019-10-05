package com.reicheltp.celtic_rituals.effects

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import net.minecraft.inventory.IInventory
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.registries.ForgeRegistryEntry
import net.minecraftforge.registries.IForgeRegistry

abstract class EffectSerializer<T : IEffect> : ForgeRegistryEntry<EffectSerializer<*>>() {
    companion object {
        var REGISTRY: IForgeRegistry<EffectSerializer<*>>? = null

        fun deserialize(element: JsonObject): IEffect {
            val key = ResourceLocation(element.get("effect").asString)
            val serializer = REGISTRY!!.getValue(key)
                ?: throw JsonParseException("No effect serializer found for $key")

            return serializer.read(element)
        }
    }

    abstract fun read(element: JsonObject): T

    abstract fun read(buffer: PacketBuffer): T

    abstract fun write(buffer: PacketBuffer, effect: T)
}

interface IEffect {
    /**
     * Applies the effect to the [world] at given [pos].
     *
     * Returns true, if effect was successful.
     */
    fun apply(world: World, pos: BlockPos, inv: IInventory): Boolean

    /**
     * Returns an instance of the belonging serializer.
     */
    val serializer: EffectSerializer<*>
}

fun PacketBuffer.readEffect(): IEffect {
    val name = this.readResourceLocation()
    val serializer = EffectSerializer.REGISTRY!!.getValue(name)

    return serializer!!.read(this)
}

fun PacketBuffer.readEffectList(): List<IEffect> {
    val size = this.readInt()

    return (0..size).map { this.readEffect() }
}

fun PacketBuffer.writeEffect(effect: IEffect) {
    val serializer = (effect.serializer) as EffectSerializer<IEffect>

    this.writeResourceLocation(serializer.registryName!!)
    serializer.write(this, effect)
}

fun <T : IEffect> PacketBuffer.writeEffectList(effects: List<T>) {
    this.writeInt(effects.size)
    effects.forEach { this.writeEffect(it) }
}
