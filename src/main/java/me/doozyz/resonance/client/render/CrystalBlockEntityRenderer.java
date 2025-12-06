package me.doozyz.resonance.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import me.doozyz.resonance.content.blockentity.CrystalBlockEntity;
import me.doozyz.resonance.content.resonance.ResonanceState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Custom renderer for crystal block entities - adds glow effects
 * Placeholder implementation until resonance system is fully active
 */
public class CrystalBlockEntityRenderer<T extends CrystalBlockEntity> implements BlockEntityRenderer<T, CrystalBlockEntityRenderState> {

    public CrystalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public CrystalBlockEntityRenderState createRenderState() {
        return new CrystalBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, CrystalBlockEntityRenderState renderState, float partialTick, Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.resonanceState = blockEntity.getResonanceState();
        renderState.renderPhase = blockEntity.getRenderPhase();
        renderState.renderAmplitude = blockEntity.getRenderAmplitude();
        renderState.connections = blockEntity.getVisualConnections();
    }

    @Override
    public void submit(CrystalBlockEntityRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        if (!renderState.resonanceState.isActive()) return;

        float amplitude = renderState.renderAmplitude;
        float phase = renderState.renderPhase;

        // Calculate glow intensity with pulsing
        float baseIntensity = Mth.clamp(amplitude / 100f, 0.1f, 1.0f);
        float pulse = (float) (Math.sin(phase) * 0.5 + 0.5);
        float finalIntensity = baseIntensity * (0.7f + pulse * 0.3f);

        // Get color based on frequency
        float[] color = getColorForFrequency(renderState.resonanceState.frequency());

        // Render glow overlay
        renderGlowCube(poseStack, nodeCollector, color, finalIntensity, renderState.lightCoords);
    }

    /**
     * Map frequency to RGB color
     */
    private float[] getColorForFrequency(float frequency) {
        float hue;

        if (frequency < 50) {
            hue = frequency / 50f * 0.1f;  // Red to orange
        } else if (frequency < 100) {
            hue = 0.1f + (frequency - 50) / 50f * 0.2f;  // Orange to green
        } else if (frequency < 200) {
            hue = 0.3f + (frequency - 100) / 100f * 0.35f;  // Green to blue
        } else {
            hue = 0.65f + Math.min((frequency - 200) / 300f, 1.0f) * 0.2f;  // Blue to purple
        }

        return hsvToRgb(hue, 0.8f, 1.0f);
    }

    /**
     * Convert HSV to RGB
     */
    private float[] hsvToRgb(float h, float s, float v) {
        int i = (int) (h * 6);
        float f = h * 6 - i;
        float p = v * (1 - s);
        float q = v * (1 - f * s);
        float t = v * (1 - (1 - f) * s);

        return switch (i % 6) {
            case 0 -> new float[]{v, t, p};
            case 1 -> new float[]{q, v, p};
            case 2 -> new float[]{p, v, t};
            case 3 -> new float[]{p, q, v};
            case 4 -> new float[]{t, p, v};
            case 5 -> new float[]{v, p, q};
            default -> new float[]{1, 1, 1};
        };
    }

