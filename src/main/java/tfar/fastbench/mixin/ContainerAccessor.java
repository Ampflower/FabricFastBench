/*
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
 */
// This file has been traced to be sourced from `net.minecraft.inventory.container.DumbShitTM`

package tfar.fastbench.mixin;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractContainerMenu.class)
public interface ContainerAccessor {
	@Invoker("moveItemStackTo")
	boolean insert(ItemStack stack, int startIndex, int endIndex, boolean fromLast);

}
