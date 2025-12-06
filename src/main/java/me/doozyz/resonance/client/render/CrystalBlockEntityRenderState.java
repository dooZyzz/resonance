package me.doozyz.resonance.client.render;

import me.doozyz.resonance.content.resonance.ResonanceState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;

import java.util.EnumSet;
import java.util.Set;

public class CrystalBlockEntityRenderState extends BlockEntityRenderState {
    public ResonanceState resonanceState = ResonanceState.EMPTY;
    public float renderPhase = 0;
    public float renderAmplitude = 0;
    public Set<Direction> connections = EnumSet.noneOf(Direction.class);
}
