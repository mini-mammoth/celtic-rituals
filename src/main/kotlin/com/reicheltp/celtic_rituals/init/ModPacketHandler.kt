package com.reicheltp.celtic_rituals.init

import com.reicheltp.celtic_rituals.MOD_ID
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.network.NetworkRegistry

const val PROTOCOL_VERSION = "1"

/**
 * Default usage of [net.minecraftforge.fml.network.simple.SimpleChannel]
 *
 * All used messages have to be registered during start in [com.reicheltp.celtic_rituals.proxy.CommonProxy]
 *
 * See: https://mcforge.readthedocs.io/en/1.13.x/networking/simpleimpl/
 */
object ModPacketHandler {
    val CHANNEL = NetworkRegistry.newSimpleChannel(
        ResourceLocation(MOD_ID, "main"),
        { PROTOCOL_VERSION },
        { PROTOCOL_VERSION == it },
        { PROTOCOL_VERSION == it }
    )
}
