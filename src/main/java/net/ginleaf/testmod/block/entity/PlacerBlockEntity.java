package net.ginleaf.testmod.block.entity;
import net.ginleaf.testmod.TestMod;
import net.ginleaf.testmod.screen.PlacerScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;


public class PlacerBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, Inventory {

    public static final int INVENTORY_SIZE = 3;

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);

    public PlacerBlockEntity(BlockPos pos, BlockState state) {
        super(TestMod.PLACER_BLOCK_ENTITY_TYPE, pos, state);
    }

    public int chooseNonEmptySlot(Random random) {
        int i = -1;
        int j = 1;
        int d = -1;
        for (int k = 0; k < INVENTORY_SIZE; ++k) {
            if (this.inventory.get(k).isEmpty() || !(this.inventory.get(k).getItem() instanceof BlockItem)) continue;
            d = k;
            if (random.nextInt(j++) != 0) continue;
            i = k;
        }
        if(i == -1) {
            return d;
        }
        return i;
    }

    @Override
    public Text getDisplayName() {
        if(TestMod.DEBUG) {
            TestMod.LOGGER.info("PlacerBlockEntity.getDisplayName() ran!");
        }
        return Text.translatable("container.placer");
    }

    @Override
    public int size() {
        return 3;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < size(); i++) {
            ItemStack stack = getStack(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int count) {
        ItemStack result = Inventories.splitStack(this.inventory, slot, count);
        if (!result.isEmpty()) {
            markDirty();
        }
        return result;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        if (stack.getCount() > stack.getMaxCount()) {
            stack.setCount(stack.getMaxCount());
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if(TestMod.DEBUG) {
            TestMod.LOGGER.info("PlacerBlockEntity.canPlayerUse() ran!");
        }
        return true;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        if(TestMod.DEBUG) {
            TestMod.LOGGER.info("PlacerBlockEntity.createMenu) ran!");
        }
        //We provide *this* to the screenHandler as our class Implements Inventory
        //Only the Server has the Inventory at the start, this will be synced to the client in the ScreenHandler
        return new PlacerScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, this.inventory, registryLookup);
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, this.inventory, registryLookup);
    }
}