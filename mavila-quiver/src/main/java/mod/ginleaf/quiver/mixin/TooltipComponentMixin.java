package mod.ginleaf.quiver.mixin;

import mod.ginleaf.quiver.gui.tooltip.QuiverTooltipComponent;
import mod.ginleaf.quiver.item.tooltip.QuiverTooltipData;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.tooltip.TooltipData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.gui.tooltip.TooltipComponent")
public interface TooltipComponentMixin {
    @Inject(at = @At("HEAD"), method = "of(Lnet/minecraft/item/tooltip/TooltipData;)Lnet/minecraft/client/gui/tooltip/TooltipComponent;", cancellable = true)
    private static void of(TooltipData data, CallbackInfoReturnable<TooltipComponent> cir) {
        if (data instanceof QuiverTooltipData quiverTooltipData) {
            cir.setReturnValue(new QuiverTooltipComponent(quiverTooltipData.contents()));
        }
    }

}
