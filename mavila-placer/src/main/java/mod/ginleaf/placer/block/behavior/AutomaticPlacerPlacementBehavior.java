package mod.ginleaf.placer.block.behavior;

import mod.ginleaf.placer.block.PlacerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class AutomaticPlacerPlacementBehavior extends AutomaticItemPlacementContext {

    public AutomaticPlacerPlacementBehavior(World world, BlockPos pos, Direction facing, ItemStack stack, Direction side) {
        super(world, pos, facing, stack, side);
    }

    @Override
    public Direction getPlayerLookDirection() {
        return this.getSide().getOpposite();
    }

    //This method is responsible for handling Floor Skulls.
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

    //This method assures the directional placement of the skull is correct.
    private Direction[] getPlacerFacingArray(Direction playerLookDirection) {
        int i = 6;
        Direction[] oneDirection = new Direction[6];
        while(--i >= 0) {
            oneDirection[i] = playerLookDirection;
        }
        return oneDirection;
    }
}
