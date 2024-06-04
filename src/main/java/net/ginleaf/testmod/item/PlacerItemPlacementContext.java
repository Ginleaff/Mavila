package net.ginleaf.testmod.item;

import net.ginleaf.testmod.block.PlacerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

/*
This class exists purely to add support for Skulls.
Placement is typically handled by an Entity, and the AutomaticItemPlacementContext...
...fails at successfully handling unique placement scenarios requiring player viewing context.
This fakes it.
 */

public class PlacerItemPlacementContext extends AutomaticItemPlacementContext {

    public PlacerItemPlacementContext(World world, BlockPos pos, Direction facing, ItemStack stack, Direction side) {
        super(world, pos, facing, stack, side);
    }

    @Override
    public Direction getPlayerLookDirection() {
        return this.getSide().getOpposite();
    }

    /*
    This method is responsible for handling Floor Skulls.

    A bug occurs when already triggered Placers are nearby, and while it's a shame, it would have cost a useless property just for this one...
    ...niche check that no one will even notice or abuse.
    If you know a solution to this issue, please suggest it.
     */
    @Override
    public float getPlayerYaw() {
        BlockState northState = getWorld().getBlockState(getBlockPos().offset(Direction.NORTH));
        if (northState.getBlock() instanceof PlacerBlock && northState.get(PlacerBlock.TRIGGERED)) {
            return Direction.NORTH.getHorizontal() * 90;
        }
        BlockState westState = getWorld().getBlockState(getBlockPos().offset(Direction.WEST));
        if (westState.getBlock() instanceof PlacerBlock && westState.get(PlacerBlock.TRIGGERED)) {
            return Direction.WEST.getHorizontal() * 90;
        }
        BlockState southState = getWorld().getBlockState(getBlockPos().offset(Direction.SOUTH));
        if (southState.getBlock() instanceof PlacerBlock && southState.get(PlacerBlock.TRIGGERED)) {
            return Direction.SOUTH.getHorizontal() * 90;
        }
        BlockState eastState = getWorld().getBlockState(getBlockPos().offset(Direction.EAST));
        if (eastState.getBlock() instanceof PlacerBlock && eastState.get(PlacerBlock.TRIGGERED)) {
            return Direction.EAST.getHorizontal() * 90;
        }
        return 0;
    }

    //This method is responsible for handling Wall Skulls.
    @Override
    public Direction[] getPlacementDirections() {
        int i;
        Direction[] directions = getPlacerFacingArray(this.getPlayerLookDirection());
        if (this.canReplaceExisting) {
            return directions;
        }
        Direction direction = this.getPlayerLookDirection();
        for (i = 0; i < directions.length && directions[i] != direction.getOpposite(); ++i) {
            if (i > 0) {
                System.arraycopy(directions, 0, directions, 1, i);
                directions[0] = direction.getOpposite();
            }
        }
        return directions;
    }

    /*
    This method assures the placement of the skull is correct.
    Extremely janky, but the directional array is demanded.
     */
    private Direction[] getPlacerFacingArray(Direction playerLookDirection) {
        int i = 6;
        Direction[] oneDirection = new Direction[6];
        while(--i >= 0) {
            oneDirection[i] = playerLookDirection;
        }
        return oneDirection;
    }
}
