# Effects

Effects are things that happens when a ritual finished. 
Effects are identified by `{ "effect": ResourceLocation }`

## Known Effects

### Change Weather

Changes the weather for a given time.

| param | value |
| --- | --- |
| effect | `celtic_rituals:change_weather` |
| weather | One of `clear`, `rain`, `thunder` |
| duration | **(optional)** time in ticks. Defaults to 1200 |  


### Craft Item

Places an item in the ritual bowl.

| param | value |
| --- | --- |
| effect | `celtic_rituals:craft_item` |
| item | name of the item |
| count | **(optional)** number of items |  


### Potion effect

Creates a new `AreaEffectCloud` originating at the bowl. 

| param | value |
| --- | --- |
| effect | `celtic_rituals:potion` |
| potion | Name of the potion. E.g. `minecraft:healing` |
| duration | **(optional)** Time in ticks the area is effected. Defaults to 60 |
| radius | **(optional)** Radius, which is effected. Defaults to 3.0 |



### Change Weather

| param | value |
| --- | --- |
| effect | `celtic_rituals:spawn_entity` |
| entity | Name of the entity type. E.g. `minecraft:cow` |
| min_distance | **(optional)** Minimum distance the entities will spawn. Defaults to 1 |  
| max_distance | **(optional)** Maximum distance the entities will spawn. Defaults to 3 |  
| count | **(optional)** Number of entities. Defaults to 1 |  

## Develop a new effect

To add a new effect you have to implement a `IEffect` and its serializer 
and put them in the registry. 

#### Example implementation

```kotlin
class MyCustomEffect(
    // add all params needed to specify this effect
) : IEffect {
    override fun apply(world: World, pos: BlockPos, inv: IInventory): Boolean {
        // use params to apply the effect to the world
    }

    override val serializer: EffectSerializer<*> = SERIALIZER

    companion object {
        val SERIALIZER = object : EffectSerializer<MyCustomEffect>() {
            init {
                registryName = ResourceLocation(MOD_ID, "my_custom")
            }

            override fun read(element: JsonObject): MyCustomEffect {
                // TODO: use element.get to read params from json
                return MyCustomEffect(...)
            }

            override fun read(buffer: PacketBuffer): MyCustomEffect {
                // TODO: use buffer.read helper to deserialize your effect from packet buffer

                return MyCustomEffect(...)
            }

            override fun write(buffer: PacketBuffer, effect: MyCustomEffect) {
                // TODO: Use buffer.write helper to serialize your effect into the buffer
            }
        }
    }
}
```

Go into common proxy and add the serializer to the registry.
```kotlin
@SubscribeEvent
fun registerEffects(event: RegistryEvent.Register<EffectSerializer<*>>) {
    event.registry.registerAll(
        MyEffect.SERIALIZER,
        ...
    )
}
```
