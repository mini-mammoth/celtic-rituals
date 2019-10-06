package com.reicheltp.celtic_rituals.entities

import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.init.ModEntities
import com.reicheltp.celtic_rituals.rituals.bag.RitualBagEntity
import com.reicheltp.celtic_rituals.rituals.bag.RitualBagRenderer
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.LivingRenderer
import net.minecraft.client.renderer.entity.model.EntityModel
import net.minecraft.entity.LivingEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.client.registry.IRenderFactory
import org.apache.logging.log4j.LogManager

class WraithEntityRenderer(manager: EntityRendererManager): LivingRenderer<WraithEntity, WraithEntityModel>(manager, WraithEntityModel(), 0f) {
    companion object {
        private val LOGGER = LogManager.getLogger()
    }

    override fun getEntityTexture(entity: WraithEntity): ResourceLocation? {
        return ResourceLocation(MOD_ID, "textures/entity/wraith.png")
    }

    class WraithEntityRendererFactory : IRenderFactory<WraithEntity> {
        override fun createRenderFor(manager: EntityRendererManager?):
          EntityRenderer<in WraithEntity>? {
            try {
                return WraithEntityRenderer(manager!!)
            } catch (e: Exception) {
                WraithEntityRenderer.LOGGER.error(
                    "{}: Could not create renderer for manager due to: {}",
                    this.javaClass.name,
                    e.message
                )
                return null
            }
        }
    }
}
