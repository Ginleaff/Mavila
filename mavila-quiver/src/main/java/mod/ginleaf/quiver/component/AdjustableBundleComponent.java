package mod.ginleaf.quiver.component;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.ginleaf.quiver.MavilaQuiver;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public record AdjustableBundleComponent(List<ItemStack> stacks, int capacity, int maxCount) implements TooltipData {
    public static final Codec<AdjustableBundleComponent> CODEC;
    public static final PacketCodec<RegistryByteBuf, AdjustableBundleComponent> PACKET_CODEC;

    public AdjustableBundleComponent(List<ItemStack> stacks, int maxCount) {
        this(stacks, calculateCapacity(stacks,maxCount), maxCount);
    }

    private static int calculateCapacity(List<ItemStack> stacks, int maxCount) {
        int base = 0;
        ItemStack itemStack;
        for(Iterator<ItemStack> stackIt = stacks.iterator(); stackIt.hasNext(); base += getStackCapacity(itemStack, maxCount) * itemStack.getCount()) {
            itemStack = stackIt.next();
        }

        return base;
    }

    static int getStackCapacity(ItemStack stack, int maxCount) {
        if(stack.getItem().getComponents().contains(DataComponentTypes.BUNDLE_CONTENTS)) {
            BundleContentsComponent basicBundleContents = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
            if(basicBundleContents != null) {
                return calculateCapacity(basicBundleContents.stream().toList(), maxCount) + 4;
            }
        }
        if(stack.getItem().getComponents().contains(MavilaQuiver.ADJUSTABLE_BUNDLE_CONTENTS)) {
            AdjustableBundleComponent adjustableBundleComponents = stack.get(MavilaQuiver.ADJUSTABLE_BUNDLE_CONTENTS);
            if(adjustableBundleComponents != null) {
                return adjustableBundleComponents.capacity + 4;
            }
        }
        List<BeehiveBlockEntity.BeeData> list = stack.getOrDefault(DataComponentTypes.BEES, List.of());
        return !list.isEmpty() ? Item.DEFAULT_MAX_COUNT : Item.DEFAULT_MAX_COUNT/stack.getMaxCount();
    }

    public ItemStack get(int index) {
        return this.stacks.get(index);
    }

    public Stream<ItemStack> stream() {
        return this.stacks.stream().map(ItemStack::copy);
    }

    public Iterable<ItemStack> iterate() {
        return this.stacks;
    }

    public Iterable<ItemStack> iterateCopy() {
        return Lists.transform(this.stacks, ItemStack::copy);
    }

    public int size() {
        return this.stacks.size();
    }

    public int getCapacity() {
        return this.capacity;
    }

    public boolean isEmpty() {
        return this.stacks.isEmpty();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdjustableBundleComponent bundleComponent)) return false;
        return this.capacity == bundleComponent.capacity && ItemStack.stacksEqual(this.stacks, bundleComponent.stacks);
    }

    public int hashCode() {
        return ItemStack.listHashCode(this.stacks);
    }

    public String toString() {
        return "AdjustableBundleContents" + this.stacks + this.capacity;
    }

    static {
        CODEC = RecordCodecBuilder.create(i -> i.apply2(
                AdjustableBundleComponent::new,
                ItemStack.CODEC.listOf().fieldOf("stacks").forGetter(AdjustableBundleComponent::stacks),
                Codec.INT.fieldOf("maxCount").forGetter(AdjustableBundleComponent::maxCount)
        ));
        PACKET_CODEC = PacketCodec.tuple(
                ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()), AdjustableBundleComponent::stacks,
                PacketCodecs.INTEGER, AdjustableBundleComponent::maxCount,
                AdjustableBundleComponent::new
        );
    }

    public static class Builder {
        private final List<ItemStack> stacks;
        private int capacity;
        private final int maxCount;

        public Builder(AdjustableBundleComponent base) {
            this.stacks = new ArrayList<>(base.stacks);
            this.capacity = base.capacity;
            this.maxCount = base.maxCount;
        }

        public AdjustableBundleComponent.Builder clear() {
            this.stacks.clear();
            this.capacity = 0;
            return this;
        }

        private int getStackMatchIndex(ItemStack stack) {
            if (stack.isStackable()) {
                for (int i = 0; i < this.stacks.size(); ++i) {
                    if (ItemStack.areItemsAndComponentsEqual(this.stacks.get(i), stack) && this.stacks.get(i).getCount() < 64) {
                        return i;
                    }
                }
            }
            return -1;
        }

        private int getMaxAllowed(ItemStack stack, int maxCount) {
            int base = maxCount - this.capacity + 1;
            return Math.max(base - AdjustableBundleComponent.getStackCapacity(stack, this.maxCount), 0);
        }

        public int addLast(ItemStack stack) {
            if (stack.isEmpty() || !stack.getItem().canBeNested()) return 0;
            int i = Math.min(stack.getCount(), this.getMaxAllowed(stack, this.maxCount));
            if (i == 0) return 0;
            int j = this.getStackMatchIndex(stack);
            if(j != -1) {
                if(i + this.stacks.get(j).getCount() > Item.DEFAULT_MAX_COUNT) { //65+ ItemStack fix
                    ItemStack stackRemoved = this.stacks.remove(j);
                    int k = Item.DEFAULT_MAX_COUNT - ((i + stackRemoved.getCount()) % Item.DEFAULT_MAX_COUNT);
                    this.stacks.add(j,stackRemoved.copyWithCount(Item.DEFAULT_MAX_COUNT));
                    stack.decrement(k);
                    this.capacity += k;
                    return addLast(stack);
                }
                this.capacity += AdjustableBundleComponent.getStackCapacity(stack, this.maxCount) * i;
                ItemStack stackRemoved = this.stacks.remove(j);
                ItemStack stacksCombined = stackRemoved.copyWithCount(stackRemoved.getCount() + i );
                stack.decrement(i);
                this.stacks.add(j,stacksCombined);
            } else {
                this.capacity += AdjustableBundleComponent.getStackCapacity(stack, this.maxCount) * i;
                this.stacks.addLast(stack.split(i));
            }
            return i;
        }

        public int addLast(Slot slot, PlayerEntity player) {
            ItemStack itemStack = slot.getStack();
            int i = this.getMaxAllowed(itemStack, this.maxCount);
            return this.addLast(slot.takeStackRange(itemStack.getCount(), i, player));
        }

        public void decrementFirst(int amount) {
            if (this.stacks.isEmpty()) return;
            int count = this.stacks.getFirst().getCount();
            if(count - amount < 1) {
                this.stacks.removeFirst();
            } else {
                this.stacks.getFirst().setCount(count-amount);
            }
            this.capacity -= amount;
        }

        @Nullable
        public ItemStack removeFirst() {
            if (this.stacks.isEmpty()) return null;
            ItemStack itemStack = (this.stacks.removeFirst()).copy();
            this.capacity -= AdjustableBundleComponent.getStackCapacity(itemStack, this.maxCount) * itemStack.getCount();
            return itemStack;
        }

        public int getCapacity() {
            return this.capacity;
        }

        public AdjustableBundleComponent build() {
            return new AdjustableBundleComponent(List.copyOf(this.stacks), this.capacity, this.maxCount);
        }
    }
}
