package com.reicheltp.celtic_rituals.utils

import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.InventoryHelper
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.NonNullList
import net.minecraftforge.items.ItemStackHandler

/**
 * Returns true if both stacks match an can be combined to a single one.
 */
fun ItemStack.canBeCombined(stack2: ItemStack): Boolean {
    return when {
        this.item !== stack2.item -> false
        this.damage != stack2.damage -> false
        this.count > this.maxStackSize -> false
        else -> ItemStack.areItemStackTagsEqual(this, stack2)
    }
}

/**
 * Sets all slots empty.
 */
fun ItemStackHandler.clear() {
    for (i in 0 until this.slots) {
        this.setStackInSlot(i, ItemStack.EMPTY)
    }
}

/**
 * Sets all slots empty.
 */
fun ItemStackHandler.isEmpty(): Boolean {
    for (i in 0 until this.slots) {
        if (!this.getStackInSlot(i).isEmpty) {
            return false
        }
    }

    return true
}

/**
 * Adds item to player inventory, or drop it if inventory is full.
 */
fun Entity.addItemOrDrop(stack: ItemStack) {
    if (this !is PlayerEntity || !this.addItemStackToInventory(stack)) {
        InventoryHelper.dropItems(this.world, this.position, NonNullList.from(stack))
    }
}

/**
 * Writes [ItemStack] to compound.
 */
fun CompoundNBT.putItemStack(key: String, stack: ItemStack) {
    val tag = CompoundNBT()
    stack.write(tag)
    this.put(key, tag)
}

/**
 * Reads entry from compound as [ItemStack].
 */
fun CompoundNBT.getItemStack(key: String): ItemStack {
    return ItemStack.read(this.getCompound(key))
}
