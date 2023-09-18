# QuickBench & Tweaks

A crafting table optimisation mod, improving compute and networking performance of various crafting tables, based off
of [Fast Workbench for Forge][FastWorkbench-curse] and [FastBench for Fabric][FastWorkbench-curse].

## How does it work?

Inherited from Fast Workbench and FastBench,
QuickBench optimises the shift+click crafting to prevent executing the matrix calculation,
reducing network traffic in the process.

QuickBench also reintroduces the ability to 'learn' recipes by using them, mimicking vanilla behaviour,
and allows you to use it solely server-side,
allowing any server to add it in and benefit to everyone.<sup>[[upstream's #8]]</sup>

This benefit is provided to various crafting tables, including ones using the vanilla classes,
allowing modded use to be seamless as long as various mods play nicely.

## Any side effects?

Within Vanilla, none that I could find in use.
With other mods, there is currently one known with Yttr, [listed below](#any-mods-it-breaks).

If you find any bugs or parity issues, including mod compatibility, feel free to report it on the [issue tracker].

## What mods does it work with?

### Optimises

- Botania's Assembly & Manufactory Halo<sup>(also fixes [upstream's #21])</sup>
- Crafting Pad
- Portable Tables' Crafting Table

### Fixed for [upstream's #20]

The crafting output slot now works with these mods, which broke with FastBench:

- Yttr's Project Table
- Tom's Storage's Crafting Terminals (note: already fairly optimised)
- Improved Workstations' Crafting Stations
- Enhanced Workbenches' Crafting Station
- Enhanced Project Table
- Crafting Craft's Inventory & Portable Crafting
- FabricAutoCrafter
- Inventorio<sup>(also tracked at [upstream's #17])</sup>

### Doesn't effect:

- Applied Energistics 2's Crafting Terminals (note: already optimised, never broke)

## Any mods it breaks?

- Yttr's Crafter Rafter (already broken in vanilla usage, amplified by FastBench & QuickBench)

No other mods are known to be broken at the moment.
If you find one QuickBench breaks, feel free to report them on the [issue tracker].

## License

This mod, QuickBench & Tweaks, is licensed under the [MIT] license, inherited from the upstreams,
[FastWorkbench for Forge][FastWorkbench-git] and [FastBench for Fabric][FabricFastBench-git],
made by [Shadows of Fire] and [Tfarcenim] respectively.

Due to the lack of clarity from the upstream repository,
FabricFastBench is assumed to have been licensed under the [MIT] license,
as its upstream, FastWorkbench is licensed as [MIT],
and the [CurseForge project][FabricFastBench-curse] claims it is also licensed as [MIT],
unlike the [GitHub repository][FabricFastBench-git], which claims it's licensed as [CC0-1.0].

It is maybe fair to assume
that the code solely produced by [Tfarcenim] maybe used under the [CC0-1.0] when without context.
However, given the original README.md stated that the template was under the [CC0-1.0], and everything else says [MIT],
it should be assumed the license of choice was intended to be [MIT].

Several contributions from [Ampflower] are also available under [CC0-1.0],
and will be clearly marked in the license header of each file.
You may freely use snippets or full copies of code solely contributed by Ampflower under the terms of the [CC0-1.0].
If the snippets have been combined, remixed or derived from the surrounding code,
you must assume the terms of the [MIT] license may be applied.

Both the [CC0-1.0] and [MIT] licenses are provided for reference.

### Disclaimer:

The dates and names used within the copyright header have been reconstructed based on the licenses and git history of
both [FastWorkbench][FastWorkbench-git] and [FastBench for Fabric][FabricFastBench-git].

<!-- --- Links below --- -->

[Shadows of Fire]: https://github.com/Shadows-of-Fire

[Tfarcenim]: https://github.com/Tfarcenim

[Ampflower]: https://github.com/Ampflower

[FastWorkbench-git]: https://github.com/Shadows-of-Fire/FastWorkbench

[FastWorkbench-curse]: https://www.curseforge.com/minecraft/mc-mods/fastworkbench

[FabricFastBench-git]: https://github.com/Tfarcenim/FabricFastBench

[FabricFastBench-curse]: https://www.curseforge.com/minecraft/mc-mods/fastbench-for-fabric

[MIT]: LICENSE-MIT

[CC0-1.0]: LICENSE-CC0

<!-- --- Meta --- -->

[issue tracker]: https://github.com/Modflower/QuickBench/issues

[upstream's #8]: https://github.com/Tfarcenim/FabricFastBench/issues/8

[upstream's #17]: https://github.com/Tfarcenim/FabricFastBench/issues/17

[upstream's #20]: https://github.com/Tfarcenim/FabricFastBench/issues/20

[upstream's #21]: https://github.com/Tfarcenim/FabricFastBench/issues/21