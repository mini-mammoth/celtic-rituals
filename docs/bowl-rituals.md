# Bowl Rituals

To perform a bowl ritual you have to put the required ingredients into a ritual bowl
and ignite it.

Be aware that the bowl can explode if you insert a false recipe.

## Bowl Recipes

```
Restone + Gold Nugget -> Glowstone
```

## Add recipes

Bowl recipes use the build in JSON format with type `celtic_rituals:bowl_ritual`.
The json file has to be structured the following way:

| path | |
| --- | --- |
| type | celtic_rituals:bowl_ritual` |
| ingredients | Array of [Ingredient][ingredient-wiki] (max 5) |
| duration | **(optional)** Ticks the bowl will burn. (Defaults to 60 / 3s) |
| result | Effect or List of [Effects][effects] | 

[ingredient-wiki]: https://minecraft.gamepedia.com/Recipe
[effects]: effects.md
