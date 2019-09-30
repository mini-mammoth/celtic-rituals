package com.reicheltp.celtic_rituals.utils

import com.sun.org.apache.xpath.internal.operations.Bool
import net.minecraft.item.ItemStack
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
    for(i in 0 until this.slots){
        this.setStackInSlot(i, ItemStack.EMPTY)
    }
}

/**
 * Sets all slots empty.
 */
fun ItemStackHandler.isEmpty(): Boolean {
    for(i in 0 until this.slots){
        if (!this.getStackInSlot(i).isEmpty){
            return false
        }
    }

    return true
}