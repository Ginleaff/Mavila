package mod.ginleaf.quiver.mixin;

import mod.ginleaf.quiver.MavilaQuiver;
import mod.ginleaf.quiver.component.AdjustableBundleComponent;
import mod.ginleaf.quiver.item.QuiverItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Predicate;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow @Final
    PlayerInventory inventory;

    @Shadow @Final private PlayerAbilities abilities;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    @Override
    public ItemStack getProjectileType(ItemStack stack) {
        if (!(stack.getItem() instanceof RangedWeaponItem)) {
            return ItemStack.EMPTY;
        } else {
            Predicate<ItemStack> predicate = ((RangedWeaponItem)stack.getItem()).getHeldProjectiles();
            ItemStack itemStack = RangedWeaponItem.getHeldProjectile(this, predicate);
            if (!itemStack.isEmpty()) {
                return itemStack;
            } else {
                predicate = ((RangedWeaponItem)stack.getItem()).getProjectiles();

                for(int i = 0; i < this.inventory.size(); ++i) {
                    ItemStack itemStack2 = this.inventory.getStack(i);
                    if(itemStack2.getItem() instanceof QuiverItem) {
                        AdjustableBundleComponent quiverContents = itemStack2.getOrDefault(MavilaQuiver.ADJUSTABLE_BUNDLE_CONTENTS, QuiverItem.QUIVER_DEFAULT);
                        if(quiverContents.isEmpty()) continue;
                    }
                    if (predicate.test(itemStack2)) {
                        return itemStack2;
                    }
                }
                return this.abilities.creativeMode ? new ItemStack(Items.ARROW) : ItemStack.EMPTY;
            }
        }
    }
}
