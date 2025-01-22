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

import java.util.*;
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
        for(Iterator<ItemStack> stackIt = stacks.iterator(); stackIt.hasNext(); base += getStackMultiplier(itemStack, maxCount) * itemStack.getCount()) {
            itemStack = stackIt.next();
        }
        return base;
    }

    static int getStackMultiplier(ItemStack stack, int maxCount) {
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
        if (!(o instanceof AdjustableBundleComponent bundleComponent)) return false;
        if (this == o) return true;
        if (this.maxCount != bundleComponent.maxCount) return false;
        if (this.capacity != bundleComponent.capacity()) return false;
        if (this.size() != bundleComponent.size()) return false;
        return ItemStack.stacksEqual(this.stacks, bundleComponent.stacks);
    }

    public int hashCode() {
        int total = 31;
        for(ItemStack stack : this.stacks) {
            total += stack.getItem().hashCode();
            total = 31 * total + stack.getComponents().hashCode();
            total = 31 * total + stack.getCount();
        }
        return total;
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

        private int getStackMatchIndex(ItemStack stack, int count) {
            if (stack.isStackable()) {
                for (int i = count + 1; i < this.stacks.size(); ++i) {
                    if (ItemStack.areItemsAndComponentsEqual(this.stacks.get(i), stack) && this.stacks.get(i).getCount() < 64) {
                        return i;
                    }
                }
            }
            return -1;
        }

        private int getAvailableCapacity(ItemStack stack) {
            int base = this.maxCount - this.capacity;
            return Math.max(base / getStackMultiplier(stack, this.maxCount), 0);
        }

        public int add(Slot slot, PlayerEntity player) {
            ItemStack itemStack = slot.getStack();
            int i = this.getAvailableCapacity(itemStack);
            return this.add(slot.takeStackRange(itemStack.getCount(), i, player));
        }

        public int add(ItemStack stack) {
            if (stack.isEmpty() || !stack.getItem().canBeNested()) return 0;
            final int ADD_ALLOWED = Math.min(stack.getCount(), this.getAvailableCapacity(stack));
            if (ADD_ALLOWED == 0) return 0;
            int remainingToAdd = ADD_ALLOWED;
            this.capacity += remainingToAdd * getStackMultiplier(stack, this.maxCount);
            int index = -1;
            while(remainingToAdd > 0) {
                index = getStackMatchIndex(stack, index);
                if(index == -1) {
                    this.stacks.addLast(stack.split(remainingToAdd));
                    break;
                }
                if(remainingToAdd + this.stacks.get(index).getCount() <= stack.getMaxCount()) {
                    this.stacks.get(index).increment(remainingToAdd);
                    stack.decrement(remainingToAdd);
                    break;
                } else {
                    int k = stack.getMaxCount() - this.stacks.get(index).getCount();
                    this.stacks.get(index).increment(k);
                    stack.decrement(k);
                    remainingToAdd -= k;
                }
            }
            return ADD_ALLOWED;
        }

        public void decrementAt(int index) {
            if (this.stacks.isEmpty()) return;
            this.capacity -= getStackMultiplier(this.stacks.get(index), this.maxCount);
            if(this.stacks.get(index).getCount() <= 1) {
                this.stacks.remove(index);
            } else {
                this.stacks.get(index).decrement(1);
            }
        }

        @Nullable
        public ItemStack removeFirst() {
            if (this.stacks.isEmpty()) return null;
            ItemStack itemStack = (this.stacks.removeFirst()).copy();
            this.capacity -= getStackMultiplier(itemStack, this.maxCount) * itemStack.getCount();
            return itemStack;
        }

        public AdjustableBundleComponent build() {
            return new AdjustableBundleComponent(List.copyOf(this.stacks), this.capacity, this.maxCount);
        }
    }
}
