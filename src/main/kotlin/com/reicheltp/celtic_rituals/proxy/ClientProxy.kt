package com.reicheltp.celtic_rituals.proxy

import com.reicheltp.celtic_rituals.init.ModItems
import com.reicheltp.celtic_rituals.rituals.bag.RitualBagEntity
import com.reicheltp.celtic_rituals.rituals.bag.RitualBagItem
import com.reicheltp.celtic_rituals.rituals.bag.RitualBagRenderer
import com.reicheltp.celtic_rituals.rituals.bowl.RitualBowlRenderer
import com.reicheltp.celtic_rituals.rituals.bowl.RitualBowlTile
import com.reicheltp.celtic_rituals.rituals.sacrifice.HeartItem
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.color.IItemColor
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

/**
 * Register stuff we need on client side only. Usually used to register particles and GUIs.
 *
 * @See CommonProxy for registrations on both sides.
 * @see ServerProxy for server-side only registrations.
 */
class ClientProxy : CommonProxy() {
    @SubscribeEvent
    fun clientSetup(event: FMLClientSetupEvent) {
        ClientRegistry.bindTileEntitySpecialRenderer(
            RitualBowlTile::class.java,
            RitualBowlRenderer()
        )

        RenderingRegistry.registerEntityRenderingHandler<RitualBagEntity>(
            RitualBagEntity::class.java,
            RitualBagRenderer.RitualBagRenderFactory()
        )

        // See: https://mcforge.readthedocs.io/en/1.13.x/models/color/
        Minecraft.getInstance().itemColors.register(
            IItemColor { item, layer -> if (layer == 0) -1 else RitualBagItem.getColor(item) },
            ModItems.RITUAL_BAG
        )

        Minecraft.getInstance().itemColors.register(
            IItemColor { i, l -> HeartItem.getColor(i, l) },
            ModItems.HEART
        )
    }
}
