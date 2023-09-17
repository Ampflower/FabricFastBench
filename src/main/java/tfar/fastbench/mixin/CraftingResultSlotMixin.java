/*
 * Copyright (c) 2022-2023 Ampflower
 * Copyright (c) 2020-2021 Tfarcenim
 * Copyright (c) 2018-2021 Brennan Ward
 *
 * This software is subject to the terms of the MIT License.
 * If a copy was not distributed with this file, you can obtain one at
 * https://github.com/Modflower/QuickBench/blob/trunk/LICENSE-MIT
 *
 * Sources:
 *  - https://github.com/Modflower/QuickBench
 *  - https://github.com/Tfarcenim/FabricFastBench
 *  - https://github.com/Shadows-of-Fire/FastWorkbench
 *
 * SPDX-License-Identifier: MIT
 *
 * Contributions from Ampflower may additionally be available under CC0-1.0,
 * as part of the pull-request for upstreaming to FabricFastBench.
 * If a copy was not distributed with this file, you can obtain one at
 * https://github.com/Modflower/QuickBench/blob/trunk/LICENSE-CC0
 *
 * Additional details are outlined in LICENSE.md, which you can obtain at
 * https://github.com/Modflower/QuickBench/blob/trunk/LICENSE.md
 */
// This file has been traced to be sourced from `shadows.fastbench.util.CraftResultSlotExt`

package tfar.fastbench.mixin;


import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.fastbench.MixinHooks;
import tfar.fastbench.interfaces.CraftingInventoryDuck;

import java.util.Collections;

@Mixin(ResultSlot.class)
public class CraftingResultSlotMixin extends Slot {

	@Shadow
	@Final
	private CraftingContainer craftSlots;

	@Shadow
	@Final
	private Player player;

	public CraftingResultSlotMixin(Container inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	@Redirect(method = "remove", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;remove(I)Lnet/minecraft/world/item/ItemStack;"))
	private ItemStack copy(Slot slot, int amount) {
		return slot.getItem().copy();
	}

	@Override
	public void set(ItemStack stack) {
		if (player.level.isClientSide) {
			super.set(stack);
		}
		//do nothing
	}

	@Redirect(method = "checkTakeAchievements",
			at = @At(value = "INVOKE",target = "Lnet/minecraft/world/inventory/RecipeHolder;awardUsedRecipes(Lnet/minecraft/world/entity/player/Player;)V"))
	public void no(Player player) {
		if (((CraftingInventoryDuck) craftSlots).getCheckMatrixChanges() &&
				this.container instanceof RecipeHolder recipeHolder) {
			var recipeUsed = recipeHolder.getRecipeUsed();
			if (recipeUsed != null && !recipeUsed.isSpecial()) {
				player.awardRecipes(Collections.singleton(recipeUsed));
			}
		}
	}

	//this.container is actually the crafting result inventory so it's a safe cast
	//using an inject instead of a redirect as a workaround for tech reborn's BS
	@Inject(method = "onTake", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/RecipeManager;getRemainingItemsFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Lnet/minecraft/core/NonNullList;"))
	private void cache(Player player, ItemStack stack, CallbackInfo ci) {
		Recipe<CraftingContainer> lastRecipe = (Recipe<CraftingContainer>) ((ResultContainer)this.container).getRecipeUsed();
		MixinHooks.lastRecipe = lastRecipe != null && lastRecipe.matches(craftSlots, player.level) ? lastRecipe : null;
		MixinHooks.hascachedrecipe = true;
	}
}