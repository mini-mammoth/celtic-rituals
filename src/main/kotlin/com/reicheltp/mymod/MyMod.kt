package com.reicheltp.mymod

import com.reicheltp.mymod.items.Knife
import net.alexwells.kottle.FMLKotlinModLoadingContext
import net.alexwells.kottle.KotlinEventBusSubscriber
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.InterModComms
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import org.apache.logging.log4j.LogManager
import java.rmi.registry.Registry

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MyMod.MOD_ID)
class MyMod {
    companion object {

        // Directly reference a log4j logger.
        private val LOGGER = LogManager.getLogger()

        const val MOD_ID = "my-mod"
    }

    init {
        // Register the setup method for modloading
        FMLKotlinModLoadingContext.get().modEventBus.addListener<FMLCommonSetupEvent> { this.setup(it) }
        // Register the enqueueIMC method for modloading
        FMLKotlinModLoadingContext.get().modEventBus.addListener<InterModEnqueueEvent> { this.enqueueIMC(it) }
        // Register the processIMC method for modloading
        FMLKotlinModLoadingContext.get().modEventBus.addListener<InterModProcessEvent> { this.processIMC(it) }
        // Register the doClientStuff method for modloading
        FMLKotlinModLoadingContext.get().modEventBus.addListener<FMLClientSetupEvent> { this.doClientStuff(it) }

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this)
    }

    private fun setup(event: FMLCommonSetupEvent) {
        // some preinit code
        LOGGER.info("HELLO FROM KOTLIN")
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.registryName)
    }

    private fun doClientStuff(event: FMLClientSetupEvent) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.minecraftSupplier.get().gameSettings)
    }

    private fun enqueueIMC(event: InterModEnqueueEvent) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("my-mod", "helloworld") {
            LOGGER.info("Hello world from the MDK")
            "Hello world"
        }
    }

    private fun processIMC(event: InterModProcessEvent) {
        // some example code to receive and process InterModComms from other mods
//        LOGGER.info("Got IMC {}", event.imcStream.map { m -> m.getMessageSupplier<Any>().get() }.collect<List<Any>, Any>(Collectors.toList()))
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    fun onServerStarting(event: FMLServerStartingEvent) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting")
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @KotlinEventBusSubscriber(bus = KotlinEventBusSubscriber.Bus.MOD)
    object RegistryEvents {
        @SubscribeEvent
        fun onBlocksRegistry(blockRegistryEvent: RegistryEvent.Register<Block>) {
            // register a new block here
            LOGGER.info("HELLO from Register Block")
        }

        @SubscribeEvent
        fun registerItems(event: RegistryEvent.Register<Item>) {
            // register a new item here
            LOGGER.info("Register items")

            event.registry.registerAll(
                    Knife()
            )
        }
    }
}
