package mod.ginleaf.quiver.item;

import mod.ginleaf.quiver.MavilaQuiver;
import mod.ginleaf.quiver.component.AdjustableBundleComponent;
import mod.ginleaf.quiver.item.tooltip.QuiverTooltipData;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QuiverItem extends Item {
    public static final AdjustableBundleComponent QUIVER_DEFAULT = new AdjustableBundleComponent(List.of(), 256);

    public QuiverItem(Settings settings) {
        super(settings);
    }

    public static double getQuiverCapacity(ItemStack stack) {
        AdjustableBundleComponent quiverContents = stack.getOrDefault(MavilaQuiver.ADJUSTABLE_BUNDLE_CONTENTS, QUIVER_DEFAULT);
        return quiverContents.getCapacity();
    }

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

    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT) return false;
        AdjustableBundleComponent quiverContents = stack.get(MavilaQuiver.ADJUSTABLE_BUNDLE_CONTENTS);
        if (quiverContents == null) return false;
        ItemStack itemStack = slot.getStack();
        AdjustableBundleComponent.Builder builder = new AdjustableBundleComponent.Builder(quiverContents);
        if (itemStack.isEmpty()) {
            this.playRemoveOneSound(player);
            ItemStack removedStack = builder.removeFirst();
            if (removedStack != null) {
                removedStack = slot.insertStack(removedStack);
                builder.add(removedStack);
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

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if(hand.equals(Hand.OFF_HAND)) return TypedActionResult.fail(itemStack);
        if (dropAllBundledItems(itemStack, user)) {
            this.playDropContentsSound(user);
            return TypedActionResult.success(itemStack, world.isClient());
        } else {
            return TypedActionResult.fail(itemStack);
        }
    }

    public boolean isItemBarVisible(ItemStack stack) {
        return getQuiverCapacity(stack) > 0;
    }

    public int getItemBarStep(ItemStack stack) {
        return (int) Math.min(getQuiverCapacity(stack)/256 * 12 + 1, 13);
    }

    public int getItemBarColor(ItemStack stack) {
        return MathHelper.packRgb(0.4F, 0.4F, 1.0F);
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

    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return !stack.contains(DataComponentTypes.HIDE_TOOLTIP) && !stack.contains(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP)
                ? Optional.ofNullable(stack.get(MavilaQuiver.ADJUSTABLE_BUNDLE_CONTENTS)).map(QuiverTooltipData::new) : Optional.empty();
    }

    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
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

    public static ItemStack getArrow(ItemStack quiverStack, LivingEntity shooter, boolean isInfinity, boolean isCreative) {
        AdjustableBundleComponent quiverContents = quiverStack.getOrDefault(MavilaQuiver.ADJUSTABLE_BUNDLE_CONTENTS, QuiverItem.QUIVER_DEFAULT);
        AdjustableBundleComponent.Builder builder = new AdjustableBundleComponent.Builder(quiverContents);
        Random random = new Random();
        int index = random.nextInt(quiverContents.size());
        ItemStack arrowStack = quiverContents.get(index).copyWithCount(1);
        if(isInfinity && quiverContents.get(index).getItem().equals(Items.ARROW)) {
            arrowStack.set(DataComponentTypes.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
        } else {
            if(!isCreative) builder.decrementAt(index);
        }
        quiverStack.set(MavilaQuiver.ADJUSTABLE_BUNDLE_CONTENTS, builder.build());

        return arrowStack;
    }

    private boolean isQuiverInteractable(ItemStack itemStack) {
        return itemStack.isEmpty() || itemStack.getItem() instanceof ArrowItem || itemStack.getItem() instanceof SnowballItem;
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
