package com.reicheltp.celtic_rituals.proxy

import com.reicheltp.celtic_rituals.items.Knife
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Register stuff we need on client and server side.
 *
 * @see ClientProxy for client-side only registrations.
 * @See ServerProxy for server-side only registrations.
 */
abstract class CommonProxy {
    @SubscribeEvent
    fun onBlocksRegistry(blockRegistryEvent: RegistryEvent.Register<Block>) {
    }

    @SubscribeEvent
    fun registerItems(event: RegistryEvent.Register<Item>) {
        event.registry.registerAll(
                Knife()
        )
    }
}