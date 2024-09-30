/*
 * Copyright (c) 2024 Ampflower
 * Copyright (c) 2020-2021 Tfarcenim
 *
 * This software is subject to the terms of the MIT License.
 * If a copy was not distributed with this file, you can obtain one at
 * https://github.com/Modflower/QuickBench/blob/trunk/LICENSE-MIT
 *
 * Sources:
 *  - https://github.com/Modflower/QuickBench
 *  - https://github.com/Tfarcenim/FabricFastBench
 *
 * SPDX-License-Identifier: MIT
 */

package tfar.fastbench.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.fastbench.MixinHooks;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

	@Inject(method = "getRemainingItemsFor",at = @At("HEAD"),cancellable = true)
    private <C extends RecipeInput, T extends Recipe<C>> void techRebornWorkAround(
            RecipeType<T> recipeType, C craftInput, Level world, CallbackInfoReturnable<NonNullList<ItemStack>> cir) {
        if (!MixinHooks.hascachedrecipe || !(craftInput instanceof CraftingInput craftingInput)) {
            return;
        }
        if (MixinHooks.lastRecipe != null) {
            cir.setReturnValue(MixinHooks.lastRecipe.getRemainingItems(craftingInput));
        } else {
            cir.setReturnValue(MixinHooks.copy(craftingInput));
        }
        MixinHooks.hascachedrecipe = false;
    }
}
