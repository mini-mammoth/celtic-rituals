package com.reicheltp.mymod

import net.alexwells.kottle.FMLKotlinModLoadingContext
import net.alexwells.kottle.KotlinEventBusSubscriber
import net.minecraft.block.Block
import net.minecraft.block.Blocks
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
import java.util.function.Consumer
import java.util.stream.Collectors

// The value here should match an entry in the META-INF/mods.toml file
@Mod("my-mod")
class MyMod {
    init {
        // Register the setup method for modloading
        FMLKotlinModLoadingContext.get().modEventBus.addListener<FMLCommonSetupEvent>(Consumer<FMLCommonSetupEvent> { this.setup(it) })
        // Register the enqueueIMC method for modloading
        FMLKotlinModLoadingContext .get().modEventBus.addListener<InterModEnqueueEvent>(Consumer<InterModEnqueueEvent> { this.enqueueIMC(it) })
        // Register the processIMC method for modloading
        FMLKotlinModLoadingContext .get().modEventBus.addListener<InterModProcessEvent>(Consumer<InterModProcessEvent> { this.processIMC(it) })
        // Register the doClientStuff method for modloading
        FMLKotlinModLoadingContext .get().modEventBus.addListener<FMLClientSetupEvent>(Consumer<FMLClientSetupEvent> { this.doClientStuff(it) })

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
    }

    companion object {

        // Directly reference a log4j logger.
        private val LOGGER = LogManager.getLogger()
    }
}
