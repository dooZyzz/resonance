package me.doozyz.resonance.content.leyline;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class LeyLineData {
    public static final MapCodec<LeyLineData> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LeyLine.CODEC.listOf().fieldOf("ley_lines").forGetter(data -> data.leyLines)
            ).apply(instance, LeyLineData::new)
    );

    private final List<LeyLine> leyLines;

    public LeyLineData() {
        this.leyLines = new ArrayList<>();
    }

    public LeyLineData(List<LeyLine> leyLines) {
        this.leyLines = new ArrayList<>(leyLines);
    }

    public void addLeyLine(LeyLine line) {
        leyLines.add(line);
    }

    public List<LeyLine> getLeyLines() {
        return leyLines;
    }

    public boolean hasLeyLine() {
        return !leyLines.isEmpty();
    }

    public Optional<LeyLine> getStrongestLine() {
        return leyLines.stream()
                .max(Comparator.comparingInt(line -> line.type().getAmplitude()));
    }

    public int getTotalStrength() {
        return leyLines.stream()
                .mapToInt(line -> line.type().getAmplitude())
                .sum();
    }

    public boolean isNexus() {
        return leyLines.size() >= 2;
    }

    public record LeyLine(
            LeyLineType type,
            float angle,
            int strength,
            BlockPos intersectionPoint
    ) {
        public static final Codec<LeyLine> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.xmap(LeyLineType::valueOf, LeyLineType::name).fieldOf("type").forGetter(LeyLine::type),
                        Codec.FLOAT.fieldOf("angle").forGetter(LeyLine::angle),
                        Codec.INT.fieldOf("strength").forGetter(LeyLine::strength),
                        BlockPos.CODEC.fieldOf("intersection_point").forGetter(LeyLine::intersectionPoint)
                ).apply(instance, LeyLine::new)
        );
    }
}
