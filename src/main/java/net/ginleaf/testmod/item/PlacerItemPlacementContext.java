package net.ginleaf.testmod.item;

import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class PlacerItemPlacementContext extends AutomaticItemPlacementContext {

    public PlacerItemPlacementContext(World world, BlockPos pos, Direction facing, ItemStack stack, Direction side) {
        super(world, pos, facing, stack, side);
    }

    @Override
    public Direction getPlayerLookDirection() {
        return this.getSide().getOpposite();
    }

    @Override
    public float getPlayerYaw() {
        return this.getPlayerLookDirection().getHorizontal() * 270;
    }

    @Override
    public Direction[] getPlacementDirections() {
        int i;
        Direction[] directions = getPlacerFacingOrder(this.getPlayerLookDirection());
        if (this.canReplaceExisting) {
            return directions;
        }
        Direction direction = this.getSide();
        for (i = 0; i < directions.length && directions[i] != direction.getOpposite(); ++i) {
            if (i > 0) {
                System.arraycopy(directions, 0, directions, 1, i);
                directions[0] = direction.getOpposite();
            }
        }
        return directions;
    }

    private Direction[] getPlacerFacingOrder(Direction playerLookDirection) {
        int i = 6;
        Direction[] allFacingDirections = new Direction[6];
        while(--i >= 0) {
            allFacingDirections[i] = playerLookDirection;
        }
        return allFacingDirections;
    }
}
