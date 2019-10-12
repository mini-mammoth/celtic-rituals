package com.reicheltp.celtic_rituals.proxy

import com.reicheltp.celtic_rituals.MOD_ID
import com.reicheltp.celtic_rituals.blocks.BoneStandBlock
import com.reicheltp.celtic_rituals.effects.AddEnchantmentEffect
import com.reicheltp.celtic_rituals.effects.ChangeWeatherEffect
import com.reicheltp.celtic_rituals.effects.CraftItemEffect
import com.reicheltp.celtic_rituals.effects.EffectSerializer
import com.reicheltp.celtic_rituals.effects.PotionEffect
import com.reicheltp.celtic_rituals.effects.SpawnEntityEffect
import com.reicheltp.celtic_rituals.ingredients.mistletoe.MistletoeItem
import com.reicheltp.celtic_rituals.ingredients.mistletoe.MistletoeLeavesBlock
import com.reicheltp.celtic_rituals.ingredients.mistletoe.addToForestBiomes
import com.reicheltp.celtic_rituals.init.ModBlocks
import com.reicheltp.celtic_rituals.init.ModFeatures
import com.reicheltp.celtic_rituals.init.ModItemGroups
import com.reicheltp.celtic_rituals.init.ModRecipes
import com.reicheltp.celtic_rituals.items.Knife
import com.reicheltp.celtic_rituals.items.SigilItem
import com.reicheltp.celtic_rituals.rituals.bag.RitualBagEntity
import com.reicheltp.celtic_rituals.rituals.bag.RitualBagItem
import com.reicheltp.celtic_rituals.rituals.bowl.BowlRitualRecipe
import com.reicheltp.celtic_rituals.rituals.bowl.RitualBowlBlock
import com.reicheltp.celtic_rituals.rituals.bowl.RitualBowlTile
import com.reicheltp.celtic_rituals.rituals.sacrifice.HeartBreakerEnchantment
import com.reicheltp.celtic_rituals.rituals.sacrifice.HeartItem
import java.util.function.Supplier
import net.minecraft.block.Block
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.EntityClassification
import net.minecraft.entity.EntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.item.crafting.IRecipeType
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.ResourceLocation
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.feature.Feature
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.registries.RegistryBuilder

/**
 * Register stuff we need on client and server side.
 *
 * @see ClientProxy for client-side only registrations.
 * @See ServerProxy for server-side only registrations.
 */
abstract class CommonProxy {
    @SubscribeEvent
    fun onBlocksRegistry(event: RegistryEvent.Register<Block>) {
        event.registry.registerAll(
            MistletoeLeavesBlock(),
            RitualBowlBlock(),
            BoneStandBlock()
        )
    }

    @SubscribeEvent
    fun onTileEntityRegistry(event: RegistryEvent.Register<TileEntityType<*>>) {
        event.registry.registerAll(
            TileEntityType.Builder.create(
                Supplier { RitualBowlTile() },
                ModBlocks.RITUAL_BOWL
            ).build(null).setRegistryName(ResourceLocation(MOD_ID, "ritual_bowl"))
        )
    }

    @SubscribeEvent
    fun registerEntities(event: RegistryEvent.Register<EntityType<*>>) {
        event.registry.registerAll(
            EntityType.Builder.create(RitualBagEntity.RitualBagFactory(), EntityClassification.MISC)
                .setCustomClientFactory(::RitualBagEntity)
                .setTrackingRange(128).setUpdateInterval(20)
                .setShouldReceiveVelocityUpdates(true).build("ritual_bag_entity")
                .setRegistryName(
                    MOD_ID,
                    "ritual_bag_entity"
                )
        )
    }

    @SubscribeEvent
    fun registerEnchantments(event: RegistryEvent.Register<Enchantment>) {
        event.registry.registerAll(
            HeartBreakerEnchantment()
        )
    }

    @SubscribeEvent
    fun registerItems(event: RegistryEvent.Register<Item>) {
        event.registry.registerAll(
            HeartItem(),
            Knife(),
            BlockItem(
                ModBlocks.RITUAL_BOWL!!,
                Item.Properties().maxStackSize(1).group(ModItemGroups.DEFAULT)
            ).setRegistryName(
                ResourceLocation(
                    MOD_ID,
                    "ritual_bowl"
                )
            ),
            MistletoeItem(),
            createItem(ModBlocks.MISTLETOE_LEAVES),
            RitualBagItem(),
            SigilItem()
        )
    }

    @SubscribeEvent
    fun registerRecipes(event: RegistryEvent.Register<IRecipeSerializer<*>>) {
        event.registry.registerAll(
            BowlRitualRecipe.Serializer()
        )

        ModRecipes.BOWL_RITUAL_TYPE =
            IRecipeType.register<BowlRitualRecipe>("celtic_rituals:bowl_ritual")
    }

    @SubscribeEvent
    fun createRegistries(event: RegistryEvent.NewRegistry) {
        EffectSerializer.REGISTRY = RegistryBuilder<EffectSerializer<*>>()
            .setName(ResourceLocation(MOD_ID, "effects"))
            .setType(EffectSerializer::class.java)
            .create()
    }

    @SubscribeEvent
    fun registerEffects(event: RegistryEvent.Register<EffectSerializer<*>>) {
        event.registry.registerAll(
            AddEnchantmentEffect.SERIALIZER,
            ChangeWeatherEffect.SERIALIZER,
            CraftItemEffect.SERIALIZER,
            PotionEffect.SERIALIZER,
            SpawnEntityEffect.SERIALIZER
        )
    }

    @SubscribeEvent
    fun registerBiomeFeatures(event: RegistryEvent.Register<Feature<*>>) {
        event.registry.registerAll(
            ModFeatures.MISTLETOE_TREE
        )
    }

    @SubscribeEvent
    fun registerBiome(event: RegistryEvent.Register<Biome>) {
        ModFeatures.MISTLETOE_TREE.addToForestBiomes(event.registry.values)
    }

    /**
     * Creates an [BlockItem] instance from existing block.
     */
    private fun createItem(block: Block?, props: Item.Properties? = null): Item {
        return BlockItem(
            block!!,
            props ?: Item.Properties().group(ModItemGroups.DEFAULT)
        ).setRegistryName(
            block.registryName
        )
    }
}
