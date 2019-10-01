package com.reicheltp.celtic_rituals.blocks

import com.reicheltp.celtic_rituals.MOD_ID
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.block.material.MaterialColor
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.Direction
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class BoneStandBlock() : Block(Properties.create(Material.ROCK, MaterialColor.SAND).hardnessAndResistance(2.0f)) {
    init {
        registryName = ResourceLocation(MOD_ID, "bone_stand")
    }

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>) {
        builder.add(BlockStateProperties.FACING)
        super.fillStateContainer(builder)
    }

    override fun getRenderLayer(): BlockRenderLayer {
        return BlockRenderLayer.CUTOUT
    }
}