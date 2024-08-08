package mod.ginleaf.railings.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class HorizontalBorderBlock extends Block implements Waterloggable {
    public static final MapCodec<HorizontalBorderBlock> CODEC = HorizontalBorderBlock.createCodec(HorizontalBorderBlock::new);
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty WEST = BooleanProperty.of("west");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty[] FACE_PROPERTIES = {NORTH, WEST, SOUTH, EAST};

    public HorizontalBorderBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(WATERLOGGED, false)
                .with(NORTH, false)
                .with(WEST, false)
                .with(SOUTH, false)
                .with(EAST, false));
    }

    protected void incrementState(World world, BlockPos blockPos, BlockState blockState, BooleanProperty property) {
        world.setBlockState(blockPos, blockState.with(property, true));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, NORTH, WEST, SOUTH, EAST);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return Objects.requireNonNull(super.getPlacementState(ctx)).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    @Override
    protected boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        int dir = (placer != null) ? placer.getHorizontalFacing().getHorizontal() : 0;
        incrementState(world,pos,state, FACE_PROPERTIES[dir]);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(stack.getItem() instanceof BlockItem blockItem) {
            if(!(blockItem.getBlock().getName().equals(this.getName()))) return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            int dir = player.getHorizontalFacing().getHorizontal();
            int bitMask = 0;
            for(int i = 0; i < 4; i++) {
                bitMask += (state.get(FACE_PROPERTIES[i]) ? 1 << i : 0);
            }
            if((bitMask | (1 << dir)) == bitMask) return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            stack.decrementUnlessCreative(1,player);
            incrementState(world,pos,state, FACE_PROPERTIES[dir]);
            return ItemActionResult.SUCCESS;
        } else {
            return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        int returnAmount = 0;
        for(int i = 0; i < 4; i++) {
            returnAmount += state.get(FACE_PROPERTIES[i]) ? 1 : 0;
        }
        ItemStack itemStack = new ItemStack(state.getBlock().asItem());
        itemStack.setCount(returnAmount);
        ItemScatterer.spawn((World) world,pos.getX(),pos.getY(),pos.getZ(),itemStack);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }
}
