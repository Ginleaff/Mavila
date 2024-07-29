package mod.ginleaf.quiver.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;


public class QuiverItem extends BundleItem implements ProjectileItem {

    public QuiverItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (!(clickType == ClickType.RIGHT && slot.canTakePartial(player) && isQuiverInteractable(otherStack))) return false;
        BundleContentsComponent bundleContentsComponent = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContentsComponent == null) return false;
        BundleContentsComponent.Builder builder = new BundleContentsComponent.Builder(bundleContentsComponent);
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
        stack.set(DataComponentTypes.BUNDLE_CONTENTS, builder.build());
        return true;
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT) return false;
        BundleContentsComponent bundleContentsComponent = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContentsComponent == null) return false;
        ItemStack itemStack = slot.getStack();
        BundleContentsComponent.Builder builder = new BundleContentsComponent.Builder(bundleContentsComponent);
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
        stack.set(DataComponentTypes.BUNDLE_CONTENTS, builder.build());
        return true;

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

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        return null;
    }
}
