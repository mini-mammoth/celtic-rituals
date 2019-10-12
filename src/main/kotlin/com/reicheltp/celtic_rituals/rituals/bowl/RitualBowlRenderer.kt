package com.reicheltp.celtic_rituals.rituals.bowl

import com.mojang.blaze3d.platform.GlStateManager
import kotlin.math.cos
import kotlin.math.sin
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.model.ItemCameraTransforms
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/**
 * Renders containing items on top of the RitualBowlBlock.
 */
@OnlyIn(Dist.CLIENT)
class RitualBowlRenderer : TileEntityRenderer<RitualBowlTile>() {
    override fun render(
      tile: RitualBowlTile,
      x: Double,
      y: Double,
      z: Double,
      partialTicks: Float,
      destroyStage: Int
    ) {
        val renderer = Minecraft.getInstance().itemRenderer

        GlStateManager.pushMatrix()

        GlStateManager.translated(x, y, z)
        GlStateManager.translated(.5, .5, .5)
        GlStateManager.rotatef((partialTicks + this.world.gameTime) * 1.2F % 360F, 0f, 1f, 0f)

        renderer.renderItem(tile.specialItem, ItemCameraTransforms.TransformType.GROUND)

        GlStateManager.popMatrix()

        if (tile.blockState.get(BlockStateProperties.ENABLED)) {
            // No need to render items when bowl is ignited
            return
        }

        val size = tile.sizeInventory
        val step = (Math.PI * 2.0) / size.toDouble()

        for (i in 0 until size) {
            val stack = tile.getStackInSlot(i)

            if (stack.isEmpty) {
                continue
            }

            GlStateManager.pushMatrix()

            GlStateManager.translated(x, y, z)
            GlStateManager.translated(.5, .5, .5)
            GlStateManager.rotated(90.0, 1.0, 0.0, 0.0)
            GlStateManager.scaled(.3, .3, .3)

            GlStateManager.translated(sin(step * i) * 0.8, cos(step * i) * 0.8, 0.5)

            renderer.renderItem(stack, ItemCameraTransforms.TransformType.FIXED)

            GlStateManager.popMatrix()
        }
    }
}
