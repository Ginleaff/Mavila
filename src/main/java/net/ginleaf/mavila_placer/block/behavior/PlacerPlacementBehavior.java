package net.ginleaf.mavila_placer.block.behavior;

import net.ginleaf.mavila_placer.MavilaPlacer;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
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
import net.minecraft.util.math.*;
import net.minecraft.world.event.GameEvent;

import java.util.Random;

public class PlacerPlacementBehavior extends FallibleItemDispenserBehavior {

    /* I know, the Boat placing check is really cursed.
    It should provide support for modded boats, but if there's an incorrect ID, hell will break.
    Feel free to suggest a better implementation. */
    @Override
    protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        BlockPos placePos = pointer.pos().offset(pointer.state().get(Properties.FACING));
        Item item = stack.getItem();
        this.setSuccess(false);

        if (item instanceof BlockItem && isPlacerPlaceable((BlockItem) item)) {
            placeBlock(pointer,stack,placePos);

        } else if (item instanceof BoatItem) {
            boolean hasChest = item.getDefaultStack().getRegistryEntry().isIn(ItemTags.CHEST_BOATS);
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

    @Override
    protected void spawnParticles(BlockPointer pointer, Direction side) {
        if (!this.isSuccess()) return;
        Direction ventSide = side.getOpposite();
        Random random = new Random();
        BlockPos pos = pointer.pos();
        ServerWorld world = pointer.world();
        int i = ventSide.getOffsetX();
        int j = ventSide.getOffsetY();
        int k = ventSide.getOffsetZ();
        double d = (double)pos.getX() + (double)i * 0.75 + 0.5;
        double e = (double)pos.getY() + (double)j * 0.6 + 0.5;
        double f = (double)pos.getZ() + (double)k * 0.75 + 0.5;
        for (int l = 0; l < 5; ++l) {
            double g = random.nextDouble() * 0.2 + 0.01;
            double h = d + (double)i * 0.01 + (random.nextDouble() - 0.5) * (double)k * 0.5;
            double m = e + (double)j * 0.01 + (random.nextDouble() - 0.5) * (double)j * 0.5;
            double n = f + (double)k * 0.01 + (random.nextDouble() - 0.5) * (double)i * 0.5;
            double o = (double)i * g + random.nextGaussian() * 0.01;
            double p = (double)j * g + random.nextGaussian() * 0.01;
            double q = (double)k * g + random.nextGaussian() * 0.01;
            world.spawnParticles(MavilaPlacer.VENT_SMOKE, h, m, n,1,o,p,q,0.055);
        }
    }

    private boolean isPlacerPlaceable(BlockItem item) {
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
            this.setSuccess(((BlockItem) item).place(new AutomaticSkullPlacementContext(world, placePos, direction, stack, blockFacingDirection)).isAccepted());
        } catch (Exception exception) {
            LOGGER.error("Error while dispensing {} from Placer at {}", stack.getItem().getName().toString(), placePos, exception);
        }
    }

    private void placeBoat(BlockPointer pointer, ItemStack stack, BlockPos placePos,boolean chest, BoatEntity.Type variant) {
        double h;
        Direction direction = pointer.state().get(Properties.FACING);
        ServerWorld world = pointer.world();
        Vec3d vec3d = pointer.centerPos();
        double d = 0.5625 + (double) EntityType.BOAT.getWidth() / 2.0;
        double e = vec3d.getX() + (double)direction.getOffsetX() * d;
        double f = vec3d.getY() + (double)((float)direction.getOffsetY() * 1.125f);
        double g = vec3d.getZ() + (double)direction.getOffsetZ() * d;
        if (world.getFluidState(placePos).isIn(FluidTags.WATER)) {
            h = 1.0;
        } else if (!isPlacementBlocked(pointer,placePos)) {
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
        FluidModificationItem fluidModificationItem = (FluidModificationItem)(stack.getItem());
        Direction direction = pointer.state().get(Properties.FACING);
        ServerWorld world = pointer.world();
        if (fluidModificationItem.placeFluid(null, world, placePos, null)) {
            fluidModificationItem.onEmptied(null, world, stack, placePos);
            stack.decrement(1);
            ItemDispenserBehavior.spawnItem(pointer.world(), new ItemStack(Items.BUCKET, 1), 3, direction, DispenserBlock.getOutputLocation(pointer));
            this.setSuccess(true);
        }
    }

    private void placeSpawnEgg(BlockPointer pointer, ItemStack stack, BlockPos placePos) {
        Direction direction = pointer.state().get(DispenserBlock.FACING);
        EntityType<?> entityType = ((SpawnEggItem)stack.getItem()).getEntityType(stack);
        if (isPlacementBlocked(pointer,placePos)) {
            return;
        }
        try {
            entityType.spawnFromItemStack(pointer.world(), stack, null, placePos, SpawnReason.DISPENSER, direction != Direction.UP, false);
        } catch (Exception exception) {
            LOGGER.error("Error while dispensing {} from Placer at {}", stack.getItem().getName().toString(), placePos, exception);
            stack.setCount(0);
            return;
        }
        stack.decrement(1);
        pointer.world().emitGameEvent(null, GameEvent.ENTITY_PLACE, pointer.pos());
        this.setSuccess(true);
    }

    private boolean isPlacementBlocked(BlockPointer pointer, BlockPos placePos) {
        ServerWorld world = pointer.world();
        return !world.isSpaceEmpty(new Box(placePos));
    }
}
