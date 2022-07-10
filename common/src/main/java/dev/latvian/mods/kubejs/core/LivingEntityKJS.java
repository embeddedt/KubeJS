package dev.latvian.mods.kubejs.core;

import dev.latvian.mods.kubejs.bindings.ItemWrapper;
import dev.latvian.mods.kubejs.item.ItemFoodEatenEventJS;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/**
 * @author LatvianModder
 */
public interface LivingEntityKJS {
	default void foodEatenKJS(ItemStack is) {
		if (this instanceof ServerPlayer player) {
			var event = new ItemFoodEatenEventJS(player, is);
			var i = is.getItem();

			var b = i.getItemBuilderKJS();
			if (b != null && b.foodBuilder != null && b.foodBuilder.eaten != null) {
				b.foodBuilder.eaten.accept(event);
			}

			ItemFoodEatenEventJS.EVENT.post(event, String.valueOf(ItemWrapper.getId(i)));
		}
	}
}