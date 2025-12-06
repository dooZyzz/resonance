package me.doozyz.resonance.content.worldgen;

import com.mojang.serialization.Codec;
import me.doozyz.resonance.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CrystalGeodeFeature extends Feature<NoneFeatureConfiguration> {

    public CrystalGeodeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        // Random size: 4-6 block radius
        int outerRadius = 4 + random.nextInt(3);
        int innerRadius = outerRadius - 2;

        // Outer shell - rough stone (80% fill)
        generateSphere(level, origin, outerRadius, ModBlocks.GEODE_SHELL.get().defaultBlockState(), random, 0.8f);

        // Middle layer - crystal block (90% fill)
        generateSphere(level, origin, outerRadius - 1, ModBlocks.RAW_CRYSTAL_BLOCK.get().defaultBlockState(), random, 0.9f);

        // Inner cavity - air (100% fill)
        generateSphere(level, origin, innerRadius, Blocks.AIR.defaultBlockState(), random, 1.0f);

        // Place crystal clusters on inner walls
        placeClustersOnSurface(level, origin, innerRadius, random);

        return true;
    }

    private void generateSphere(WorldGenLevel level, BlockPos center, int radius, BlockState state, RandomSource random, float fillChance) {
        int radiusSquared = radius * radius;

        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-radius, -radius, -radius),
                center.offset(radius, radius, radius))) {

            // Calculate distance from center
            double distanceSquared = center.distSqr(pos);

            // Check if within sphere radius
            if (distanceSquared <= radiusSquared) {
                // Random fill based on fill chance
                if (random.nextFloat() < fillChance) {
                    level.setBlock(pos, state, 2);
                }
            }
        }
    }

    private void placeClustersOnSurface(WorldGenLevel level, BlockPos center, int radius, RandomSource random) {
        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-radius, -radius, -radius),
                center.offset(radius, radius, radius))) {

            if (level.getBlockState(pos).isAir()) {
                // Check each direction for solid surface
                for (Direction dir : Direction.values()) {
                    BlockPos surfacePos = pos.relative(dir);
                    BlockState surfaceState = level.getBlockState(surfacePos);

                    // Check if there's a crystal block surface
                    if (surfaceState.is(ModBlocks.RAW_CRYSTAL_BLOCK.get())) {
                        // 30% chance to place a cluster
                        if (random.nextFloat() < 0.3f) {
                            BlockState clusterState = ModBlocks.CRYSTAL_CLUSTER.get().defaultBlockState();
                            // Set facing direction (away from surface)
                            if (clusterState.hasProperty(net.minecraft.world.level.block.AmethystClusterBlock.FACING)) {
                                clusterState = clusterState.setValue(
                                        net.minecraft.world.level.block.AmethystClusterBlock.FACING,
                                        dir.getOpposite()
                                );
                            }
                            level.setBlock(pos, clusterState, 2);
                        }
                        break;
                    }
                }
            }
        }
    }
}
