package com.reicheltp.celtic_rituals.entities.kobold

import com.reicheltp.celtic_rituals.MOD_ID
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.LivingRenderer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.client.registry.IRenderFactory
import org.apache.logging.log4j.LogManager

class KoboldEntityRenderer(manager: EntityRendererManager): LivingRenderer<KoboldEntity, KoboldEntityModel>(manager,
    KoboldEntityModel(), 0f) {
    companion object {
        private val LOGGER = LogManager.getLogger()
    }

    override fun getEntityTexture(entity: KoboldEntity): ResourceLocation? {
        return ResourceLocation(MOD_ID, "textures/entity/kobold.png")
    }

    class KoboldEntityRendererFactory : IRenderFactory<KoboldEntity> {
        override fun createRenderFor(manager: EntityRendererManager?):
          EntityRenderer<in KoboldEntity>? {
            try {
                return KoboldEntityRenderer(manager!!)
            } catch (e: Exception) {
                LOGGER.error(
                    "{}: Could not create renderer for manager due to: {}",
                    this.javaClass.name,
                    e.message
                )
                return null
            }
        }
    }
}
