package com.reicheltp.mymod

import com.reicheltp.mymod.proxy.ClientProxy
import com.reicheltp.mymod.proxy.CommonProxy
import com.reicheltp.mymod.proxy.ServerProxy
import net.alexwells.kottle.FMLKotlinModLoadingContext
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.common.Mod
import java.util.function.Supplier

const val MOD_ID = "my-mod"

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
object MyMod {
    /**
     * Pick a proxy depending if this mod runs client or server side.
     *
     * Follows an approach from: https://mcforge.readthedocs.io/en/latest/concepts/sides/#sidedproxy
     */
    private val proxy: CommonProxy = DistExecutor.runForDist<CommonProxy>(
            { Supplier { ClientProxy() } },
            { Supplier { ServerProxy() } }
    )

    init {
        FMLKotlinModLoadingContext.get().modEventBus.register(proxy)
    }
}
