/*
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
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TransientCraftingContainer.class)
public interface CraftingInventoryAccessor {
	@Accessor
	NonNullList<ItemStack> getItems();
}
