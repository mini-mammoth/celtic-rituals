package com.reicheltp.celtic_rituals.blocks

import com.reicheltp.celtic_rituals.MOD_ID
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.material.Material
import net.minecraft.block.material.MaterialColor
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.World

class BoneStandBlock : Block(Properties.create(Material.ROCK, MaterialColor.SAND).hardnessAndResistance(2.0f)) {
    init {
        registryName = ResourceLocation(MOD_ID, "bone_stand")
    }

    companion object {
        private val SHAPE = makeCuboidShape(5.0, .0, 5.0, 11.0, 12.0, 11.0)

        /**
         * Returns true, if the bone can be placed on this block
         */
        fun isValidUnderground(block: Block): Boolean {
            return block === Blocks.GRASS_BLOCK || block === Blocks.DIRT
        }
    }

    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return SHAPE
    }

    override fun neighborChanged(state: BlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos, p_220069_6_: Boolean) {
        var blockBeneath = worldIn.getBlockState(fromPos).block

        // if block below gets changed to something else than dirt or grass drop a bone and remove BoneStandBlock
        if (pos.down() == fromPos && !isValidUnderground(blockBeneath)) {
            worldIn.setBlockState(pos, Blocks.AIR.defaultState)

            val boneItemStack = ItemStack(Items.BONE, 1)

            if (blockBeneath == Blocks.AIR)
                spawnAsEntity(worldIn, fromPos, boneItemStack)
            else
                spawnAsEntity(worldIn, pos, boneItemStack)
        }
    }

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>) {
        builder.add(BlockStateProperties.FACING)
        super.fillStateContainer(builder)
    }

    override fun getRenderLayer(): BlockRenderLayer {
        return BlockRenderLayer.CUTOUT
    }
}
