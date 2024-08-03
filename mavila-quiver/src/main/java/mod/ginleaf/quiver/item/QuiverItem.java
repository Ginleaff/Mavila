package mod.ginleaf.quiver.item;

import mod.ginleaf.quiver.MavilaQuiver;
import mod.ginleaf.quiver.component.AdjustableBundleComponent;
import mod.ginleaf.quiver.item.tooltip.QuiverTooltipData;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class QuiverItem extends BundleItem {
    public static final AdjustableBundleComponent QUIVER_DEFAULT = new AdjustableBundleComponent(List.of(), 256);

    public QuiverItem(Item.Settings settings) {
        super(settings);
    }

    public static int getQuiverCapacity(ItemStack stack) {
        AdjustableBundleComponent quiverContents = stack.getOrDefault(MavilaQuiver.ADJUSTABLE_BUNDLE_CONTENTS, QUIVER_DEFAULT);
        return quiverContents.getCapacity();
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (!(clickType == ClickType.RIGHT && slot.canTakePartial(player) && isQuiverInteractable(otherStack))) return false;
        AdjustableBundleComponent quiverContents = stack.get(MavilaQuiver.ADJUSTABLE_BUNDLE_CONTENTS);
        if (quiverContents == null) return false;
        AdjustableBundleComponent.Builder builder = new AdjustableBundleComponent.Builder(quiverContents);
        if (otherStack.isEmpty()) {
            ItemStack itemStack = builder.removeFirst();
            if (itemStack != null) {

                this.playRemoveOneSound(player);
                cursorStackReference.set(itemStack);
            }
        } else {
            int i = builder.add(otherStack);
            if (i > 0) {
                this.playInsertSound(player);
            }
        }
        stack.set(MavilaQuiver.ADJUSTABLE_BUNDLE_CONTENTS, builder.build());
        return true;
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT) return false;
        AdjustableBundleComponent quiverContents = stack.get(MavilaQuiver.ADJUSTABLE_BUNDLE_CONTENTS);
        if (quiverContents == null) return false;
        ItemStack itemStack = slot.getStack();
        AdjustableBundleComponent.Builder builder = new AdjustableBundleComponent.Builder(quiverContents);
        if (itemStack.isEmpty()) {
            this.playRemoveOneSound(player);
            ItemStack itemStack2 = builder.removeFirst();
            if (itemStack2 != null) {
                ItemStack itemStack3 = slot.insertStack(itemStack2);
                builder.add(itemStack3);
            }
        } else if (itemStack.getItem().canBeNested() && isQuiverInteractable(itemStack)) {
            int i = builder.add(slot, player);
            if (i > 0) {
                this.playInsertSound(player);
            }
        }
        stack.set(MavilaQuiver.ADJUSTABLE_BUNDLE_CONTENTS, builder.build());
        return true;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (dropAllBundledItems(itemStack, user)) {
            this.playDropContentsSound(user);
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            return TypedActionResult.success(itemStack, world.isClient());
        } else {
            return TypedActionResult.fail(itemStack);
        }
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return getQuiverCapacity(stack) > 0;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return Math.min(1 + ((getQuiverCapacity(stack)/256) * 12), 13);
    }

    private static boolean dropAllBundledItems(ItemStack stack, PlayerEntity player) {
        AdjustableBundleComponent quiverContents = stack.get(MavilaQuiver.ADJUSTABLE_BUNDLE_CONTENTS);
        if (quiverContents == null || quiverContents.isEmpty()) return false;
        stack.set(MavilaQuiver.ADJUSTABLE_BUNDLE_CONTENTS, QUIVER_DEFAULT);
        if (player instanceof ServerPlayerEntity) {
            quiverContents.iterateCopy().forEach((stackIt) -> player.dropItem(stackIt, true));
        }
        return true;
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return !stack.contains(DataComponentTypes.HIDE_TOOLTIP) && !stack.contains(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP)
                ? Optional.ofNullable(stack.get(MavilaQuiver.ADJUSTABLE_BUNDLE_CONTENTS)).map(QuiverTooltipData::new) : Optional.empty();
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        AdjustableBundleComponent quiverContents = stack.get(MavilaQuiver.ADJUSTABLE_BUNDLE_CONTENTS);
        if (quiverContents != null) {
            tooltip.add(Text.translatable("item.mavila_quiver.quiver.fullness", quiverContents.capacity(), 256).formatted(Formatting.GRAY));
        }

    }

    public void onItemEntityDestroyed(ItemEntity entity) {
        AdjustableBundleComponent quiverContents = entity.getStack().get(MavilaQuiver.ADJUSTABLE_BUNDLE_CONTENTS);
        if (quiverContents != null) {
            entity.getStack().set(MavilaQuiver.ADJUSTABLE_BUNDLE_CONTENTS, QUIVER_DEFAULT);
            ItemUsage.spawnItemContents(entity, quiverContents.iterateCopy());
        }
    }

    private boolean isQuiverInteractable(ItemStack itemStack) {
        return itemStack.isEmpty() || itemStack.getItem() instanceof ProjectileItem;
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }
}
