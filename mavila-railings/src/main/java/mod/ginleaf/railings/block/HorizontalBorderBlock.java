package mod.ginleaf.railings.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class HorizontalBorderBlock extends Block implements Waterloggable {
    public static final MapCodec<HorizontalBorderBlock> CODEC = HorizontalBorderBlock.createCodec(HorizontalBorderBlock::new);
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final IntProperty COMBINATIONS = IntProperty.of("combinations", 1, 15);
    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty WEST = BooleanProperty.of("west");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    private int borderCount;

    public HorizontalBorderBlock(Settings settings) {
        super(settings);
        this.borderCount = 1;
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(WATERLOGGED, false)
                .with(NORTH, false)
                .with(WEST, false)
                .with(SOUTH, false)
                .with(EAST, false)
        );
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        world.setBlockState(blockPos, blockState.with(COMBINATIONS,1 << (ctx.getHorizontalPlayerFacing().getHorizontal() / 90 - 1)));
        return blockState;
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(stack.getItem() instanceof BlockItem blockItem) {
            if(this.borderCount >= 4) return ItemActionResult.FAIL;
            if(!(blockItem.getBlock().getName().equals(this.getName()))) return ItemActionResult.FAIL;
            int combinations = state.get(COMBINATIONS);
            int directionBit = 1 << (player.getHorizontalFacing().getHorizontal() / 90 - 1);
            if((combinations | directionBit) == combinations) return ItemActionResult.FAIL;
            stack.decrement(1);
            this.borderCount++;
            world.setBlockState(pos, state.with(COMBINATIONS, combinations | directionBit));
            return ItemActionResult.CONSUME;
        }
        return ItemActionResult.FAIL;
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        ItemStack itemStack = new ItemStack(world.getBlockState(pos).getBlock().asItem());
        itemStack.setCount(this.borderCount);
        ItemScatterer.spawn((World) world,pos.getX(),pos.getY(),pos.getZ(),itemStack);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, NORTH, WEST, SOUTH, EAST);
    }
}
