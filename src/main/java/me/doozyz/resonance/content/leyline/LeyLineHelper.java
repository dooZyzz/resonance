package me.doozyz.resonance.content.leyline;

import me.doozyz.resonance.registry.ModAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.Optional;

public class LeyLineHelper {

    /**
     * Get ley line data for a chunk at the given block position
     */
    public static LeyLineData getLeyLineData(Level level, BlockPos pos) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return new LeyLineData();
        }

        ChunkPos chunkPos = new ChunkPos(pos);
        LevelChunk chunk = serverLevel.getChunk(chunkPos.x, chunkPos.z);

        return chunk.getData(ModAttachments.LEY_LINE_DATA);
    }

    /**
     * Get ley line data for a specific chunk
     */
    public static LeyLineData getLeyLineData(ServerLevel level, ChunkPos chunkPos) {
        LevelChunk chunk = level.getChunk(chunkPos.x, chunkPos.z);
        return chunk.getData(ModAttachments.LEY_LINE_DATA);
    }

    /**
     * Check if a position has any ley lines
     */
    public static boolean hasLeyLine(Level level, BlockPos pos) {
        LeyLineData data = getLeyLineData(level, pos);
        return data.hasLeyLine();
    }

    /**
     * Get the strongest ley line at a position
     */
    public static Optional<LeyLineData.LeyLine> getStrongestLine(Level level, BlockPos pos) {
        LeyLineData data = getLeyLineData(level, pos);
        return data.getStrongestLine();
    }

    /**
     * Check if a position is a nexus (2+ ley lines)
     */
    public static boolean isNexus(Level level, BlockPos pos) {
        LeyLineData data = getLeyLineData(level, pos);
        return data.isNexus();
    }

    /**
     * Initialize ley line data for a chunk
     */
    public static void initializeChunkData(LevelChunk chunk, long worldSeed) {
        LeyLineData data = LeyLineGenerator.generateForChunk(worldSeed, chunk.getPos());
        chunk.setData(ModAttachments.LEY_LINE_DATA, data);
    }
}
