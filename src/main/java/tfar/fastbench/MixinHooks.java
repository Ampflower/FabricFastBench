/*
 * Copyright (c) 2022-2024 Ampflower
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
// This file has been traced to be sourced from `shadows.fastbench.util.FastBenchUtil`.

package tfar.fastbench;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import tfar.fastbench.interfaces.CraftingInventoryDuck;
import tfar.fastbench.mixin.ContainerAccessor;
import tfar.fastbench.quickbench.internal.Reflector;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.Collections;

public final class MixinHooks {
	private static final MethodHandle recipe$assemble = Reflector.virtual(Recipe.class, "method_8116",
			MethodType.methodType(ItemStack.class, CraftingInput.class, RegistryAccess.class));

	public static boolean hascachedrecipe = false;

	public static Recipe<CraftingInput> lastRecipe;

	public static void slotChangedCraftingGrid(Level level, CraftingContainer container, ResultContainer result) {
		if (!level.isClientSide) {
			CraftingInput input = container.asCraftInput();
			ItemStack itemstack = ItemStack.EMPTY;

			RecipeHolder<CraftingRecipe> recipe = coerce(result.getRecipeUsed());
			if (recipe == null || !recipe.value().matches(input, level)) {
				recipe = findRecipe(input, level);
			}

			if (recipe != null) {
				try {
					itemstack = (ItemStack) recipe$assemble.invoke(recipe.value(), input, level.registryAccess());
				} catch (Throwable t) {
					throw new AssertionError(t);
				}
			}

			result.setItem(0, itemstack);

			result.setRecipeUsed(recipe);
		}
	}

	public static ItemStack handleShiftCraft(
			Player player, AbstractContainerMenu menu, Slot resultSlot, CraftingContainer container,
			ResultContainer craftResult, int outStart, int outEnd) {
		ItemStack outputCopy = ItemStack.EMPTY;
		CraftingInput input = container.asCraftInput();
		CraftingInventoryDuck duck = (CraftingInventoryDuck) container;
		duck.setCheckMatrixChanges(false);
		RecipeHolder<CraftingRecipe> recipeHolder = coerce(craftResult.getRecipeUsed());

		if (recipeHolder != null && resultSlot != null && resultSlot.hasItem()) {
			final Recipe<CraftingInput> recipe = recipeHolder.value();
			while (recipe.matches(input, player.level())) {
				ItemStack recipeOutput = resultSlot.getItem().copy();
				outputCopy = recipeOutput.copy();

				recipeOutput.getItem().onCraftedBy(recipeOutput, player.level(), player);

				if (!player.level().isClientSide && !((ContainerAccessor) menu).insert(recipeOutput, outStart, outEnd, true)) {
					duck.setCheckMatrixChanges(true);
					return ItemStack.EMPTY;
				}

				resultSlot.onQuickCraft(recipeOutput, outputCopy);
				resultSlot.setChanged();

				if (!player.level().isClientSide && recipeOutput.getCount() == outputCopy.getCount()) {
					duck.setCheckMatrixChanges(true);
					return ItemStack.EMPTY;
				}

				resultSlot.onTake(player, recipeOutput);

				//player.drop(resultSlot.getItem(), false);
			}
			duck.setCheckMatrixChanges(true);
			slotChangedCraftingGrid(player.level(), container, craftResult);

			// Award the player the recipe for using it. Mimics vanilla behaviour.
			if (!recipe.isSpecial()) {
				player.awardRecipes(Collections.singleton(recipeHolder));
			}
		}
		duck.setCheckMatrixChanges(true);
		return recipeHolder == null ? ItemStack.EMPTY : outputCopy;
	}

	public static RecipeHolder<CraftingRecipe> findRecipe(CraftingInput inv, Level level) {
		return level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, inv, level).orElse(null);
	}

	@SuppressWarnings("unchecked")
	public static <I extends RecipeInput, T extends Recipe<I>> RecipeHolder<T> coerce(RecipeHolder<?> in) {
		return (RecipeHolder<T>) in;
	}

	public static NonNullList<ItemStack> copy(RecipeInput recipeInput) {
		final var list = NonNullList.withSize(recipeInput.size(), ItemStack.EMPTY);
		for (int i = 0; i < recipeInput.size(); i++) {
			list.set(i, recipeInput.getItem(i));
		}
		return list;
	}
}
