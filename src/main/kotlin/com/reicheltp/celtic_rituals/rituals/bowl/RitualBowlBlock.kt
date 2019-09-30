package com.reicheltp.celtic_rituals.rituals.bowl

import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.utils.canBeCombined
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.InventoryHelper
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.Items
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Hand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.World

/**
 * Central item required to perform rituals.
 *
 * The player puts all his items in a ritual bowl and burn it afterward.
 */
class RitualBowlBlock : Block(
        Properties.create(Material.WOOD)
                .variableOpacity()
                .hardnessAndResistance(1.0f)
                .sound(SoundType.WOOD)
) {
    companion object {
        private val SHAPE = makeCuboidShape(.0, .0, .0, 16.0, 8.0, 16.0)
    }

    init {
        registryName = ResourceLocation(MOD_ID, "ritual_bowl")
    }

    override fun isSolid(state: BlockState): Boolean {
        // TODO: This seems to be deprecated. We should discover a better way.
        return false
    }

    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return SHAPE
    }

    override fun hasTileEntity(state: BlockState?): Boolean = true

    override fun createTileEntity(state: BlockState?, world: IBlockReader?): TileEntity? = RitualBowlTile()

    /**
     * Drops all stored items when block gets replaced.
     *
     * Borrowed from AbstractFurnaceBlock
     */
    override fun onReplaced(state: BlockState, worldIn: World, pos: BlockPos, newState: BlockState, isMoving: Boolean) {
        if (state.block !== newState.block) {
            val tile = worldIn.getTileEntity(pos)
            if (tile is RitualBowlTile) {
                for (i in 0 until tile.getSizeInventory()) {
                    InventoryHelper.spawnItemStack(worldIn, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), tile.getStackInSlot(i))
                }
                worldIn.updateComparatorOutputLevel(pos, this)
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving)
        }
    }

    override fun onBlockActivated(state: BlockState, worldIn: World, pos: BlockPos, player: PlayerEntity, handIn: Hand, hit: BlockRayTraceResult): Boolean {
        if (handIn != Hand.MAIN_HAND) {
            return false
        }

        val tile = worldIn.getTileEntity(pos)
        if (tile !is RitualBowlTile) {
            return false
        }

        if (player.heldItemMainhand.item == Items.FLINT_AND_STEEL) {
            // TODO: This will ignite the bowl and start a ritual
            return false
        }

        // Sneak pick pulls a stack from bowl
        if (player.heldItemMainhand.isEmpty && player.isSneaking) {
            for (i in 0 until tile.getSizeInventory()) {
                val stack = tile.getStackInSlot(i)
                if (stack.isEmpty) {
                    continue
                }

                InventoryHelper.spawnItemStack(worldIn, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), stack)
            }
        }

        // Use item on bowl puts it in
        for (i in 0 until tile.getSizeInventory()) {
            val stack = tile.getStackInSlot(i)

            when {
                stack.isEmpty -> {
                    val item = player.heldItemMainhand.split(1)
                    tile.setInventorySlotContents(i, item)

                    return true
                }
                stack.canBeCombined(player.heldItemMainhand) -> {
                    player.heldItemMainhand.shrink(1)
                    stack.grow(1)

                    return true
                }
            }
        }

        return false
    }
}
