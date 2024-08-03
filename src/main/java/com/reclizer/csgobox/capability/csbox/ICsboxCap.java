package com.reclizer.csgobox.capability.csbox;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;

public interface ICsboxCap extends INBTSerializable<CompoundTag> {

    long playerSeed();

    long setSeed(final long seed);

    int mode();

    int setMode(final int mode);

    ItemStack setItem(final ItemStack item);

    ItemStack getItem();

    int getGrade();

    int setGrade(final int grade);


}
