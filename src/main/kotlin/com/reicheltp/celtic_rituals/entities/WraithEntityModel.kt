package com.reicheltp.celtic_rituals.entities

import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.renderer.entity.model.EntityModel
import net.minecraft.client.renderer.entity.model.RendererModel
import net.minecraft.client.renderer.model.ModelBox

class WraithEntityModel : EntityModel<WraithEntity>() {
    private val head: RendererModel
    private val body: RendererModel
    private val leftArm: RendererModel
    private val rightArm: RendererModel
    private val leftLeg: RendererModel
    private val rightLegs: RendererModel

    init {
        textureWidth = 16
        textureHeight = 16

        head = RendererModel(this)
        head.setRotationPoint(0.0f, 13.0f, 0.5f)
        head.cubeList.add(ModelBox(head, 0, 0, -1.0f, -2.0f, -0.5f, 2, 2, 1, 0.0f, false))

        body = RendererModel(this)
        body.setRotationPoint(0.0f, 24.0f, 0.0f)
        body.cubeList.add(ModelBox(body, 0, 0, -2.0f, -11.0f, 0.0f, 4, 6, 1, 0.0f, false))

        leftArm = RendererModel(this)
        leftArm.setRotationPoint(0.0f, 24.0f, 0.0f)
        leftArm.cubeList.add(ModelBox(leftArm, 0, 0, 2.0f, -11.0f, 0.0f, 1, 5, 1, 0.0f, false))

        rightArm = RendererModel(this)
        rightArm.setRotationPoint(0.0f, 24.0f, 0.0f)
        rightArm.cubeList.add(ModelBox(rightArm, 0, 0, -3.0f, -11.0f, 0.0f, 1, 5, 1, 0.0f, false))

        leftLeg = RendererModel(this)
        leftLeg.setRotationPoint(0.0f, 24.0f, 0.0f)
        leftLeg.cubeList.add(ModelBox(leftLeg, 0, 0, 1.0f, -5.0f, 0.0f, 1, 5, 1, 0.0f, false))

        rightLegs = RendererModel(this)
        rightLegs.setRotationPoint(0.0f, 24.0f, 0.0f)
        rightLegs.cubeList.add(ModelBox(rightLegs, 0, 0, -2.0f, -5.0f, 0.0f, 1, 5, 1, 0.0f, false))
    }

    override fun render(
      entityIn: WraithEntity,
      limbSwing: Float,
      limbSwingAmount: Float,
      ageInTicks: Float,
      netHeadYaw: Float,
      headPitch: Float,
      scale: Float
    ) {
        this.setRotationAngles(
            entityIn,
            limbSwing,
            limbSwingAmount,
            ageInTicks,
            netHeadYaw,
            headPitch,
            scale
        )
        if (this.isChild) {
            GlStateManager.pushMatrix()
            head.render(scale)
            body.render(scale)
            leftArm.render(scale)
            rightArm.render(scale)
            leftLeg.render(scale)
            rightLegs.render(scale)
            GlStateManager.popMatrix()
        }
        else
        {
            head.render(scale)
            body.render(scale)
            leftArm.render(scale)
            rightArm.render(scale)
            leftLeg.render(scale)
            rightLegs.render(scale)
        }
    }

    override fun setRotationAngles(
      entityIn: WraithEntity,
      limbSwing: Float,
      limbSwingAmount: Float,
      ageInTicks: Float,
      netHeadYaw: Float,
      headPitch: Float,
      scaleFactor: Float
    ) {
        this.head.rotateAngleX = headPitch * (Math.PI.toFloat() / 180f)
        this.head.rotateAngleY = netHeadYaw * (Math.PI.toFloat() / 180f)
    }
}
