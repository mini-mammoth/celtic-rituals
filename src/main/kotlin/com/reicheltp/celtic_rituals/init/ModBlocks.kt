package com.reicheltp.celtic_rituals.init

import net.minecraft.block.Block
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.registries.ObjectHolder

object ModBlocks {
    @ObjectHolder("celtic_rituals:bone_stand")
    var BONE_STAND: Block? = null

    @ObjectHolder("celtic_rituals:ritual_bowl")
    var RITUAL_BOWL: Block? = null

    @ObjectHolder("celtic_rituals:ritual_bowl")
    var RITUAL_BOWL_TILE: TileEntityType<*>? = null

    @ObjectHolder("celtic_rituals:mistletoe_leaves")
    var MISTLETOE_LEAVES: Block? = null
}
