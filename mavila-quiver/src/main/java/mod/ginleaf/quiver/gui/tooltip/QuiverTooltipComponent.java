package mod.ginleaf.quiver.gui.tooltip;

import mod.ginleaf.quiver.component.AdjustableBundleComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class QuiverTooltipComponent implements TooltipComponent {
    private static final Identifier BACKGROUND_TEXTURE = Identifier.ofVanilla("container/bundle/background");
    private final AdjustableBundleComponent quiverContents;

    public QuiverTooltipComponent(AdjustableBundleComponent quiverContents) {
        this.quiverContents = quiverContents;
    }

    @Override
    public int getHeight() {
        return this.getRowsHeight() + 4;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return this.getColumnsWidth();
    }

    private int getColumnsWidth() {
        return this.getColumns() * 18 + 2;
    }

    private int getRowsHeight() {
        return this.getRows() * 20 + 2;
    }

    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        int i = this.getColumns();
        int j = this.getRows();
        context.drawGuiTexture(BACKGROUND_TEXTURE, x, y, this.getColumnsWidth(), this.getRowsHeight());
        boolean bl = this.quiverContents.getCapacity() > 0;
        int k = 0;

        for(int l = 0; l < j; ++l) {
            for(int m = 0; m < i; ++m) {
                int n = x + m * 18 + 1;
                int o = y + l * 20 + 1;
                this.drawSlot(n, o, k++, bl, context, textRenderer);
            }
        }
    }

    private void drawSlot(int x, int y, int index, boolean shouldBlock, DrawContext context, TextRenderer textRenderer) {
        if (index >= this.quiverContents.size()) {
            this.draw(context, x, y, shouldBlock ? QuiverTooltipComponent.SlotSprite.BLOCKED_SLOT : QuiverTooltipComponent.SlotSprite.SLOT);
        } else {
            ItemStack itemStack = this.quiverContents.get(index);
            this.draw(context, x, y, QuiverTooltipComponent.SlotSprite.SLOT);
            context.drawItem(itemStack, x + 1, y + 1, index);
            context.drawItemInSlot(textRenderer, itemStack, x + 1, y + 1);
            if (index == 0) {
                HandledScreen.drawSlotHighlight(context, x + 1, y + 1, 0);
            }

        }
    }

    private void draw(DrawContext context, int x, int y, QuiverTooltipComponent.SlotSprite sprite) {
        context.drawGuiTexture(sprite.texture, x, y, 0, sprite.width, sprite.height);
    }

    private int getColumns() {
        return Math.max(2, (int) Math.ceil(Math.sqrt((double) this.quiverContents.size() + 1.0)));
    }

    private int getRows() {
        return (int) Math.ceil(((double) this.quiverContents.size() + 1.0) / (double) this.getColumns());
    }

    @Environment(EnvType.CLIENT)
    private enum SlotSprite {
        BLOCKED_SLOT(Identifier.ofVanilla("container/bundle/blocked_slot"), 18, 20),
        SLOT(Identifier.ofVanilla("container/bundle/slot"), 18, 20);

        public final Identifier texture;
        public final int width;
        public final int height;

        SlotSprite(final Identifier texture, final int width, final int height) {
            this.texture = texture;
            this.width = width;
            this.height = height;
        }
    }
}
