package com.reicheltp.celtic_rituals.blocks

import com.reicheltp.celtic_rituals.MOD_ID
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.block.material.MaterialColor
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.ResourceLocation

class BoneStandBlock() : Block(Properties.create(Material.ROCK, MaterialColor.SAND).hardnessAndResistance(2.0f)) {
    init {
        registryName = ResourceLocation(MOD_ID, "bone_stand")
    }

    override fun getRenderLayer(): BlockRenderLayer {
        return BlockRenderLayer.CUTOUT
    }
}