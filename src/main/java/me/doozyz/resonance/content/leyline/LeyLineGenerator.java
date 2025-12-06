package me.doozyz.resonance.content.leyline;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;

public class LeyLineGenerator {
    private static final int MINOR_GRID_SIZE = 6;  // 6 chunks = ~96 blocks
    private static final int MAJOR_GRID_SIZE = 32; // 32 chunks = ~512 blocks

    public static LeyLineData generateForChunk(long worldSeed, ChunkPos chunkPos) {
        LeyLineData data = new LeyLineData();

        // Check for major ley lines
        checkMajorLeyLine(worldSeed, chunkPos, data);

        // Check for minor ley lines
        checkMinorLeyLine(worldSeed, chunkPos, data);

        return data;
    }

    private static void checkMajorLeyLine(long worldSeed, ChunkPos chunkPos, LeyLineData data) {
        // Determine which major grid cell this chunk is in
        int majorGridX = Math.floorDiv(chunkPos.x, MAJOR_GRID_SIZE);
        int majorGridZ = Math.floorDiv(chunkPos.z, MAJOR_GRID_SIZE);

        // Create deterministic random for this grid cell
        long gridSeed = worldSeed ^ ((long) majorGridX << 32 | (majorGridZ & 0xFFFFFFFFL));
        RandomSource random = RandomSource.create(gridSeed);

        // Generate line parameters for this grid cell
        float angle = random.nextFloat() * (float) Math.PI * 2;
        int strength = 100 + random.nextInt(200); // 100-300

        // Calculate if the line passes through this chunk
        if (lineIntersectsChunk(chunkPos, majorGridX * MAJOR_GRID_SIZE, majorGridZ * MAJOR_GRID_SIZE, angle, MAJOR_GRID_SIZE)) {
            BlockPos intersection = calculateIntersectionPoint(chunkPos, angle);
            data.addLeyLine(new LeyLineData.LeyLine(LeyLineType.MAJOR, angle, strength, intersection));
        }
    }

    private static void checkMinorLeyLine(long worldSeed, ChunkPos chunkPos, LeyLineData data) {
        // Determine which minor grid cell this chunk is in
        int minorGridX = Math.floorDiv(chunkPos.x, MINOR_GRID_SIZE);
        int minorGridZ = Math.floorDiv(chunkPos.z, MINOR_GRID_SIZE);

        // Create deterministic random for this grid cell
        long gridSeed = worldSeed ^ ((long) minorGridX << 32 | (minorGridZ & 0xFFFFFFFFL)) ^ 0x123456789ABCDEFL;
        RandomSource random = RandomSource.create(gridSeed);

        // Generate line parameters for this grid cell
        float angle = random.nextFloat() * (float) Math.PI * 2;
        int strength = 30 + random.nextInt(40); // 30-70

        // Calculate if the line passes through this chunk
        if (lineIntersectsChunk(chunkPos, minorGridX * MINOR_GRID_SIZE, minorGridZ * MINOR_GRID_SIZE, angle, MINOR_GRID_SIZE)) {
            BlockPos intersection = calculateIntersectionPoint(chunkPos, angle);
            data.addLeyLine(new LeyLineData.LeyLine(LeyLineType.MINOR, angle, strength, intersection));
        }
    }

    private static boolean lineIntersectsChunk(ChunkPos chunk, int gridOriginX, int gridOriginZ, float angle, int gridSize) {
        // Simple check: if chunk is in the grid cell, there's a chance the line passes through
        // For more accuracy, we'd do line-square intersection math
        // For now, use a probability based on angle
        int relativeX = chunk.x - gridOriginX;
        int relativeZ = chunk.z - gridOriginZ;

        // Calculate distance from grid center
        int centerX = gridSize / 2;
        int centerZ = gridSize / 2;

        // Simple proximity check - line passes through center of grid
        float lineX = centerX + (float) Math.cos(angle) * (relativeZ - centerZ);
        float lineZ = centerZ + (float) Math.sin(angle) * (relativeX - centerX);

        // Check if line is close to this chunk
        float distX = Math.abs(relativeX - lineX);
        float distZ = Math.abs(relativeZ - lineZ);

        return distX < 2 && distZ < 2; // Within 2 chunks of the line
    }

    private static BlockPos calculateIntersectionPoint(ChunkPos chunk, float angle) {
        // Calculate a point within the chunk where the line intersects
        int x = chunk.getMinBlockX() + 8;
        int z = chunk.getMinBlockZ() + 8;
        int y = 64; // Default Y level for ley line reference

        return new BlockPos(x, y, z);
    }
}
