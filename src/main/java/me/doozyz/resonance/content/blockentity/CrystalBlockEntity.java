package me.doozyz.resonance.content.blockentity;

import com.mojang.serialization.Codec;
import me.doozyz.resonance.content.CrystalTier;
import me.doozyz.resonance.content.resonance.IResonanceNode;
import me.doozyz.resonance.content.resonance.ResonanceState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.EnumSet;
import java.util.Set;

/**
 * Base class for all crystal block entities that participate in resonance networks
 */
public abstract class CrystalBlockEntity extends BlockEntity implements IResonanceNode {

    protected final CrystalTier tier;
    protected ResonanceState currentState = ResonanceState.EMPTY;
    protected Set<Direction> connections = EnumSet.noneOf(Direction.class);

    // For client-side rendering
    protected float renderPhase = 0;
    protected float renderAmplitude = 0;

    public CrystalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, CrystalTier tier) {
        super(type, pos, state);
        this.tier = tier;
    }

    @Override
    public CrystalTier getTier() {
        return tier;
    }

    @Override
    public ResonanceState getResonanceState() {
        return currentState;
    }

    @Override
    public void receiveResonance(Direction from, ResonanceState state) {
        this.currentState = state;
        this.renderAmplitude = lerp(0.1f, renderAmplitude, state.amplitude());
        setChanged();
    }

    @Override
    public void onNetworkChanged() {
        updateConnections();
        setChanged();
    }

    protected void updateConnections() {
        connections.clear();
        for (Direction dir : getConnectionDirections()) {
            BlockPos neighbor = worldPosition.relative(dir);
            if (level != null && level.getBlockEntity(neighbor) instanceof IResonanceNode node) {
                if (node.getConnectionDirections().contains(dir.getOpposite())) {
                    connections.add(dir);
                }
            }
        }
    }

    public void clientTick() {
        if (currentState.isActive()) {
            float targetPhase = currentState.phase();
            renderPhase = lerp(0.2f, renderPhase, targetPhase);
        }
    }

    public float getRenderPhase() {
        return renderPhase;
    }

    public float getRenderAmplitude() {
        return renderAmplitude;
    }

    public Set<Direction> getVisualConnections() {
        return connections;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("frequency", Codec.FLOAT, currentState.frequency());
        output.store("amplitude", Codec.FLOAT, currentState.amplitude());
        output.store("phase", Codec.FLOAT, currentState.phase());
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        float frequency = input.read("frequency", Codec.FLOAT).orElse(0f);
        float amplitude = input.read("amplitude", Codec.FLOAT).orElse(0f);
        float phase = input.read("phase", Codec.FLOAT).orElse(0f);
        currentState = new ResonanceState(frequency, amplitude, phase);
    }

    private static float lerp(float delta, float start, float end) {
        return start + delta * (end - start);
    }
}
