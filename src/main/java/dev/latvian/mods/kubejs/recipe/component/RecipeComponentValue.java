package dev.latvian.mods.kubejs.recipe.component;

import dev.latvian.mods.kubejs.recipe.InputReplacement;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.OutputReplacement;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.util.WrappedJS;
import dev.latvian.mods.rhino.Context;

import java.util.Map;
import java.util.Objects;

public final class RecipeComponentValue<T> implements WrappedJS, Map.Entry<RecipeKey<T>, T> {
	public static final RecipeComponentValue<?>[] EMPTY_ARRAY = new RecipeComponentValue[0];

	public final RecipeKey<T> key;
	public final int index;
	public T value;
	public boolean write;

	public RecipeComponentValue(RecipeKey<T> key, int index) {
		this.key = key;
		this.index = index;
		this.value = null;
		this.write = false;
	}

	public RecipeComponentValue<T> copy() {
		var copy = new RecipeComponentValue<>(key, index);
		copy.value = value;
		copy.write = write;
		return copy;
	}

	public boolean matches(KubeRecipe recipe, ReplacementMatch match) {
		return value != null && key.role.isInput() && key.component.matches(recipe, value, match);
	}

	public boolean replaceInput(Context cx, KubeRecipe recipe, ReplacementMatch match, InputReplacement with) {
		if (!key.role.isInput()) {
			return false;
		}

		var newValue = value == null ? null : key.component.replaceInput(cx, recipe, value, match, with);

		if (key.component.checkValueHasChanged(value, newValue)) {
			value = newValue;
			write();
			return true;
		}

		return false;
	}

	public boolean replaceOutput(Context cx, KubeRecipe recipe, ReplacementMatch match, OutputReplacement with) {
		if (!key.role.isOutput()) {
			return false;
		}

		var newValue = value == null ? null : key.component.replaceOutput(cx, recipe, value, match, with);

		if (key.component.checkValueHasChanged(value, newValue)) {
			value = newValue;
			write();
			return true;
		}

		return false;
	}

	@Override
	public RecipeKey<T> getKey() {
		return key;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public T setValue(T newValue) {
		var v = value;
		value = newValue;
		return v;
	}

	public boolean shouldWrite() {
		return write;
	}

	public void write() {
		write = true;
	}

	@Override
	public String toString() {
		return "%s = %s".formatted(key.name, value);
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this || obj instanceof Map.Entry<?, ?> e && key == e.getKey() && Objects.equals(value, e.getValue());
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, value);
	}

	public String checkEmpty() {
		return key.allowEmpty ? "" : value != null ? key.component.checkEmpty(key, value) : key.optional() ? "" : ("Value of '" + key.name + "' can't be null!");
	}
}
