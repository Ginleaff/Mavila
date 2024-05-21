package net.ginleaf.testmod.block.placer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

public class BlockPlacementPlacerBehavior extends FallibleItemDispenserBehavior {

    public ItemStack place(ItemStack stack, BlockState state, ServerWorld world, BlockPos pos) {
        Direction direction = state.get(Properties.FACING);
        ItemStack updatedStack = this.dispenseSilently(stack,direction,world,pos);
        this.spawnParticles(world,direction,pos);
        this.playSound(world,pos);
        return updatedStack;
    }

    protected boolean isVentBlocked(Direction direction, World world, BlockPos pos) {
        if (!world.getBlockState(pos.offset(direction.getOpposite())).getFluidState().isEmpty()) return true;
        VoxelShape blockBehindVent = world.getBlockState(pos.offset(direction.getOpposite())).getCollisionShape(world,pos);
        if (Block.isFaceFullSquare(blockBehindVent,direction)) {
            LOGGER.info("Vent face IS Blocked");
            return true;
        }
        return false;
    }

    private ItemStack dispenseSilently(ItemStack stack, Direction direction, ServerWorld world, BlockPos pos) {
        this.setSuccess(false);
        BlockPos placePos = pos.offset(direction);
        if(!world.isAir(placePos) && world.getBlockState(placePos).getFluidState().isEmpty() || isVentBlocked(direction,world,pos)) {
            return stack;
        }
        ItemStack itemStack = stack.split(1);
        placeBlock(world,pos,direction,itemStack,placePos);
        return stack;
    }

    private void placeBlock(ServerWorld world, BlockPos pos, Direction direction, ItemStack stack, BlockPos placePos) {
        Item item = stack.getItem();
        Direction blockFacingDirection = world.isAir(pos.down()) || world.isWater(pos.down()) ? direction : Direction.UP;
        try {
            this.setSuccess(((BlockItem)item).place(new AutomaticItemPlacementContext(world,placePos,direction,stack,blockFacingDirection)).isAccepted());
        } catch (Exception exception) {
            LOGGER.error("Error trying to place shulker box at {}", placePos, exception);
        }

    }
    protected void spawnParticles(ServerWorld world, Direction direction, BlockPos pos) {
        if (this.isSuccess()) {
            world.syncWorldEvent(WorldEvents.DISPENSER_ACTIVATED, pos, direction.getOpposite().getId());
        }
    }

    protected void playSound(ServerWorld world, BlockPos pos) {
        if (this.isSuccess()) {

        }
    }

}
