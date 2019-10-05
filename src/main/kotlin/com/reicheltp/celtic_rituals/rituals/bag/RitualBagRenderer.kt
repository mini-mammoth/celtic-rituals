package com.reicheltp.celtic_rituals.rituals.bag

import com.reicheltp.celtic_rituals.MOD_ID
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.SpriteRenderer
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.client.registry.IRenderFactory
import org.apache.logging.log4j.LogManager

class RitualBagRenderer : SpriteRenderer<RitualBagEntity> {
    companion object {
        private val LOGGER = LogManager.getLogger()
    }

    constructor(rendererManager: EntityRendererManager) : super(rendererManager, Minecraft.getInstance().itemRenderer)

    override fun getEntityTexture(entity: Entity): ResourceLocation? {
        return ResourceLocation(MOD_ID, "textures/item/ritual_bag.png")
    }

    class RitualBagRenderFactory : IRenderFactory<RitualBagEntity> {
        override fun createRenderFor(manager: EntityRendererManager?): EntityRenderer<in RitualBagEntity>? {
            try {
                return RitualBagRenderer(manager!!)
            } catch (e: Exception) {
                LOGGER.error("{}: Could not create renderer for manager due to: {}", this.javaClass.name, e.message)
                return null
            }
        }
    }
}
