package com.reicheltp.celtic_rituals.itemgroups

import com.reicheltp.celtic_rituals.MOD_ID
import java.util.function.Supplier
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack

class CelticRitualsItemGroup : ItemGroup {
    private val supplier: Supplier<Item>

    constructor(
      label: String,
      supplier: Supplier<Item>
    ) : super("$MOD_ID.$label") {
        this.supplier = supplier
    }

    override fun createIcon(): ItemStack {
        return ItemStack(supplier.get())
    }
}
