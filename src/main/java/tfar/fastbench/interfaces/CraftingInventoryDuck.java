/*
 * Copyright (c) 2022-2023 Ampflower
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
 *
 * Contributions from Ampflower may additionally be available under CC0-1.0,
 * as part of the pull-request for upstreaming to FabricFastBench.
 * If a copy was not distributed with this file, you can obtain one at
 * https://github.com/Modflower/QuickBench/blob/trunk/LICENSE-CC0
 *
 * Additional details are outlined in LICENSE.md, which you can obtain at
 * https://github.com/Modflower/QuickBench/blob/trunk/LICENSE.md
 */

package tfar.fastbench.interfaces;

public interface CraftingInventoryDuck {
	void setCheckMatrixChanges(boolean checkMatrixChanges);

	boolean getCheckMatrixChanges();
}
