package net.ginleaf.testmod.block.placer;

import net.ginleaf.testmod.item.PlacerItemPlacementContext;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.item.*;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;

public class PlacerPlacementBehavior extends FallibleItemDispenserBehavior {


    @Override
    protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        BlockPos placePos = pointer.pos().offset(pointer.state().get(Properties.FACING));
        Item item = stack.getItem();
        this.setSuccess(false);

        if (item instanceof BlockItem && isBlockItemMovable((BlockItem) item)) {
            placeBlock(pointer,stack,placePos);

        } else if (item instanceof BoatItem) {
            boolean hasChest = item.getDefaultStack().getRegistryEntry().isIn(ItemTags.CHEST_BOATS);
            /* Listen, I know this is super cursed.
            But an Interface/Hashset implementation will fail to support modded boats without interacting with my mod, which is no go.
            This should provide support, but if there's an incorrect ID, hell will break.
            I'll see about properly implementing this in the future. Don't write an issue about this. */
            String boatID = item.toString();
            if (boatID.contains("chest")) {
                boatID = boatID.substring(0,boatID.indexOf("chest")-1);
            } else {
                boatID = boatID.substring(0,boatID.indexOf("boat")-1);
            }
            placeBoat(pointer,stack,placePos,hasChest,BoatEntity.Type.getType(boatID));

        } else if (item instanceof BucketItem) {
            placeBucket(pointer,stack,placePos);

        } else if (item instanceof SpawnEggItem) {
            placeSpawnEgg(pointer,stack,placePos);
        }

        return stack;
    }

    private boolean isBlockItemMovable(BlockItem item) {
        Block itemBlock = item.getBlock();
        if (
                itemBlock.equals(Blocks.PISTON)
                || itemBlock.equals(Blocks.STICKY_PISTON)
                || itemBlock.equals(Blocks.BELL)
                || itemBlock.equals(Blocks.COMPARATOR)
                || itemBlock instanceof BannerBlock
                || itemBlock instanceof SignBlock
                || itemBlock instanceof SkullBlock
                || itemBlock instanceof BedBlock
        ) {
            return true;
        }
        if (itemBlock.getHardness() == -1.0f) {
            return false;
        }
        if (itemBlock.equals(Blocks.OBSIDIAN) || itemBlock.equals(Blocks.REINFORCED_DEEPSLATE) || itemBlock.equals(Blocks.CRYING_OBSIDIAN) || itemBlock.equals(Blocks.RESPAWN_ANCHOR)) {
            return false;
        }
        if (itemBlock.getStateManager().getDefaultState().getPistonBehavior().equals(PistonBehavior.BLOCK)) {
            return false;
        }
        return !itemBlock.getStateManager().getDefaultState().hasBlockEntity();
    }

    private void placeBlock(BlockPointer pointer, ItemStack stack, BlockPos placePos) {
        Direction direction = pointer.state().get(Properties.FACING);
        ServerWorld world = pointer.world();
        Item item = stack.getItem();

        Direction blockFacingDirection = world.isAir(placePos.down()) || !world.getFluidState(placePos.down()).isEmpty() ? direction : Direction.UP;
        try {
            this.setSuccess(((BlockItem) item).place(new PlacerItemPlacementContext(world, placePos, direction, stack, blockFacingDirection)).isAccepted());
        } catch (Exception exception) {
            LOGGER.error("Error trying to place shulker box at {}", placePos, exception);
        }
    }

    private void placeBoat(BlockPointer pointer, ItemStack stack, BlockPos placePos,boolean chest, BoatEntity.Type variant) {
        double h;
        Direction direction = pointer.state().get(DispenserBlock.FACING);
        ServerWorld world = pointer.world();
        Vec3d vec3d = pointer.centerPos();
        double d = 0.5625 + (double) EntityType.BOAT.getWidth() / 2.0;
        double e = vec3d.getX() + (double)direction.getOffsetX() * d;
        double f = vec3d.getY() + (double)((float)direction.getOffsetY() * 1.125f);
        double g = vec3d.getZ() + (double)direction.getOffsetZ() * d;
        if (world.getFluidState(placePos).isIn(FluidTags.WATER)) {
            h = 1.0;
        } else if (world.getBlockState(placePos).isAir()) {
            h = 0.0;
        } else {
            return;
        }
        BoatEntity boatEntity = chest ? new ChestBoatEntity(world, e, f + h, g) : new BoatEntity(world, e, f + h, g);
        EntityType.copier(world, stack, null).accept(boatEntity);
        boatEntity.setVariant(variant);
        boatEntity.setYaw(direction.asRotation());
        world.spawnEntity(boatEntity);
        stack.decrement(1);
        this.setSuccess(true);

    }

    private void placeBucket(BlockPointer pointer, ItemStack stack, BlockPos placePos) {

    }

    private void placeSpawnEgg(BlockPointer pointer, ItemStack stack, BlockPos placePos) {
        Direction direction = pointer.state().get(DispenserBlock.FACING);
        EntityType<?> entityType = ((SpawnEggItem)stack.getItem()).getEntityType(stack);
        try {
            entityType.spawnFromItemStack(pointer.world(), stack, null, placePos, SpawnReason.DISPENSER, direction != Direction.UP, false);
        } catch (Exception exception) {
            LOGGER.error("Error while dispensing spawn egg from placer at {}", pointer.pos(), exception);
            stack.setCount(0);
            return;
        }
        stack.decrement(1);
        pointer.world().emitGameEvent(null, GameEvent.ENTITY_PLACE, pointer.pos());
        this.setSuccess(true);
    }
}
