package com.reicheltp.celtic_rituals.rituals.bowl

import com.reicheltp.celtic_rituals.init.ModBlocks
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemStackHandler

/**
 * TileEntity for @see RitualBowlBlock entity.
 */
class RitualBowlTile : TileEntity(ModBlocks.RITUAL_BOWL_TILE!!), ITickableTileEntity {
    // The item handler capability keeps
    private val incredients = LazyOptional.of { ItemStackHandler(5) }

    override fun tick() {
        if(world?.isRemote == true){
            System.out.println("TICK")
    /**
     * Used when world is loaded an the tile entity is reconstructed.
     */
    override fun read(tag: CompoundNBT) {
        incredients.ifPresent { it.deserializeNBT(tag.getCompound("inv")) }
        super.read(tag)
    }

    /**
     * Used when world is saved. We have to store all data we want to keep over the world
     */
    override fun write(tag: CompoundNBT): CompoundNBT {
        incredients.ifPresent { tag.put("inv", it.serializeNBT()) }
        return super.write(tag)
    }

    /**
     *
     */
    override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return incredients.cast()
        }

        return super.getCapability(cap, side)
        }

    override fun remove() {
        incredients.invalidate()

        super.remove()
    }
}