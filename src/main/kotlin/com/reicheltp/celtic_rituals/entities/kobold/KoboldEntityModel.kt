package com.reicheltp.celtic_rituals.entities.kobold

import net.minecraft.client.renderer.entity.model.EntityModel
import net.minecraft.client.renderer.entity.model.RendererModel
import net.minecraft.client.renderer.model.ModelBox

class KoboldEntityModel : EntityModel<KoboldEntity>(){
    private var MainBody: RendererModel
    private var Head: RendererModel
    private var UpperHead: RendererModel
    private var Mouth: RendererModel
    private var Teeth: RendererModel
    private var LowerJaw: RendererModel
    private var UpperJaw: RendererModel
    private var Nose: RendererModel
    private var Horns: RendererModel
    private var Neck: RendererModel
    private var Chest: RendererModel
    private var Legs: RendererModel
    private var UpperLegs: RendererModel
    private var LowerLegs: RendererModel
    private var FootClaws: RendererModel
    private var RightClaws: RendererModel
    private var LeftClaws: RendererModel
    private var Feet: RendererModel
    private var Arms: RendererModel
    private var Shoulders: RendererModel
    private var UpperArms: RendererModel
    private var LowerArms: RendererModel
    private var Hands: RendererModel
    private var HandClaws: RendererModel
    private var RightHandClaws: RendererModel
    private var LeftHandClaws: RendererModel
    private var Tail: RendererModel
    private var UpperTail: RendererModel
    private var LowerTail: RendererModel

