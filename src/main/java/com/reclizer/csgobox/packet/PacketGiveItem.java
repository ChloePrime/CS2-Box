package com.reclizer.csgobox.packet;

import com.reclizer.csgobox.item.ItemCsgoBox;
import com.reclizer.csgobox.utils.RandomItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

public class PacketGiveItem {

    private final long seed;

    public PacketGiveItem(FriendlyByteBuf buf) {
        seed = buf.readLong();
    }

    // Encoder
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(seed);
    }

    public PacketGiveItem(long seed) {
        this.seed = seed;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> Optional.ofNullable(ctx.get().getSender()).ifPresent(player -> {
            var box = player.getMainHandItem();
            if (!(box.getItem() instanceof ItemCsgoBox boxItem)) {
                return;
            }
            var itemList = boxItem.getItemGroup(box);
            var rng = new Random(seed);
            ITEM_BUFFER.clear();

            for (int i = 0; i < 46; i++) {
                int grade = RandomItem.randomItemsGrade(rng, ItemCsgoBox.getRandom(box), player);
                ItemStack itemStack = RandomItem.randomItems(rng, grade, itemList);
                ITEM_BUFFER.add(itemStack);
            }

            ItemStack giveItem = ITEM_BUFFER.get(45);
            ITEM_BUFFER.clear();

            if (giveItem != null) {
                var inventory = player.getInventory();
                int emptySlot = -1;
                for (int i = 0; i < 36; i++) {
                    if (inventory.getItem(i).isEmpty()) {
                        emptySlot = i;
                        break;
                    }
                }
                if (emptySlot != -1) {
                    player.getInventory().add(giveItem);
                } else {
                    player.drop(giveItem, true, false);
                }
            }
        }));
        ctx.get().setPacketHandled(true);
    }

    private static final List<ItemStack> ITEM_BUFFER = new ArrayList<>(50);
}
