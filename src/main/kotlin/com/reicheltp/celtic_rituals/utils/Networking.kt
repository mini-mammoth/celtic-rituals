package com.reicheltp.celtic_rituals.utils

import java.util.function.Supplier
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkEvent
import net.minecraftforge.fml.network.simple.SimpleChannel
import org.apache.logging.log4j.LogManager

/**
 * Generic interface for messenger classes.
 */
interface IMessenger<T> {
    companion object {
        val LOGGER = LogManager.getLogger()
    }

    val messageType: Class<T>

    /**
     * Write your [message] to a [buffer].
     */
    fun write(buffer: PacketBuffer, message: T)

    /**
     * Read your message from [buffer]
     */
    fun read(buffer: PacketBuffer): T

    /**
     * Called when a new [message] is received.
     */
    fun received(message: T, ctx: Supplier<NetworkEvent.Context>?) {
        ctx?.get()?.enqueueWork {
            try {
                handle(message)
            } catch (err: Error) {
                LOGGER.error(err)
            }
        }

        ctx?.get()?.packetHandled = true
    }

    /**
     * Threadsafe variant of [received]. Errors will be catched.
     */
    fun handle(message: T) {
    }
}

/**
 * Registers a message to [SimpleChannel] which is wrapped through [IMessenger]
 */
fun <T> SimpleChannel.register(id: Int, messenger: IMessenger<T>) {
    this.registerMessage<T>(
        id,
        messenger.messageType,
        { message, buffer -> messenger.write(buffer, message) },
        { buffer -> messenger.read(buffer) },
        { message, ctx -> messenger.received(message, ctx) }
    )
}
