package com.reicheltp.celtic_rituals.rituals.bowl

import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.init.ModBlocks
import com.reicheltp.celtic_rituals.utils.canBeCombined
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.inventory.InventoryHelper
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.IEnviromentBlockReader
import net.minecraft.world.IWorldReader
import net.minecraft.world.World
import java.util.*

/**
 * Central item required to perform rituals.
 *
 * The player puts all his items in a ritual bowl and burn it afterward.
 */
class RitualBowlBlock : Block(
        Properties.create(Material.WOOD)
                .variableOpacity()
                .hardnessAndResistance(1.0f)
                .lightValue(14)
                .sound(SoundType.WOOD)
) {
    companion object {
        private val SHAPE = makeCuboidShape(.0, .0, .0, 16.0, 8.0, 16.0)
        private val random = Random()
    }

    init {
        registryName = ResourceLocation(MOD_ID, "ritual_bowl")
        defaultState = stateContainer.baseState.with(BlockStateProperties.ENABLED, false)
    }

    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return SHAPE
    }

    override fun getRenderLayer(): BlockRenderLayer {
        return BlockRenderLayer.CUTOUT
    }

    override fun getLightValue(state: BlockState?, world: IEnviromentBlockReader?, pos: BlockPos?): Int {
        return if (state!!.get(BlockStateProperties.ENABLED)) super.getLightValue(state, world, pos) else 0
    }

    override fun hasTileEntity(state: BlockState?): Boolean = true

    override fun createTileEntity(state: BlockState?, world: IBlockReader?): TileEntity? = RitualBowlTile()

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>) {
        builder.add(BlockStateProperties.ENABLED)
    }

    /**
     * Drops all stored items when block gets replaced.
     *
     * Borrowed from AbstractFurnaceBlock
     */
    override fun onReplaced(state: BlockState, worldIn: World, pos: BlockPos, newState: BlockState, isMoving: Boolean) {
        if (state.block !== newState.block) {
            if (state.get(BlockStateProperties.ENABLED)) {
                // Destroying a running ritual will loose all items
                worldIn.setBlockState(pos, Blocks.FIRE.defaultState)
            } else {
                val tile = worldIn.getTileEntity(pos)
                if (tile is RitualBowlTile) {
                    InventoryHelper.dropInventoryItems(worldIn, pos, tile)
                }
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

        val inProgress = state.get(BlockStateProperties.ENABLED)

        // Extinguishes an ignited bowl, but removes all items.
        if (player.heldItemMainhand.item == Items.WATER_BUCKET) {
            worldIn.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 2.6f + (random.nextFloat() - random.nextFloat()) * 0.8f)

            if (inProgress) {
                worldIn.setBlockState(pos, state.with(BlockStateProperties.ENABLED, false))
            }

            player.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack(Items.BUCKET))

            tile.clear()

            return true
        }

        // Ignites the bowl
        if (player.heldItemMainhand.item == Items.FLINT_AND_STEEL) {
            // TODO: This will ignite the bowl and start a ritual
            worldIn.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, random.nextFloat() * 0.4f + 0.8f)

            if (player is ServerPlayerEntity) {
                CriteriaTriggers.PLACED_BLOCK.trigger(player, pos, player.heldItemMainhand)
                player.heldItemMainhand.damageItem(1, player, { it.sendBreakAnimation(handIn) })
            }

            if (!inProgress) {
                worldIn.setBlockState(pos, state.with(BlockStateProperties.ENABLED, true))
                worldIn.pendingBlockTicks.scheduleTick(pos, ModBlocks.RITUAL_BOWL!!, 200)
            }

            return true
        }

        // Sneak pick pulls a stack from bowl
        if (!inProgress && player.heldItemMainhand.isEmpty && player.isSneaking) {
            for (i in 0 until tile.sizeInventory) {
                val stack = tile.getStackInSlot(i)
                if (stack.isEmpty) {
                    continue
                }

                InventoryHelper.spawnItemStack(worldIn, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), stack)
            }
        }

        if (!inProgress) {
            // Use item on bowl puts it in
            for (i in 0 until tile.sizeInventory) {
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
        }

        return false
    }

    override fun tick(state: BlockState, worldIn: World, pos: BlockPos, random: Random) {
        worldIn.setBlockState(pos, state.with(BlockStateProperties.ENABLED, false))

        // TODO: Craft the item / Perform the ritual

        super.tick(state, worldIn, pos, random)
    }
}
