package com.reicheltp.celtic_rituals.proxy

import com.reicheltp.celtic_rituals.init.ModBlocks
import com.reicheltp.celtic_rituals.entities.WraithEntity
import com.reicheltp.celtic_rituals.entities.WraithEntityRenderer
import com.reicheltp.celtic_rituals.init.ModItems
import com.reicheltp.celtic_rituals.rituals.bag.RitualBagEntity
import com.reicheltp.celtic_rituals.rituals.bag.RitualBagItem
import com.reicheltp.celtic_rituals.rituals.bag.RitualBagRenderer
import com.reicheltp.celtic_rituals.rituals.bowl.RitualBowlRenderer
import com.reicheltp.celtic_rituals.rituals.bowl.RitualBowlTile
import com.reicheltp.celtic_rituals.rituals.sacrifice.HeartItem
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.item.BlockItem
import net.minecraft.world.FoliageColors
import net.minecraft.world.biome.BiomeColors
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

<<<<<<< master
        val blockColors = Minecraft.getInstance().blockColors

        blockColors.register(
            IBlockColor { state, world, pos, layer ->
                if (layer != 0) {
                    -1
                } else if (world != null && pos != null) {
                    BiomeColors.getFoliageColor(world, pos)
                } else {
                    FoliageColors.getDefault()
                }
            },
            ModBlocks.MISTLETOE_LEAVES
        )
=======
        RenderingRegistry.registerEntityRenderingHandler<WraithEntity>(
            WraithEntity::class.java,
            WraithEntityRenderer.WraithEntityRendererFactory()
        )

>>>>>>> Add WraithEntity
        // See: https://mcforge.readthedocs.io/en/1.13.x/models/color/
        Minecraft.getInstance().itemColors.register(
            IItemColor { item, layer -> if (layer == 0) -1 else RitualBagItem.getColor(item) },
            ModItems.RITUAL_BAG
        )

        Minecraft.getInstance().itemColors.register(
            IItemColor { i, l -> HeartItem.getColor(i, l) },
            ModItems.HEART
        )

        Minecraft.getInstance().itemColors.register(
            IItemColor { i, l ->
                val state = (i.item as BlockItem).block.defaultState
                blockColors.getColor(state, null, null, l)
            },
            ModBlocks.MISTLETOE_LEAVES
        )
    }
}
