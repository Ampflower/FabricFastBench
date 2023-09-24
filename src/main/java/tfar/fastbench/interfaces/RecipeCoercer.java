package tfar.fastbench.interfaces;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

/**
 * Why support 1.19.4 out of band when you can curse your code?
 *
 * @author Ampflower
 * @since 4.0.1
 **/
public interface RecipeCoercer<C extends Container> {
    default ItemStack method_8116(C container) {
        return this.assemble(container);
    }

    ItemStack assemble(C container);

    default ItemStack method_8116(C container, RegistryAccess registryAccess) {
        return this.method_8116(container);
    }
}
