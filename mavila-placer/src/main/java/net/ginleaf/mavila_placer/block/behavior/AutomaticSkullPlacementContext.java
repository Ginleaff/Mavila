package net.ginleaf.mavila_placer.block.behavior;

import net.ginleaf.mavila_placer.block.PlacerBlock;
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

public class AutomaticSkullPlacementContext extends AutomaticItemPlacementContext {

    public AutomaticSkullPlacementContext(World world, BlockPos pos, Direction facing, ItemStack stack, Direction side) {
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
    This is a fine sacrifise and works correctly in 99.9% of circumstances.
    If you know a solution to this issue, please suggest it.
     */
    @Override
    public float getPlayerYaw() {
        for(int i=2;i<=5;i++) {
            Direction horizontalDirection = Direction.byId(i);
            BlockState state = getWorld().getBlockState(getBlockPos().offset(horizontalDirection));
            if (state.getBlock() instanceof PlacerBlock && state.get(PlacerBlock.TRIGGERED)) {
                return horizontalDirection.getHorizontal() * 90;
            }
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
    This method assures the directional placement of the skull is correct.
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
