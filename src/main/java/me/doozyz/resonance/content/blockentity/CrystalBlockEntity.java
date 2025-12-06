package me.doozyz.resonance.content.blockentity;

import com.mojang.serialization.Codec;
import me.doozyz.resonance.content.CrystalTier;
import me.doozyz.resonance.content.resonance.IResonanceNode;
import me.doozyz.resonance.content.resonance.ResonanceState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

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
    public void receiveResonance(Direction from, ResonanceState newState) {
        // Check if we should sync to clients
        if (shouldSync(this.currentState, newState)) {
            this.currentState = newState;
            this.renderAmplitude = lerp(0.1f, renderAmplitude, newState.amplitude());
            setChanged();

            // Sync to clients
            if (level != null && !level.isClientSide()) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
            }
        } else {
            // Update locally without syncing
            this.currentState = newState;
            this.renderAmplitude = lerp(0.1f, renderAmplitude, newState.amplitude());
        }
    }

    /**
     * Check if state change is significant enough to sync
     */
    private boolean shouldSync(ResonanceState old, ResonanceState newState) {
        // Frequency change > 1 Hz
        if (Math.abs(old.frequency() - newState.frequency()) > 1.0f) return true;

        // Amplitude change > 5%
        if (old.amplitude() > 0.01f) {
            float amplitudeChangePercent = Math.abs(old.amplitude() - newState.amplitude()) / old.amplitude();
            if (amplitudeChangePercent > 0.05f) return true;
        } else if (newState.amplitude() > 0.01f) {
            return true; // Going from inactive to active
        }

        return false;
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

    /**
     * Client-side tick - advances phase locally to reduce network traffic
     */
    public void clientTick() {
        if (currentState.isActive()) {
            // Advance phase locally based on frequency
            float dt = 1.0f / 20.0f; // One tick
            float phaseAdvance = 2 * (float) Math.PI * currentState.frequency() * dt;
            renderPhase = (renderPhase + phaseAdvance) % (2 * (float) Math.PI);

            // Smoothly interpolate amplitude
            renderAmplitude = lerp(0.1f, renderAmplitude, currentState.amplitude());
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


    /**
     * Get data for client sync (chunk loading)
     */
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);
        tag.putFloat("frequency", currentState.frequency());
        tag.putFloat("amplitude", currentState.amplitude());
        tag.putFloat("phase", currentState.phase());
        return tag;
    }

    /**
     * Load from update tag (used by getUpdatePacket)
     */
    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        float frequency = input.read("frequency", Codec.FLOAT).orElse(0f);
        float amplitude = input.read("amplitude", Codec.FLOAT).orElse(0f);
        float phase = input.read("phase", Codec.FLOAT).orElse(0f);
        currentState = new ResonanceState(frequency, amplitude, phase);

        // Update render values immediately for client
        if (level != null && level.isClientSide()) {
            renderPhase = phase;
            renderAmplitude = amplitude;
        }
    }

    /**
     * Get packet for state updates
     */
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    /**
     * Update client state from server (used by update packet)
     */
    public void updateClientState(float frequency, float amplitude, float phase) {
        this.currentState = new ResonanceState(frequency, amplitude, phase);
        this.renderPhase = phase;
        this.renderAmplitude = amplitude;
    }

    private static float lerp(float delta, float start, float end) {
        return start + delta * (end - start);
    }
}
