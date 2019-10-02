# Celtic Rituals

This mod aims to add a bunch of celtic rituals to Minecraft.

## Core Mechanics
 
Sacrifice mobs to please the gods and become a strong druid. 
In exchange the gods provide you with rewards like **rich harvest** 
or **improved condition**. 


Some rituals require special settings like location or weather, but also
some druid experience. 
Unfortunately, the ancient knowledge about all rituals got lost.
Thus, you have to discover those yourself. -
*These conditions are set at world generation* 

Be careful. Once you got the attention of the gods they are not always your friends. 

## Development

Developing this mod, we suggest `IntelliJ IDEA Community`. 

### Requirements
- Java 1.8 (Exactly this version. It's required by Minecraft)

### Prepare your workspace

1. Install [`Minecraft Development`][mc-dev] plugin for Idea
2. Run `.\gradlew initIdea` to generate the required run configurations
3. You can now open the project with `IntelliJ`
4. Use `Import gradle config`
5. Use `runClient` config to start/debug Minecraft

> Don't be eager as MCP and Forge are downloaded on first run.

> **Remember**
> You always have to rerun `initIdea` once the used Forge or MCP version changed.

`initIdea` also applies the desired Kotlin coding style and adds a `ktlint` pre-commit hook. You can check the style
yourself with `gradle ktlintCheck` and enforce it via `gradle ktlintFormat`.

[mc-dev]: https://plugins.jetbrains.com/plugin/8327-minecraft-development
