package com.reicheltp.celtic_rituals.rituals.bowl

import com.reicheltp.celtic_rituals.init.ModBlocks
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.NonNullList
import java.util.logging.LogManager

/**
 * TileEntity for @see RitualBowlBlock entity.
 */
class RitualBowlTile : TileEntity(ModBlocks.RITUAL_BOWL_TILE!!), ITickableTileEntity {
    override fun tick() {
        if(world?.isRemote == true){
            System.out.println("TICK")
        }
    }
}