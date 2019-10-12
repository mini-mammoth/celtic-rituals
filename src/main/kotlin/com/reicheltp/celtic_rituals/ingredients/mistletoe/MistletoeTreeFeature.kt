package com.reicheltp.celtic_rituals.ingredients.mistletoe

import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.init.ModBlocks
import java.util.Random
import java.util.function.Function
import kotlin.math.abs
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.GenerationStage
import net.minecraft.world.gen.IWorldGenerationReader
import net.minecraft.world.gen.feature.AbstractTreeFeature
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraft.world.gen.feature.NoFeatureConfig
import net.minecraft.world.gen.placement.ChanceConfig
import net.minecraft.world.gen.placement.Placement

class MistletoeTreeFeature :
    AbstractTreeFeature<NoFeatureConfig>(Function { NoFeatureConfig.deserialize(it) }, false) {
    init {
        registryName = ResourceLocation(MOD_ID, "mistletoe_tree")
    }

    /**
     * This is just picked from [net.minecraft.block.trees.BirchTree]
     *
     * Modified to pick the leaves randomly.
     */
    public override fun place(
      changedBlocks: Set<BlockPos>,
      worldIn: IWorldGenerationReader,
      rand: Random,
      position: BlockPos,
      boundingBox: MutableBoundingBox
    ): Boolean {
        val i = rand.nextInt(3) + 5

        var flag = true
        if (position.y >= 1 && position.y + i + 1 <= worldIn.maxHeight) {
            for (j in position.y..position.y + 1 + i) {
                var k = 1
                if (j == position.y) {
                    k = 0
                }

                if (j >= position.y + 1 + i - 2) {
                    k = 2
                }

                val mutableBlockPos = BlockPos.MutableBlockPos()

                var l = position.x - k
                while (l <= position.x + k && flag) {
                    var i1 = position.z - k
                    while (i1 <= position.z + k && flag) {
                        if (j >= 0 && j < worldIn.maxHeight) {
                            if (!func_214587_a(worldIn, mutableBlockPos.setPos(l, j, i1))) {
                                flag = false
                            }
                        } else {
                            flag = false
                        }
                        ++i1
                    }
                    ++l
                }
            }

            if (!flag) {
                return false
            } else if (isSoil(
                    worldIn,
                    position.down(),
                    getSapling()
                ) && position.y < worldIn.maxHeight - i - 1
            ) {
                this.setDirtAt(worldIn, position.down(), position)

                for (l1 in position.y - 3 + i..position.y + i) {
                    val j2 = l1 - (position.y + i)
                    val k2 = 1 - j2 / 2

                    for (l2 in position.x - k2..position.x + k2) {
                        val i3 = l2 - position.x

                        for (j1 in position.z - k2..position.z + k2) {
                            val k1 = j1 - position.z
                            if (abs(i3) != k2 || abs(k1) != k2 || rand.nextInt(2) != 0 && j2 != 0) {
                                val pos = BlockPos(l2, l1, j1)
                                if (isAirOrLeaves(worldIn, pos)) {
                                    this.setLogState(
                                        changedBlocks,
                                        worldIn,
                                        pos,
                                        leaf(rand),
                                        boundingBox
                                    )
                                }
                            }
                        }
                    }
                }

                for (i2 in 0 until i) {
                    if (isAirOrLeaves(worldIn, position.up(i2))) {
                        this.setLogState(
                            changedBlocks,
                            worldIn,
                            position.up(i2),
                            LOG,
                            boundingBox
                        )
                    }
                }

                return true
            } else {
                return false
            }
        } else {
            return false
        }
    }

    /**
     * One of 20 leaves should be a mistletoe.
     *
     * Thus, there's a good chance for a tree to have at least one.
     */
    private fun leaf(random: Random): BlockState {
        if (random.nextInt(20) == 0) {
            return ModBlocks.MISTLETOE_LEAVES!!.defaultState
        }
        return LEAF
    }

    companion object {
        private val LOG = Blocks.BIRCH_LOG.defaultState
        private val LEAF = Blocks.BIRCH_LEAVES.defaultState
    }
}

/**
 * Adds a mistletoe infected tree every 32 chunks within a medium temperature forest biome.
 */
fun MistletoeTreeFeature.addToForestBiomes(biomes: Collection<Biome>) {
    for (biome in biomes) {
        if (biome.category != Biome.Category.FOREST) {
            continue
        }

        if (biome.tempCategory != Biome.TempCategory.MEDIUM) {
            continue
        }

        biome.addFeature(
            GenerationStage.Decoration.VEGETAL_DECORATION,
            Biome.createDecoratedFeature(
                this,
                IFeatureConfig.NO_FEATURE_CONFIG,
                Placement.CHANCE_HEIGHTMAP,
                ChanceConfig(32)
            )
        )
    }
}