    /**
     * Render a glowing cube overlay
     */
    private void renderGlowCube(PoseStack poseStack, SubmitNodeCollector nodeCollector, float[] color, float intensity, int lightCoords) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);

        // Scale slightly larger than block for glow effect
        float scale = 1.02f;
        poseStack.scale(scale, scale, scale);
        poseStack.translate(-0.5, -0.5, -0.5);

        // Convert color to packed ARGB
        int r = (int) (color[0] * 255);
        int g = (int) (color[1] * 255);
        int b = (int) (color[2] * 255);
        int a = (int) (intensity * 128); // Semi-transparent glow

        int packedColor = net.minecraft.util.ARGB.color(a, r, g, b);

        // Submit custom geometry for glow overlay
        nodeCollector.submitCustomGeometry(
                poseStack,
                RenderType.TRANSLUCENT_MOVING_BLOCK,
                (pose, consumer) -> renderGlowCubeFaces(consumer, pose, packedColor, lightCoords)
        );

        poseStack.popPose();
    }

    /**
     * Render all 6 faces of a glowing cube
     */
    private void renderGlowCubeFaces(VertexConsumer consumer, PoseStack.Pose pose, int color, int light) {
        var matrix = pose.pose();
        var normal = pose.normal();

        // Render all 6 cube faces
        // Down face (Y-)
        addVertex(consumer, matrix, normal, 0, 0, 0, 0, 1, 0, -1, 0, color, light);
        addVertex(consumer, matrix, normal, 1, 0, 0, 1, 1, 0, -1, 0, color, light);
        addVertex(consumer, matrix, normal, 1, 0, 1, 1, 0, 0, -1, 0, color, light);
        addVertex(consumer, matrix, normal, 0, 0, 1, 0, 0, 0, -1, 0, color, light);

        // Up face (Y+)
        addVertex(consumer, matrix, normal, 0, 1, 1, 0, 0, 0, 1, 0, color, light);
        addVertex(consumer, matrix, normal, 1, 1, 1, 1, 0, 0, 1, 0, color, light);
        addVertex(consumer, matrix, normal, 1, 1, 0, 1, 1, 0, 1, 0, color, light);
        addVertex(consumer, matrix, normal, 0, 1, 0, 0, 1, 0, 1, 0, color, light);

        // North face (Z-)
        addVertex(consumer, matrix, normal, 1, 0, 0, 1, 1, 0, 0, -1, color, light);
        addVertex(consumer, matrix, normal, 0, 0, 0, 0, 1, 0, 0, -1, color, light);
        addVertex(consumer, matrix, normal, 0, 1, 0, 0, 0, 0, 0, -1, color, light);
        addVertex(consumer, matrix, normal, 1, 1, 0, 1, 0, 0, 0, -1, color, light);

        // South face (Z+)
        addVertex(consumer, matrix, normal, 0, 0, 1, 0, 1, 0, 0, 1, color, light);
        addVertex(consumer, matrix, normal, 1, 0, 1, 1, 1, 0, 0, 1, color, light);
        addVertex(consumer, matrix, normal, 1, 1, 1, 1, 0, 0, 0, 1, color, light);
        addVertex(consumer, matrix, normal, 0, 1, 1, 0, 0, 0, 0, 1, color, light);

        // West face (X-)
        addVertex(consumer, matrix, normal, 0, 0, 0, 0, 1, -1, 0, 0, color, light);
        addVertex(consumer, matrix, normal, 0, 0, 1, 1, 1, -1, 0, 0, color, light);
        addVertex(consumer, matrix, normal, 0, 1, 1, 1, 0, -1, 0, 0, color, light);
        addVertex(consumer, matrix, normal, 0, 1, 0, 0, 0, -1, 0, 0, color, light);

        // East face (X+)
        addVertex(consumer, matrix, normal, 1, 0, 1, 0, 1, 1, 0, 0, color, light);
        addVertex(consumer, matrix, normal, 1, 0, 0, 1, 1, 1, 0, 0, color, light);
        addVertex(consumer, matrix, normal, 1, 1, 0, 1, 0, 1, 0, 0, color, light);
        addVertex(consumer, matrix, normal, 1, 1, 1, 0, 0, 1, 0, 0, color, light);
    }

    /**
     * Add a single vertex to the buffer
     */
    private void addVertex(VertexConsumer consumer, org.joml.Matrix4f matrix, org.joml.Matrix3f normalMatrix,
                           float x, float y, float z, float u, float v,
                           float nx, float ny, float nz, int color, int light) {
        // Transform normal using normal matrix
        org.joml.Vector3f transformedNormal = new org.joml.Vector3f(nx, ny, nz);
        normalMatrix.transform(transformedNormal);

        consumer.addVertex(matrix, x, y, z)
                .setColor(color)
                .setUv(u, v)
                .setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(transformedNormal.x(), transformedNormal.y(), transformedNormal.z());
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return false; // Can be enhanced later for high amplitude crystals
    }

    @Override
    public int getViewDistance() {
        return 128;
    }
}
