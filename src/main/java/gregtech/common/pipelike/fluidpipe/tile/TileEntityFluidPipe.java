package gregtech.common.pipelike.fluidpipe.tile;

import gregtech.api.pipenet.tile.TileEntityPipeBase;
import gregtech.common.pipelike.fluidpipe.FluidPipeProperties;
import gregtech.common.pipelike.fluidpipe.FluidPipeType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.Random;

public class TileEntityFluidPipe extends TileEntityPipeBase<FluidPipeType, FluidPipeProperties> {

    private static final Random random = new Random();
    private IFluidHandler fluidHandler;

    @Override
    public Class<FluidPipeType> getPipeTypeClass() {
        return FluidPipeType.class;
    }

    @Override
    public boolean supportsTicking() {
        return false;
    }

    protected IFluidHandler getFluidHandler() {
        if (fluidHandler == null) {
            this.fluidHandler = new FluidPipeFluidHandler(this);
        }
        return fluidHandler;
    }

    @Nullable
    @Override
    public <T> T getCapabilityInternal(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getFluidHandler());
        }
        return super.getCapabilityInternal(capability, facing);
    }

    public static void setNeighboursToFire(World world, BlockPos selfPos) {
        for (EnumFacing side : EnumFacing.VALUES) {
            if (!random.nextBoolean()) continue;
            BlockPos blockPos = selfPos.offset(side);
            IBlockState blockState = world.getBlockState(blockPos);
            if (blockState.getBlock().isAir(blockState, world, blockPos) ||
                blockState.getBlock().isFlammable(world, blockPos, side.getOpposite())) {
                world.setBlockState(blockPos, Blocks.FIRE.getDefaultState());
            }
        }
    }

    public static void spawnParticles(World worldIn, BlockPos pos, EnumFacing direction, EnumParticleTypes particleType, int particleCount, Random rand) {
        for (int i = 0; i < particleCount; i++) {
            worldIn.spawnParticle(particleType,
                pos.getX() + 0.5 - direction.getFrontOffsetX() / 1.8,
                pos.getY() + 0.5 - direction.getFrontOffsetY() / 1.8,
                pos.getZ() + 0.5 - direction.getFrontOffsetZ() / 1.8,
                direction.getFrontOffsetX() * 0.2 + rand.nextDouble() * 0.1,
                direction.getFrontOffsetY() * 0.2 + rand.nextDouble() * 0.1,
                direction.getFrontOffsetZ() * 0.2 + rand.nextDouble() * 0.1);
        }
    }
}
