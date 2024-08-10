package mod.ginleaf.railings.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;

public class HorizontalBorderBlock extends TransparentBlock implements Waterloggable {
    public static final MapCodec<HorizontalBorderBlock> CODEC = HorizontalBorderBlock.createCodec(HorizontalBorderBlock::new);
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty WEST = BooleanProperty.of("west");
    public static final IntProperty RAILING_AMOUNT = IntProperty.of("railing_amount", 1,4);

    public static final BooleanProperty[] FACE_PROPERTIES = {NORTH, EAST, SOUTH, WEST};

    public HorizontalBorderBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(WATERLOGGED, false)
                .with(NORTH, false)
                .with(EAST, false)
                .with(SOUTH, false)
                .with(WEST, false)
                .with(RAILING_AMOUNT, 1)
        );
    }

    protected void incrementState(World world, BlockPos blockPos, BlockState blockState, BooleanProperty property) {
        world.setBlockState(blockPos, blockState.with(property, true).with(RAILING_AMOUNT, blockState.get(RAILING_AMOUNT)+1));

    }

    protected boolean willBordersCollide(BlockState intersectingState, int dir) {
        if(!(intersectingState.getBlock() instanceof HorizontalBorderBlock)) return false;
        return intersectingState.get(FACE_PROPERTIES[(dir + 2) % 4]);
    }

    protected boolean willClipOf(VoxelShape outlineShape, int dir) {
        switch(dir) {
            case 0:
                if(outlineShape.getMax(Direction.Axis.Z) >= 1) return true;
                break;
            case 1:
                if(outlineShape.getMin(Direction.Axis.X) <= 0) return true;
                break;
            case 2:
                if(outlineShape.getMin(Direction.Axis.Z) <= 0) return true;
                break;
            case 3:
                if(outlineShape.getMax(Direction.Axis.X) >= 1) return true;
                break;
        }
        return false;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, NORTH, EAST, SOUTH, WEST, RAILING_AMOUNT);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        int dir = ctx.getHorizontalPlayerFacing().getHorizontal();
        return Objects.requireNonNull(super.getPlacementState(ctx))
                .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER)
                .with(FACE_PROPERTIES[dir], true);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(stack.getItem() instanceof BlockItem blockItem) {
            if(!(blockItem.getBlock().getName().equals(this.getName()))) {
                return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
            int dir = player.getHorizontalFacing().getHorizontal();
            int bitMask = 0;
            for(int i = 0; i < 4; i++) {
                bitMask += (state.get(FACE_PROPERTIES[i]) ? 1 << i : 0);
            }
            if((bitMask | (1 << dir)) == bitMask) return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            BlockPos intersectingPos = pos.offset(player.getHorizontalFacing().getOpposite());
            BlockState intersectingState = world.getBlockState(intersectingPos);
            if(willBordersCollide(intersectingState,dir) || willClipOf(intersectingState.getOutlineShape(world,intersectingPos),dir)) {
                return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
            }
            stack.decrementUnlessCreative(1,player);
            incrementState(world,pos,state, FACE_PROPERTIES[dir]);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, world.getBlockState(pos)));
            world.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
            return ItemActionResult.success(world.isClient());
        } else {
            return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
    }

    /*@Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if(!world.isClient() && !player.isCreative()) {
            int returnAmount = 0;
            for(int i = 0; i < 4; i++) {
                returnAmount += state.get(FACE_PROPERTIES[i]) ? 1 : 0;
            }
            ItemStack itemStack = new ItemStack(state.getBlock().asItem());
            itemStack.setCount(returnAmount);
            ItemScatterer.spawn(world,pos.getX(),pos.getY(),pos.getZ(),itemStack);
        }
        return super.onBreak(world, pos, state, player);
    }*/

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.offset(Direction.DOWN);
        if(!world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, Direction.UP)) return false;
        for(int dir = 0; dir < 4; dir++) {
            if(state.get(FACE_PROPERTIES[dir])) {
                BlockPos intersectionPos = pos.offset(Direction.fromHorizontal(dir).getOpposite());
                BlockState intersectionState = world.getBlockState(intersectionPos);
                if(intersectionState.isAir()) return true;
                if(intersectionState.getBlock() instanceof HorizontalBorderBlock) {
                    return !intersectionState.get(FACE_PROPERTIES[(dir + 2) % 4]);
                }
                return !willClipOf(intersectionState.getOutlineShape(world,intersectionPos), dir);
            }
        }
        return true;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if(direction == Direction.DOWN && !this.canPlaceAt(state, world, pos)) return Blocks.AIR.getDefaultState();
        if(direction.getHorizontal() != -1 && state.get(FACE_PROPERTIES[(direction.getHorizontal() + 2) % 4]) && !(neighborState.getBlock() instanceof HorizontalBorderBlock)) {
            if(willClipOf(neighborState.getOutlineShape(world,pos), direction.getHorizontal())) {
                return Blocks.AIR.getDefaultState();
            }
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }
}
