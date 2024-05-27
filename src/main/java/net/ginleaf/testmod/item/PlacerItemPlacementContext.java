package net.ginleaf.testmod.item;

import net.ginleaf.testmod.block.PlacerBlock;
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

    This had to be done because there is no system for supporting Vec3D horizontal axis with directional placement without an entity.
    This fails when more than one Placer is placed around an activated Placer, even though it should be only checking...
    ...for a Placer currently placing the skull, even if they aren't activated.
    This is a fine sacrifise and works correctly under 99.9% of circumstances.
    If you know a solution to this issue, please suggest it.
     */
    @Override
    public float getPlayerYaw() {
        if (getWorld().getBlockState(getBlockPos().offset(Direction.NORTH)).getBlock() instanceof PlacerBlock placerBlock && placerBlock.isPlacing()) {
            return Direction.NORTH.getHorizontal() * 90;
        }
        if (getWorld().getBlockState(getBlockPos().offset(Direction.WEST)).getBlock() instanceof PlacerBlock placerBlock && placerBlock.isPlacing()) {
            return Direction.WEST.getHorizontal() * 90;
        }
        if (getWorld().getBlockState(getBlockPos().offset(Direction.SOUTH)).getBlock() instanceof PlacerBlock placerBlock && placerBlock.isPlacing()) {
            return Direction.SOUTH.getHorizontal() * 90;
        }
        if (getWorld().getBlockState(getBlockPos().offset(Direction.EAST)).getBlock() instanceof PlacerBlock placerBlock && placerBlock.isPlacing()) {
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
