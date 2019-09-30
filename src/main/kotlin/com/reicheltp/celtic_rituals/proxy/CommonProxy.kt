package com.reicheltp.celtic_rituals.proxy

import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.init.ModBlocks
import com.reicheltp.celtic_rituals.items.Knife
import com.reicheltp.celtic_rituals.rituals.bowl.RitualBowlBlock
import com.reicheltp.celtic_rituals.rituals.bowl.RitualBowlTile
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import java.util.function.Supplier


/**
 * Register stuff we need on client and server side.
 *
 * @see ClientProxy for client-side only registrations.
 * @See ServerProxy for server-side only registrations.
 */
abstract class CommonProxy {
    @SubscribeEvent
    fun onBlocksRegistry(event: RegistryEvent.Register<Block>) {
        event.registry.registerAll(
                RitualBowlBlock()
        )
    }

    @SubscribeEvent
    fun onTileEntityRegistry(event: RegistryEvent.Register<TileEntityType<*>>) {
        event.registry.register(
                TileEntityType.Builder.create(Supplier { RitualBowlTile() }, ModBlocks.RITUAL_BOWL).build(null).setRegistryName(ResourceLocation(MOD_ID, "ritual_bowl"))
        )
    }


    @SubscribeEvent
    fun registerItems(event: RegistryEvent.Register<Item>) {
        event.registry.registerAll(
                Knife(),
                BlockItem(ModBlocks.RITUAL_BOWL!!, Item.Properties().maxStackSize(1)).setRegistryName(ResourceLocation(MOD_ID, "ritual_bowl"))
        )
    }
}