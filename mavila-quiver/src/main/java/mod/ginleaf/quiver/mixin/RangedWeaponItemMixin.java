package mod.ginleaf.quiver.mixin;

import mod.ginleaf.quiver.MavilaQuiver;
import mod.ginleaf.quiver.item.QuiverItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(RangedWeaponItem.class)
public abstract class RangedWeaponItemMixin extends Item {
    public RangedWeaponItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "getProjectile", at = @At("HEAD"), cancellable = true)
    private static void getProjectileInject(ItemStack stack, ItemStack projectileStack, LivingEntity shooter, boolean multishot, CallbackInfoReturnable<ItemStack> cir) {
        if(projectileStack.getItem() instanceof QuiverItem) {
            ItemStack itemStack = QuiverItem.getQuiverAmmo(projectileStack);
            if (!itemStack.isEmpty()) {
                if(shooter.isPlayer()) {
                    Objects.requireNonNull(shooter.getWorld().getPlayerByUuid(shooter.getUuid())).incrementStat(MavilaQuiver.INSPECT_QUIVER_ITEM);
                }
                cir.setReturnValue(itemStack);
            }
        }
    }
}
