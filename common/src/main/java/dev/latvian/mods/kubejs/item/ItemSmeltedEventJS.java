package dev.latvian.mods.kubejs.item;

import dev.latvian.mods.kubejs.player.PlayerEventJS;
import dev.latvian.mods.kubejs.typings.JsInfo;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@JsInfo("""
		Invoked when an item is smelted by a player.
		""")
public class ItemSmeltedEventJS extends PlayerEventJS {
	private final Player player;
	private final ItemStack smelted;

	public ItemSmeltedEventJS(Player player, ItemStack smelted) {
		this.player = player;
		this.smelted = smelted;
	}

	@Override
	@JsInfo("The player that smelted the item.")
	public Player getEntity() {
		return player;
	}

	@JsInfo("The item that was smelted.")
	public ItemStack getItem() {
		return smelted;
	}
}