package com.reicheltp.celtic_rituals.rituals.bowl

import com.reicheltp.celtic_rituals.init.ModBlocks
import com.reicheltp.celtic_rituals.utils.clear
import com.reicheltp.celtic_rituals.utils.isEmpty
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemStackHandler
import java.util.*

/**
 * TileEntity for @see RitualBowlBlock entity.
 */
class RitualBowlTile : TileEntity(ModBlocks.RITUAL_BOWL_TILE!!), IInventory {
    // The item handler capability keeps
    private val ingredients = LazyOptional.of { ItemStackHandler(5) }

    /**
     * Used when world is loaded an the tile entity is reconstructed.
     */
    override fun read(tag: CompoundNBT) {
        ingredients.ifPresent { it.deserializeNBT(tag.getCompound("inv")) }
        super.read(tag)
    }

    /**
     * Used when world is saved. We have to store all data we want to keep over the world
     */
    override fun write(tag: CompoundNBT): CompoundNBT {
        ingredients.ifPresent { tag.put("inv", it.serializeNBT()) }
        return super.write(tag)
    }

    /**
     *
     */
    override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            // We can not push / pull new items as long as the ritual is running.
            if (blockState.get(BlockStateProperties.ENABLED)) {
                return super.getCapability(cap, side)
            }

            return ingredients.cast()
        }

        return super.getCapability(cap, side)
    }

    override fun getStackInSlot(index: Int): ItemStack = ingredients.map { it.getStackInSlot(index) }.orElse(ItemStack.EMPTY)
    override fun getSizeInventory(): Int = ingredients.map { it.slots }.orElse(0)

    override fun decrStackSize(index: Int, count: Int): ItemStack =
            ingredients.map { it.extractItem(index, count, false) }.orElse(ItemStack.EMPTY)

    override fun setInventorySlotContents(index: Int, stack: ItemStack) =
            ingredients.ifPresent { it.setStackInSlot(index, stack) }

    override fun removeStackFromSlot(index: Int): ItemStack =
            ingredients.map { it.extractItem(index, 1, false) }.orElse(ItemStack.EMPTY)

    override fun clear() = ingredients.ifPresent { it.clear() }

    override fun isEmpty(): Boolean = ingredients.map { it.isEmpty() }.orElse(true)

    // TODO: Checkout what this method does
    override fun isUsableByPlayer(player: PlayerEntity): Boolean = false

    override fun remove() {
        ingredients.invalidate()

        super.remove()
    }
}