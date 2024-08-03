package mod.ginleaf.quiver.item.tooltip;

import mod.ginleaf.quiver.component.AdjustableBundleComponent;
import net.minecraft.item.tooltip.TooltipData;

public record QuiverTooltipData(AdjustableBundleComponent contents) implements TooltipData {

    public AdjustableBundleComponent contents() {
        return this.contents;
    }
}
