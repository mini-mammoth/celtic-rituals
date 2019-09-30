package com.reicheltp.celtic_rituals.utils

import net.minecraft.item.ItemStack

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