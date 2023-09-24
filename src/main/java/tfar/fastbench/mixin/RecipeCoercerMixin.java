package tfar.fastbench.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import tfar.fastbench.interfaces.RecipeCoercer;

/**
 * For 1.19.4 compatibility, the cursed way.
 *
 * @author Ampflower
 * @since 4.0.1
 **/
@Mixin(Recipe.class)
public interface RecipeCoercerMixin<C extends Container> extends RecipeCoercer<C> {
}
