package mod.ginleaf.quiver.mixin;

import mod.ginleaf.quiver.MavilaQuiver;
import mod.ginleaf.quiver.item.QuiverItem;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Predicate;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow @Final PlayerInventory inventory;
    @Shadow @Final private PlayerAbilities abilities;

    @Shadow public abstract void incrementStat(Identifier stat);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public ItemStack getProjectileType(ItemStack stack) {
        if (!(stack.getItem() instanceof RangedWeaponItem)) return ItemStack.EMPTY;
        Predicate<ItemStack> predicate = ((RangedWeaponItem) stack.getItem()).getHeldProjectiles();
        ItemStack itemStack = RangedWeaponItem.getHeldProjectile(this, predicate);
        if (!itemStack.isEmpty()) {
            return itemStack;
        }
        predicate = ((RangedWeaponItem) stack.getItem()).getProjectiles();
        for(int i = 0; i < this.inventory.size(); ++i) {
            ItemStack currentProjectile = this.inventory.getStack(i);
            if(currentProjectile.getItem() instanceof QuiverItem) {
                if(!(this.getWorld() instanceof ServerWorld world)) continue;
                boolean isAmmoOne = EnchantmentHelper.getAmmoUse(world, stack, new ItemStack(Items.SPECTRAL_ARROW, 64), 1) == 1;
                if(!isAmmoOne || currentProjectile.getOrDefault(MavilaQuiver.ADJUSTABLE_BUNDLE_CONTENTS, QuiverItem.QUIVER_DEFAULT).isEmpty()) continue;
                this.incrementStat(MavilaQuiver.INSPECT_QUIVER_ITEM);
                return currentProjectile;
            }
            if (predicate.test(currentProjectile)) {
                return currentProjectile;
            }
        }
        return this.abilities.creativeMode ? new ItemStack(Items.ARROW) : ItemStack.EMPTY;
    }
}
