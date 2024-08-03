package com.reclizer.csgobox.utils;


import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class ItemNBT {
    public static ItemStack getStacks(String itemData) {
        return Objects.requireNonNull(ItemStack.CODEC.decode(JsonOps.INSTANCE, JsonParser.parseString(itemData)).result().orElse(null)).getFirst();
    }
}
