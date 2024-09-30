/*
 * Copyright (c) 2022-2024 Ampflower
 *
 * This software is subject to the terms of either the MIT or CC0-1.0 Licenses.
 * If a copy was not distributed with this file, you can obtain one at
 * https://github.com/Modflower/QuickBench/blob/trunk/LICENSE-MIT
 * https://github.com/Modflower/QuickBench/blob/trunk/LICENSE-CC0
 *
 * Sources:
 *  - https://github.com/Modflower/QuickBench
 *
 * SPDX-License-Identifier: MIT OR CC0-1.0
 *
 * Additional details are outlined in LICENSE.md, which you can obtain at
 * https://github.com/Modflower/QuickBench/blob/trunk/LICENSE.md
 */

package tfar.fastbench.mixin;// Created 2022-05-12T13:17:33

import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.fastbench.interfaces.CraftingInventoryDuck;

/**
 * @author Ampflower
 * @since ${version}
 **/
@Mixin(ServerPlaceRecipe.class)
public class ServerPlaceRecipeMixin<I extends RecipeInput, R extends Recipe<I>> {
    @Shadow
    protected RecipeBookMenu<I, R> menu;

    /**
     * Inhibits the matrix check to avoid doing expensive checks for recipes while the inventory is being cleared.
     */
    @Inject(method = "clearGrid", at = @At("HEAD"))
    private void fastbench$setCheckMatrixFalse(CallbackInfo ci) {
        // Important: Do not try to call this if the menu doesn't implement the
        // duck interface.
        if (menu instanceof CraftingInventoryDuck duck) {
            duck.setCheckMatrixChanges(false);
        }
    }
}
