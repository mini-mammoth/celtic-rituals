package com.reicheltp.celtic_rituals.init

import com.reicheltp.celtic_rituals.itemgroups.CelticRitualsItemGroup
import java.util.function.Supplier

object ModItemGroups {
    var DEFAULT =
        CelticRitualsItemGroup("default", Supplier { ModItems.RITUAL_SIGIL!! })
}
