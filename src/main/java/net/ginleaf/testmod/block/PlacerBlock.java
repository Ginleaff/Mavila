package net.ginleaf.testmod.block;
import com.mojang.serialization.MapCodec;
import net.ginleaf.testmod.TestMod;
import net.ginleaf.testmod.block.entity.PlacerBlockEntity;
import net.ginleaf.testmod.block.placer.PlacerPlacementBehavior;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

public class PlacerBlock extends DispenserBlock {
    public static final DirectionProperty FACING = Properties.FACING;
    public static final BooleanProperty TRIGGERED = Properties.TRIGGERED;
    public static final MapCodec<PlacerBlock> CODEC = PlacerBlock.createCodec(PlacerBlock::new);

    public PlacerBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(TRIGGERED, false));
    }

    protected boolean isBlocked(Direction direction, World world, BlockPos pos) {
        if (pos.offset(direction).getY() < world.getBottomY() || pos.offset(direction).getY() > world.getTopY() - 1 || !world.getWorldBorder().contains(pos.offset(direction))) return true;
        if (!world.getBlockState(pos.offset(direction.getOpposite())).getFluidState().isEmpty()) return true;
        VoxelShape blockBehindVent = world.getBlockState(pos.offset(direction.getOpposite())).getCollisionShape(world,pos);
        return Block.isFaceFullSquare(blockBehindVent, direction);
    }

    public MapCodec<? extends PlacerBlock> getCodec() {
        return CODEC;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PlacerBlockEntity(pos, state);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;
        NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
        if (screenHandlerFactory != null) {
            player.openHandledScreen(screenHandlerFactory);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected void dispense(ServerWorld world, BlockState state, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(!(blockEntity instanceof PlacerBlockEntity placerBlockEntity)) {
            TestMod.LOGGER.warn("Ignoring placing attempt for Placer without matching block entity at {}", pos);
            return;
        }
        int i = placerBlockEntity.chooseNonEmptySlot(world.random);
        if (i < 0 || isBlocked(state.get(FACING),world,pos)) {
            world.syncWorldEvent(WorldEvents.DISPENSER_FAILS, pos, 0);
            world.emitGameEvent(GameEvent.BLOCK_ACTIVATE, pos, GameEvent.Emitter.of(blockEntity.getCachedState()));
            return;
        }
        ItemStack itemStack = placerBlockEntity.getStack(i);
        BlockPointer placerPointer = new BlockPointer(world,pos,state,placerBlockEntity);
        PlacerPlacementBehavior placerBehavior = new PlacerPlacementBehavior();
        placerBlockEntity.setStack(i, placerBehavior.dispense(placerPointer,itemStack));
    }
}
