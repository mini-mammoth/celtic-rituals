package com.reicheltp.celtic_rituals.effects

import com.google.gson.JsonObject
import com.reicheltp.celtic_rituals.MOD_ID
import net.minecraft.inventory.IInventory
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

enum class WeatherConditions {
    CLEAR,
    RAIN,
    THUNDER,
}

/**
 * Use this effect to change the weather
 */
class ChangeWeatherEffect(
  private val condition: WeatherConditions,
  private val duration: Int
) : IEffect {
    override fun apply(world: World, pos: BlockPos, inv: IInventory): Boolean {
        when {
            condition === WeatherConditions.CLEAR -> setClear(world, duration)
            condition === WeatherConditions.RAIN -> setRain(world, duration)
            condition === WeatherConditions.THUNDER -> setThunder(world, duration)
            else -> return false
        }

        return true
    }

    override val serializer: EffectSerializer<*> = SERIALIZER

    companion object {
        val SERIALIZER = object : EffectSerializer<ChangeWeatherEffect>() {
            init {
                registryName = ResourceLocation(MOD_ID, "change_weather")
            }

            override fun read(element: JsonObject): ChangeWeatherEffect {
                return ChangeWeatherEffect(
                    WeatherConditions.valueOf(
                        (element.get("weather")?.asString ?: "clear").toUpperCase()
                    ),
                    element.get("duration")?.asInt ?: 1200
                )
            }

            override fun read(buffer: PacketBuffer): ChangeWeatherEffect {
                return ChangeWeatherEffect(
                    WeatherConditions.valueOf(buffer.readString()),
                    buffer.readInt()
                )
            }

            override fun write(buffer: PacketBuffer, effect: ChangeWeatherEffect) {
                buffer.writeString(effect.condition.toString())
                buffer.writeInt(effect.duration)
            }
        }

        private fun setClear(world: World, time: Int) {
            world.worldInfo.clearWeatherTime = time
            world.worldInfo.rainTime = 0
            world.worldInfo.thunderTime = 0
            world.worldInfo.isRaining = false
            world.worldInfo.isThundering = false
        }

        private fun setRain(world: World, time: Int) {
            world.worldInfo.clearWeatherTime = 0
            world.worldInfo.rainTime = time
            world.worldInfo.thunderTime = time
            world.worldInfo.isRaining = true
            world.worldInfo.isThundering = false
        }

        private fun setThunder(world: World, time: Int) {
            world.worldInfo.clearWeatherTime = 0
            world.worldInfo.rainTime = time
            world.worldInfo.thunderTime = time
            world.worldInfo.isRaining = true
            world.worldInfo.isThundering = true
        }
    }
}