    init {
        textureWidth = 32
        textureHeight = 32

        MainBody = RendererModel(this)
        MainBody.setRotationPoint(0.0f, 24.0f, 0.0f)

        Head = RendererModel(this)
        Head.setRotationPoint(0.0f, 0.0f, 0.0f)
        MainBody.addChild(Head)

        UpperHead = RendererModel(this)
        UpperHead.setRotationPoint(1.0f, -16.5f, -0.5f)
        setRotationAngle(UpperHead, 0.0f, 0.0f, -0.7854f)
        Head.addChild(UpperHead)
        UpperHead.cubeList.add(
            ModelBox(
                UpperHead,
                12,
                12,
                -1.0f,
                0.0f,
                -1.5f,
                2,
                2,
                3,
                0.0f,
                false
            )
        )

        Mouth = RendererModel(this)
        Mouth.setRotationPoint(0.0f, 0.0f, 0.0f)
        Head.addChild(Mouth)

        Teeth = RendererModel(this)
        Teeth.setRotationPoint(0.0f, 0.0f, 0.0f)
        Mouth.addChild(Teeth)
        Teeth.cubeList.add(ModelBox(Teeth, 13, 10, -2.5f, -15.0f, 0.5f, 3, 1, 0, 0.0f, false))
        Teeth.cubeList.add(ModelBox(Teeth, 9, 4, -2.5f, -15.0f, -1.5f, 3, 1, 0, 0.0f, false))
        Teeth.cubeList.add(ModelBox(Teeth, 0, 0, -2.5f, -15.0f, -1.5f, 0, 1, 2, 0.0f, false))

        LowerJaw = RendererModel(this)
        LowerJaw.setRotationPoint(0.0f, -14.3f, -0.5f)
        setRotationAngle(LowerJaw, 0.0f, 0.0f, -0.0873f)
        Mouth.addChild(LowerJaw)
        LowerJaw.cubeList.add(ModelBox(LowerJaw, 9, 0, -3.0f, -0.5f, -1.5f, 5, 1, 3, 0.0f, false))

        UpperJaw = RendererModel(this)
        UpperJaw.setRotationPoint(0.0f, 0.0f, 0.0f)
        Mouth.addChild(UpperJaw)
        UpperJaw.cubeList.add(ModelBox(UpperJaw, 0, 8, -3.0f, -16.0f, -2.0f, 5, 1, 3, 0.0f, false))

        Nose = RendererModel(this)
        Nose.setRotationPoint(-2.5f, -16.0f, -0.5f)
        setRotationAngle(Nose, -0.7854f, 0.0f, 0.0f)
        Mouth.addChild(Nose)
        Nose.cubeList.add(ModelBox(Nose, 0, 0, -0.3f, -0.5f, -0.5f, 1, 1, 1, 0.0f, false))

        Horns = RendererModel(this)
        Horns.setRotationPoint(1.5f, -18.0f, 0.0f)
        setRotationAngle(Horns, 0.0f, 0.0f, 0.7854f)
        Head.addChild(Horns)
        Horns.cubeList.add(ModelBox(Horns, 9, 0, 0.9f, -2.0f, 0.5f, 1, 3, 0, 0.0f, false))
        Horns.cubeList.add(ModelBox(Horns, 9, 0, 1.0f, -2.0f, -1.5f, 1, 3, 0, 0.0f, false))

        Neck = RendererModel(this)
        Neck.setRotationPoint(0.0f, -13.0f, -0.5f)
        setRotationAngle(Neck, 0.0f, 0.0f, -0.0873f)
        MainBody.addChild(Neck)
        Neck.cubeList.add(ModelBox(Neck, 24, 24, -0.35f, -0.9f, -1.0f, 2, 2, 2, 0.0f, false))

        Chest = RendererModel(this)
        Chest.setRotationPoint(1.0f, -9.0f, -0.5f)
        setRotationAngle(Chest, 0.0f, 0.0f, -0.0873f)
        MainBody.addChild(Chest)
        Chest.cubeList.add(ModelBox(Chest, 0, 0, -1.0f, -3.0f, -2.5f, 2, 3, 5, 0.0f, false))
        Chest.cubeList.add(ModelBox(Chest, 0, 12, -1.0f, 0.0f, -2.0f, 2, 3, 4, 0.0f, false))

        Legs = RendererModel(this)
        Legs.setRotationPoint(0.0f, 0.0f, 0.0f)
        MainBody.addChild(Legs)

        UpperLegs = RendererModel(this)
        UpperLegs.setRotationPoint(1.0f, -4.0f, -0.5f)
        setRotationAngle(UpperLegs, 0.0f, 0.0f, 0.3927f)
        Legs.addChild(UpperLegs)
        UpperLegs.cubeList.add(
            ModelBox(
                UpperLegs,
                18,
                19,
                -1.75f,
                -2.58f,
                0.5f,
                2,
                4,
                2,
                0.0f,
                false
            )
        )
        UpperLegs.cubeList.add(
            ModelBox(
                UpperLegs,
                18,
                19,
                -1.75f,
                -2.58f,
                -2.5f,
                2,
                4,
                2,
                0.0f,
                false
            )
        )

        LowerLegs = RendererModel(this)
        LowerLegs.setRotationPoint(-1.5f, -3.0f, -0.5f)
        setRotationAngle(LowerLegs, 0.0f, 0.0f, -0.7854f)
        Legs.addChild(LowerLegs)
        LowerLegs.cubeList.add(ModelBox(LowerLegs, 6, 24, 0.5f, 0.0f, 0.5f, 1, 4, 2, 0.0f, false))
        LowerLegs.cubeList.add(ModelBox(LowerLegs, 6, 24, 0.5f, 0.0f, -2.5f, 1, 4, 2, 0.0f, false))

        FootClaws = RendererModel(this)
        FootClaws.setRotationPoint(0.0f, 0.0f, 0.0f)
        Legs.addChild(FootClaws)

        RightClaws = RendererModel(this)
        RightClaws.setRotationPoint(0.0f, 0.0f, 0.0f)
        FootClaws.addChild(RightClaws)
        RightClaws.cubeList.add(
            ModelBox(
                RightClaws,
                2,
                4,
                -2.0f,
                -1.0f,
                1.5f,
                1,
                1,
                0,
                0.0f,
                false
            )
        )
        RightClaws.cubeList.add(
            ModelBox(
                RightClaws,
                2,
                4,
                -2.0f,
                -1.0f,
                1.0f,
                1,
                1,
                0,
                0.0f,
                false
            )
        )
        RightClaws.cubeList.add(
            ModelBox(
                RightClaws,
                2,
                4,
                -2.0f,
                -1.0f,
                0.5f,
                1,
                1,
                0,
                0.0f,
                false
            )
        )

        LeftClaws = RendererModel(this)
        LeftClaws.setRotationPoint(0.0f, 0.0f, 0.0f)
        FootClaws.addChild(LeftClaws)
        LeftClaws.cubeList.add(ModelBox(LeftClaws, 2, 4, -2.0f, -1.0f, -2.0f, 1, 1, 0, 0.0f, false))
        LeftClaws.cubeList.add(ModelBox(LeftClaws, 2, 4, -2.0f, -1.0f, -1.5f, 1, 1, 0, 0.0f, false))
        LeftClaws.cubeList.add(ModelBox(LeftClaws, 2, 4, -2.0f, -1.0f, -2.5f, 1, 1, 0, 0.0f, false))

        Feet = RendererModel(this)
        Feet.setRotationPoint(0.0f, 0.0f, 0.0f)
        Legs.addChild(Feet)
        Feet.cubeList.add(ModelBox(Feet, 21, 4, -1.0f, -1.0f, 0.0f, 3, 1, 2, 0.0f, false))
        Feet.cubeList.add(ModelBox(Feet, 21, 4, -1.0f, -1.0f, -3.0f, 3, 1, 2, 0.0f, false))

        Arms = RendererModel(this)
        Arms.setRotationPoint(0.0f, 0.0f, 0.0f)
        MainBody.addChild(Arms)

        Shoulders = RendererModel(this)
        Shoulders.setRotationPoint(0.0f, 0.0f, 0.0f)
        Arms.addChild(Shoulders)
        Shoulders.cubeList.add(
            ModelBox(
                Shoulders,
                10,
                17,
                -1.0f,
                -12.0f,
                -5.0f,
                3,
                2,
                2,
                0.0f,
                false
            )
        )
        Shoulders.cubeList.add(
            ModelBox(
                Shoulders,
                13,
                6,
                -1.0f,
                -12.0f,
                2.0f,
                3,
                2,
                2,
                0.0f,
                false
            )
        )

        UpperArms = RendererModel(this)
        UpperArms.setRotationPoint(0.0f, 0.0f, 0.0f)
        Arms.addChild(UpperArms)
        UpperArms.cubeList.add(ModelBox(UpperArms, 25, 0, 0.0f, -10.0f, 3.0f, 2, 3, 1, 0.0f, false))
        UpperArms.cubeList.add(
            ModelBox(
                UpperArms,
                18,
                25,
                0.0f,
                -10.0f,
                -5.0f,
                2,
                3,
                1,
                0.0f,
                false
            )
        )

        LowerArms = RendererModel(this)
        LowerArms.setRotationPoint(0.5981f, -5.0f, 3.5f)
        setRotationAngle(LowerArms, 0.0f, 0.0f, -0.7854f)
        Arms.addChild(LowerArms)
        LowerArms.cubeList.add(
            ModelBox(
                LowerArms,
                8,
                21,
                -1.5981f,
                -2.5f,
                -0.5f,
                4,
                2,
                1,
                0.0f,
                false
            )
        )
        LowerArms.cubeList.add(
            ModelBox(
                LowerArms,
                19,
                10,
                -1.5981f,
                -2.5f,
                -8.5f,
                4,
                2,
                1,
                0.0f,
                false
            )
        )

        Hands = RendererModel(this)
        Hands.setRotationPoint(0.0f, 0.0f, 0.0f)
        Arms.addChild(Hands)
        Hands.cubeList.add(ModelBox(Hands, 23, 7, -2.8f, -6.2f, 3.0f, 2, 2, 1, 0.0f, false))
        Hands.cubeList.add(ModelBox(Hands, 8, 12, -2.8f, -6.2f, -5.0f, 2, 2, 1, 0.0f, false))

        HandClaws = RendererModel(this)
        HandClaws.setRotationPoint(0.0f, 0.0f, 0.0f)
        Arms.addChild(HandClaws)

        RightHandClaws = RendererModel(this)
        RightHandClaws.setRotationPoint(0.0f, 0.0f, 0.0f)
        HandClaws.addChild(RightHandClaws)
        RightHandClaws.cubeList.add(
            ModelBox(
                RightHandClaws,
                0,
                4,
                -3.8f,
                -4.7f,
                3.0f,
                1,
                0,
                1,
                0.0f,
                false
            )
        )
        RightHandClaws.cubeList.add(
            ModelBox(
                RightHandClaws,
                0,
                4,
                -3.8f,
                -5.2f,
                3.0f,
                1,
                0,
                1,
                0.0f,
                false
            )
        )
        RightHandClaws.cubeList.add(
            ModelBox(
                RightHandClaws,
                0,
                4,
                -3.8f,
                -5.7f,
                3.0f,
                1,
                0,
                1,
                0.0f,
                false
            )
        )

        LeftHandClaws = RendererModel(this)
        LeftHandClaws.setRotationPoint(-3.3f, -5.2f, -4.5f)
        HandClaws.addChild(LeftHandClaws)
        LeftHandClaws.cubeList.add(
            ModelBox(
                LeftHandClaws,
                0,
                13,
                -0.5f,
                -0.5f,
                -0.5f,
                1,
                0,
                1,
                0.0f,
                false
            )
        )
        LeftHandClaws.cubeList.add(
            ModelBox(
                LeftHandClaws,
                0,
                13,
                -0.5f,
                0.0f,
                -0.5f,
                1,
                0,
                1,
                0.0f,
                false
            )
        )
        LeftHandClaws.cubeList.add(
            ModelBox(
                LeftHandClaws,
                0,
                13,
                -0.5f,
                0.5f,
                -0.5f,
                1,
                0,
                1,
                0.0f,
                false
            )
        )

        Tail = RendererModel(this)
        Tail.setRotationPoint(-1.1f, -5.5f, -0.5f)
        MainBody.addChild(Tail)

        UpperTail = RendererModel(this)
        UpperTail.setRotationPoint(0.0f, 0.0f, 0.0f)
        setRotationAngle(UpperTail, 0.0f, 0.0f, -0.7854f)
        Tail.addChild(UpperTail)
        UpperTail.cubeList.add(
            ModelBox(
                UpperTail,
                0,
                25,
                2.1f,
                0.3284f,
                0.0f,
                2,
                7,
                0,
                0.0f,
                false
            )
        )

        LowerTail = RendererModel(this)
        LowerTail.setRotationPoint(0.0f, 0.0f, 0.0f)
        Tail.addChild(LowerTail)
        LowerTail.cubeList.add(ModelBox(LowerTail, 19, 13, 6.6f, 1.7f, 0.0f, 5, 2, 0, 0.0f, false))
    }

    override fun render(
      entityIn: KoboldEntity,
      limbSwing: Float,
      limbSwingAmount: Float,
      ageInTicks: Float,
      netHeadYaw: Float,
      headPitch: Float,
      scale: Float
    ){
        MainBody.render(scale)
    }

    fun setRotationAngle(modelRenderer: RendererModel, x: Float, y: Float, z: Float) {
        modelRenderer.rotateAngleX = x
        modelRenderer.rotateAngleY = y
        modelRenderer.rotateAngleZ = z
    }
}
