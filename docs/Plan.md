## **Resonance Mod: Waterfall Implementation Plan**

---

## Phase Overview

```
Phase 1: Foundation
├── 1.1 Project Setup & Registry Infrastructure
├── 1.2 Basic Items & Blocks (Crystals, Raw Materials)
├── 1.3 Worldgen (Ore, Geodes, Ley Lines)
└── 1.4 Basic Tools (Tuning Fork, Resonance Meter)

Phase 2: Core Resonance System
├── 2.1 Resonance Data Model
├── 2.2 Network Graph System
├── 2.3 Wave Propagation Engine
├── 2.4 Crystal Conduit Blocks
└── 2.5 Basic Visualization

Phase 3: Generation & Consumption
├── 3.1 Generator Interface & Base Class
├── 3.2 Ley Line Tap
├── 3.3 Ambient Collector
├── 3.4 Consumer Interface & Base Class
└── 3.5 Simple Test Consumer

Phase 4: First Machines
├── 4.1 Machine Base Framework
├── 4.2 Resonant Grinder
├── 4.3 Crystal Purifier
├── 4.4 Arcane Infuser
└── 4.5 Machine GUI Framework

Phase 5: Basic Logistics
├── 5.1 Drift Conduit System
├── 5.2 Intake Funnel
├── 5.3 Output Funnel
├── 5.4 Basic Junction Routing
└── 5.5 Item Rendering in Conduits

Phase 6: Advanced Resonance
├── 6.1 Interference Calculations
├── 6.2 Standing Wave Detection
├── 6.3 Resonant Chamber
├── 6.4 Harmonic Relationships
└── 6.5 Cascade Failure System

Phase 7: Advanced Logistics
├── 7.1 Frequency-Based Sorting
├── 7.2 Attunement System
├── 7.3 Grabbers
├── 7.4 Storage Network (Nexus)
└── 7.5 Wireless Transport

Phase 8: Golems & Automation
├── 8.1 Golem Entity Base
├── 8.2 Golem AI Framework
├── 8.3 Courier Golem
├── 8.4 Instruction System
└── 8.5 Golem Maintenance

Phase 9: Polish & Integration
├── 9.1 Particles & Effects
├── 9.2 Sound Design
├── 9.3 Patchouli Guidebook
├── 9.4 Config System
└── 9.5 Cross-Mod Compatibility
```

---

## Phase 1: Foundation

### 1.1 Project Setup & Registry Infrastructure

```java
// Main mod class structure
public class ResonanceMod {
    public static final String MOD_ID = "resonance";
    
    // Deferred registers for Forge
    public static final DeferredRegister<Block> BLOCKS;
    public static final DeferredRegister<Item> ITEMS;
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES;
    public static final DeferredRegister<MenuType<?>> MENUS;
    public static final DeferredRegister<Feature<?>> FEATURES;
    
    // Custom registries
    public static final DeferredRegister<ResonanceType> RESONANCE_TYPES;
    public static final DeferredRegister<GolemType> GOLEM_TYPES;
}

// Registry classes - one per category for organization
public class ModBlocks {
    // Raw crystals
    public static final RegistryObject<Block> RAW_CRYSTAL_ORE;
    public static final RegistryObject<Block> DEEPSLATE_CRYSTAL_ORE;
    public static final RegistryObject<Block> RAW_CRYSTAL_BLOCK;
    
    // Processed crystals
    public static final RegistryObject<Block> REFINED_CRYSTAL_BLOCK;
    public static final RegistryObject<Block> SYNTHETIC_CRYSTAL_BLOCK;
    
    // Functional blocks (registered later in phases)
    public static final RegistryObject<Block> CRYSTAL_PRISM;
    public static final RegistryObject<Block> CRYSTAL_NODE;
    // ... etc
}

public class ModItems {
    public static final RegistryObject<Item> RAW_CRYSTAL_SHARD;
    public static final RegistryObject<Item> REFINED_CRYSTAL;
    public static final RegistryObject<Item> TUNING_FORK;
    public static final RegistryObject<Item> RESONANCE_METER;
    // ... etc
}

// Creative tab
public class ModCreativeTabs {
    public static final RegistryObject<CreativeModeTab> MAIN_TAB = 
        CREATIVE_TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.resonance.main"))
            .icon(() -> ModItems.REFINED_CRYSTAL.get().getDefaultInstance())
            .displayItems((params, output) -> {
                // Organized by category
                output.accept(ModItems.RAW_CRYSTAL_SHARD.get());
                // ... etc
            })
            .build());
}
```

**Deliverables:**

- Mod compiles and loads
- Empty registries ready
- Creative tab visible

---

### 1.2 Basic Items & Blocks

```java
// Crystal material hierarchy
public enum CrystalTier {
    RAW(1, 50, 100, 0.05f),        // maxFreq, maxAmplitude, attenuation/block
    REFINED(200, 500, 0.02f),
    SYNTHETIC(500, 2000, 0.005f),
    LEY_INFUSED(1000, 10000, 0.001f);
    
    public final int maxFrequency;
    public final int maxAmplitude;
    public final float attenuationPerBlock;
    
    CrystalTier(int maxFreq, int maxAmp, float atten) {
        this.maxFrequency = maxFreq;
        this.maxAmplitude = maxAmp;
        this.attenuationPerBlock = atten;
    }
}

// Base crystal block with tier
public class CrystalBlock extends Block {
    protected final CrystalTier tier;
    
    public CrystalBlock(CrystalTier tier, Properties props) {
        super(props);
        this.tier = tier;
    }
    
    public CrystalTier getTier() {
        return tier;
    }
    
    // Visual properties based on tier
    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return switch(tier) {
            case RAW -> 3;
            case REFINED -> 7;
            case SYNTHETIC -> 11;
            case LEY_INFUSED -> 15;
        };
    }
}

// Crystal ore block with fortune support
public class CrystalOreBlock extends DropExperienceBlock {
    public CrystalOreBlock(Properties props) {
        super(UniformInt.of(3, 7), props);
    }
    
    // Custom loot with fortune
    // Drops 1-3 shards, +1 per fortune level
}

// Register blocks
public class ModBlocks {
    public static final RegistryObject<Block> RAW_CRYSTAL_ORE = BLOCKS.register(
        "raw_crystal_ore",
        () -> new CrystalOreBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .strength(3.0f, 3.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.CRYSTAL))
    );
    
    public static final RegistryObject<Block> DEEPSLATE_CRYSTAL_ORE = BLOCKS.register(
        "deepslate_crystal_ore",
        () -> new CrystalOreBlock(BlockBehaviour.Properties.copy(RAW_CRYSTAL_ORE.get())
            .mapColor(MapColor.DEEPSLATE)
            .strength(4.5f, 3.0f))
    );
    
    public static final RegistryObject<Block> CRYSTAL_CLUSTER = BLOCKS.register(
        "crystal_cluster",
        () -> new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .noOcclusion()
            .randomTicks()
            .sound(SoundType.AMETHYST_CLUSTER)
            .lightLevel(state -> 5))
    );
}
```

**Deliverables:**

- Crystal ore blocks (stone and deepslate variants)
- Crystal shard item
- Crystal blocks for each tier
- Basic block/item models and textures

---

### 1.3 Worldgen

```java
// Ore generation configuration
public class ModWorldGen {
    
    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        // Crystal ore veins - similar to copper
        register(context, CRYSTAL_ORE_KEY,
            Feature.ORE,
            new OreConfiguration(
                List.of(
                    OreConfiguration.target(
                        OreFeatures.STONE_ORE_REPLACEABLES,
                        ModBlocks.RAW_CRYSTAL_ORE.get().defaultBlockState()
                    ),
                    OreConfiguration.target(
                        OreFeatures.DEEPSLATE_ORE_REPLACEABLES,
                        ModBlocks.DEEPSLATE_CRYSTAL_ORE.get().defaultBlockState()
                    )
                ),
                12  // vein size
            )
        );
    }
    
    public static void bootstrapPlacements(BootstrapContext<PlacedFeature> context) {
        // Y: -16 to 64, 8 veins per chunk
        register(context, CRYSTAL_ORE_PLACED_KEY,
            configuredFeatures.getOrThrow(CRYSTAL_ORE_KEY),
            commonOrePlacement(8, HeightRangePlacement.triangle(
                VerticalAnchor.absolute(-16),
                VerticalAnchor.absolute(64)
            ))
        );
    }
}

// Crystal geode structure
public class CrystalGeodeFeature extends Feature<GeodeConfiguration> {
    
    @Override
    public boolean place(FeaturePlaceContext<GeodeConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        
        // Generate geode shell
        int outerRadius = 4 + random.nextInt(3);
        int innerRadius = outerRadius - 2;
        
        // Outer shell - rough stone
        generateSphere(level, origin, outerRadius, 
            ModBlocks.GEODE_SHELL.get().defaultBlockState(),
            random, 0.8f);  // 80% fill for rough look
        
        // Middle layer - crystal block
        generateSphere(level, origin, outerRadius - 1,
            ModBlocks.RAW_CRYSTAL_BLOCK.get().defaultBlockState(),
            random, 0.9f);
        
        // Inner cavity - air with crystal clusters
        generateSphere(level, origin, innerRadius,
            Blocks.AIR.defaultBlockState(),
            random, 1.0f);
        
        // Crystal clusters on inner walls
        placeClustersOnSurface(level, origin, innerRadius, random);
        
        return true;
    }
    
    private void placeClustersOnSurface(WorldGenLevel level, BlockPos center, 
                                         int radius, RandomSource random) {
        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-radius, -radius, -radius),
                center.offset(radius, radius, radius))) {
            
            if (level.getBlockState(pos).isAir()) {
                // Check each direction for solid surface
                for (Direction dir : Direction.values()) {
                    BlockPos surfacePos = pos.relative(dir);
                    if (level.getBlockState(surfacePos).isSolid()) {
                        if (random.nextFloat() < 0.3f) {
                            level.setBlock(pos, 
                                ModBlocks.CRYSTAL_CLUSTER.get()
                                    .defaultBlockState()
                                    .setValue(FACING, dir.getOpposite()),
                                Block.UPDATE_ALL);
                        }
                        break;
                    }
                }
            }
        }
    }
}

// Ley Line generation - stored as capability on chunks
public class LeyLineGenerator {
    
    // Ley lines are generated deterministically from world seed
    // They form a network across the world
    
    public static LeyLineData generateForChunk(long worldSeed, ChunkPos chunk) {
        Random random = new Random(worldSeed ^ chunk.toLong());
        
        LeyLineData data = new LeyLineData();
        
        // Major ley lines every ~500 blocks
        // Minor ley lines every ~100 blocks
        // Lines have direction and strength
        
        // Check if major ley line passes through this chunk
        int majorGridX = Math.floorDiv(chunk.x, 32);  // 32 chunks = 512 blocks
        int majorGridZ = Math.floorDiv(chunk.z, 32);
        
        long majorSeed = worldSeed ^ ((long)majorGridX << 32 | majorGridZ);
        Random majorRandom = new Random(majorSeed);
        
        // Major line parameters for this grid cell
        float majorAngle = majorRandom.nextFloat() * (float)Math.PI;
        int majorStrength = 100 + majorRandom.nextInt(200);  // 100-300 amplitude
        
        // Calculate if line intersects this chunk
        Vec2 lineStart = calculateLineStart(majorGridX, majorGridZ, majorAngle);
        Vec2 lineEnd = calculateLineEnd(majorGridX, majorGridZ, majorAngle);
        
        if (chunkIntersectsLine(chunk, lineStart, lineEnd)) {
            data.addLeyLine(new LeyLine(
                LeyLineType.MAJOR,
                majorAngle,
                majorStrength,
                calculateIntersectionPoint(chunk, lineStart, lineEnd)
            ));
        }
        
        // Similar for minor lines at finer grid
        // ...
        
        return data;
    }
}

// Ley line data saved with chunk
public class LeyLineData implements INBTSerializable<CompoundTag> {
    private final List<LeyLine> leyLines = new ArrayList<>();
    
    public record LeyLine(
        LeyLineType type,
        float angle,
        int strength,
        BlockPos intersectionPoint
    ) {}
    
    public boolean hasLeyLine() {
        return !leyLines.isEmpty();
    }
    
    public Optional<LeyLine> getStrongestLine() {
        return leyLines.stream()
            .max(Comparator.comparingInt(LeyLine::strength));
    }
    
    public int getTotalStrength() {
        return leyLines.stream()
            .mapToInt(LeyLine::strength)
            .sum();
    }
    
    // Intersection points are where multiple lines cross
    // These are prime locations for Ley Taps
    public Optional<BlockPos> getNexusPoint() {
        if (leyLines.size() < 2) return Optional.empty();
        // Calculate intersection of lines
        // ...
    }
}

// Chunk capability attachment
public class ModCapabilities {
    public static final ResourceLocation LEY_LINE_DATA = 
        ResourceLocation.fromNamespaceAndPath(MOD_ID, "ley_line_data");
    
    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<LevelChunk> event) {
        LeyLineData data = LeyLineGenerator.generateForChunk(
            event.getObject().getLevel().getSeed(),
            event.getObject().getPos()
        );
        
        event.addCapability(LEY_LINE_DATA, new LeyLineProvider(data));
    }
}
```

**Deliverables:**

- Crystal ore spawns naturally
- Crystal geodes generate (rare)
- Ley line data generated per chunk (invisible for now)

---

### 1.4 Basic Tools

```java
// Tuning Fork - configures resonance devices
public class TuningForkItem extends Item {
    
    public TuningForkItem(Properties props) {
        super(props.stacksTo(1));
    }
    
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        
        if (state.getBlock() instanceof IResonanceConfigurable configurable) {
            if (level.isClientSide()) {
                // Open configuration GUI
                openConfigScreen(pos, configurable);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        
        if (state.getBlock() instanceof IResonanceNode node) {
            if (context.getPlayer().isShiftKeyDown()) {
                // Shift-click: show network debug info
                if (!level.isClientSide()) {
                    showNetworkDebug(context.getPlayer(), level, pos);
                }
            } else {
                // Normal click: ping network
                if (!level.isClientSide()) {
                    pingNetwork(level, pos);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        
        return InteractionResult.PASS;
    }
    
    private void pingNetwork(Level level, BlockPos pos) {
        // Send visual pulse along all connected crystals
        ResonanceNetworkManager manager = ResonanceNetworkManager.get(level);
        ResonanceNetwork network = manager.getNetworkAt(pos);
        
        if (network != null) {
            network.triggerDebugPing(pos);
        }
    }
}

// Resonance Meter - shows wave properties
public class ResonanceMeterItem extends Item {
    
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        
        if (level.getBlockEntity(pos) instanceof IResonanceNode node) {
            if (!level.isClientSide()) {
                ResonanceState state = node.getResonanceState();
                
                Player player = context.getPlayer();
                player.sendSystemMessage(Component.literal("§6=== Resonance Reading ==="));
                player.sendSystemMessage(Component.literal(
                    String.format("§eFrequency: §f%.1f Hz", state.frequency())
                ));
                player.sendSystemMessage(Component.literal(
                    String.format("§eAmplitude: §f%.1f A", state.amplitude())
                ));
                player.sendSystemMessage(Component.literal(
                    String.format("§ePhase: §f%.1f°", Math.toDegrees(state.phase()))
                ));
                
                // Show crystal tier limits
                if (node instanceof CrystalBlockEntity crystal) {
                    CrystalTier tier = crystal.getTier();
                    float usage = state.amplitude() / tier.maxAmplitude * 100;
                    player.sendSystemMessage(Component.literal(
                        String.format("§eCapacity: §f%.1f%% §7(%s)", usage, tier.name())
                    ));
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        
        // Check for ley lines
        LeyLineData leyData = getLeyLineData(level, pos);
        if (leyData != null && leyData.hasLeyLine()) {
            if (!level.isClientSide()) {
                Player player = context.getPlayer();
                player.sendSystemMessage(Component.literal("§d=== Ley Line Detected ==="));
                leyData.getStrongestLine().ifPresent(line -> {
                    player.sendSystemMessage(Component.literal(
                        String.format("§5Type: §f%s", line.type().name())
                    ));
                    player.sendSystemMessage(Component.literal(
                        String.format("§5Strength: §f%d A", line.strength())
                    ));
                });
                if (leyData.getNexusPoint().isPresent()) {
                    player.sendSystemMessage(Component.literal(
                        "§d§lNEXUS POINT DETECTED"
                    ));
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        
        return InteractionResult.PASS;
    }
}

// Divining Rod - helps find ley lines
public class DiviningRodItem extends Item {
    
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, 
                              int slot, boolean selected) {
        if (!selected || level.isClientSide() || !(entity instanceof Player player)) {
            return;
        }
        
        // Only check every 10 ticks
        if (level.getGameTime() % 10 != 0) return;
        
        // Check nearby chunks for ley lines
        BlockPos playerPos = player.blockPosition();
        int searchRadius = 3;  // chunks
        
        LeyLine strongest = null;
        double closestDist = Double.MAX_VALUE;
        
        for (int cx = -searchRadius; cx <= searchRadius; cx++) {
            for (int cz = -searchRadius; cz <= searchRadius; cz++) {
                ChunkPos chunkPos = new ChunkPos(
                    playerPos.getX() >> 4 + cx,
                    playerPos.getZ() >> 4 + cz
                );
                
                LeyLineData data = getLeyLineData(level, chunkPos);
                if (data != null) {
                    for (LeyLine line : data.getLeyLines()) {
                        double dist = line.intersectionPoint()
                            .distSqr(playerPos);
                        if (dist < closestDist) {
                            closestDist = dist;
                            strongest = line;
                        }
                    }
                }
            }
        }
        
        // Show intensity via action bar
        if (strongest != null) {
            double dist = Math.sqrt(closestDist);
            int intensity = (int) Math.max(0, 10 - dist / 16);
            
            String bar = "§5" + "▮".repeat(intensity) + 
                        "§8" + "▯".repeat(10 - intensity);
            
            player.displayClientMessage(
                Component.literal("Ley Line: " + bar),
                true
            );
        }
    }
}
```

**Deliverables:**

- Tuning Fork item (UI placeholder for now)
- Resonance Meter shows readings
- Divining Rod detects ley lines

---

## Phase 2: Core Resonance System

### 2.1 Resonance Data Model

```java
// Immutable resonance state at a point
public record ResonanceState(
    float frequency,    // Hz, 0 = no signal
    float amplitude,    // A, power level
    float phase         // radians, 0-2π
) {
    public static final ResonanceState EMPTY = new ResonanceState(0, 0, 0);
    
    public boolean isActive() {
        return amplitude > 0.01f;
    }
    
    // Combine two waves (superposition)
    public ResonanceState combine(ResonanceState other) {
        if (!this.isActive()) return other;
        if (!other.isActive()) return this;
        
        // Same frequency: add amplitudes considering phase
        if (Math.abs(this.frequency - other.frequency) < 0.1f) {
            // Phasor addition
            float ax = this.amplitude * (float)Math.cos(this.phase);
            float ay = this.amplitude * (float)Math.sin(this.phase);
            float bx = other.amplitude * (float)Math.cos(other.phase);
            float by = other.amplitude * (float)Math.sin(other.phase);
            
            float rx = ax + bx;
            float ry = ay + by;
            
            float resultAmp = (float)Math.sqrt(rx*rx + ry*ry);
            float resultPhase = (float)Math.atan2(ry, rx);
            
            return new ResonanceState(this.frequency, resultAmp, resultPhase);
        }
        
        // Different frequencies: beat frequency (complex case)
        // For now, return the stronger signal
        return this.amplitude > other.amplitude ? this : other;
    }
    
    // Apply attenuation over distance
    public ResonanceState attenuate(float attenuationPerBlock, float distance) {
        float remaining = amplitude * (float)Math.pow(1 - attenuationPerBlock, distance);
        return new ResonanceState(frequency, remaining, phase);
    }
    
    // Advance phase based on time
    public ResonanceState tick(float deltaTime) {
        float newPhase = (phase + frequency * deltaTime * 2 * (float)Math.PI) 
            % (2 * (float)Math.PI);
        return new ResonanceState(frequency, amplitude, newPhase);
    }
}

// Capability for blocks that participate in resonance networks
public interface IResonanceNode {
    
    // What tier is this node?
    CrystalTier getTier();
    
    // Current resonance state at this node
    ResonanceState getResonanceState();
    
    // Receive resonance from adjacent node
    void receiveResonance(Direction from, ResonanceState state);
    
    // Which directions can connect?
    Set<Direction> getConnectionDirections();
    
    // Is this node a source (generator)?
    default boolean isSource() { return false; }
    
    // Is this node a sink (consumer)?
    default boolean isSink() { return false; }
    
    // Called when network topology changes
    void onNetworkChanged();
}

// Generator interface
public interface IResonanceGenerator extends IResonanceNode {
    
    @Override
    default boolean isSource() { return true; }
    
    // What resonance does this generate?
    ResonanceState getGeneratedResonance();
    
    // Can this generator currently produce?
    boolean canGenerate();
}

// Consumer interface  
public interface IResonanceConsumer extends IResonanceNode {
    
    @Override
    default boolean isSink() { return true; }
    
    // What does this machine need to operate?
    ResonanceRequirement getRequirement();
    
    // How much amplitude does this consume per tick?
    float getAmplitudeConsumption();
    
    // Is the current resonance sufficient?
    default boolean isPowered() {
        return getRequirement().isSatisfiedBy(getResonanceState());
    }
}

// Machine requirements
public record ResonanceRequirement(
    float minFrequency,
    float maxFrequency,
    float minAmplitude,
    boolean requiresExactFrequency  // false = range, true = exact match
) {
    // Common factory methods
    public static ResonanceRequirement any(float minAmplitude) {
        return new ResonanceRequirement(0, Float.MAX_VALUE, minAmplitude, false);
    }
    
    public static ResonanceRequirement range(float minFreq, float maxFreq, float minAmp) {
        return new ResonanceRequirement(minFreq, maxFreq, minAmp, false);
    }
    
    public static ResonanceRequirement exact(float frequency, float minAmp, float tolerance) {
        return new ResonanceRequirement(
            frequency - tolerance, 
            frequency + tolerance, 
            minAmp, 
            true
        );
    }
    
    public boolean isSatisfiedBy(ResonanceState state) {
        if (state.amplitude() < minAmplitude) return false;
        if (state.frequency() < minFrequency) return false;
        if (state.frequency() > maxFrequency) return false;
        return true;
    }
    
    public float getEfficiency(ResonanceState state) {
        if (!isSatisfiedBy(state)) return 0;
        
        if (requiresExactFrequency) {
            float center = (minFrequency + maxFrequency) / 2;
            float deviation = Math.abs(state.frequency() - center);
            float tolerance = (maxFrequency - minFrequency) / 2;
            return 1.0f - (deviation / tolerance) * 0.5f;  // 50-100% efficiency
        }
        
        return 1.0f;
    }
}
```

**Deliverables:**

- Core data types defined
- Interfaces for nodes, generators, consumers
- Wave math implemented

---

### 2.2 Network Graph System

```java
// Manages all resonance networks in a level
public class ResonanceNetworkManager extends SavedData {
    
    private final Level level;
    private final Map<UUID, ResonanceNetwork> networks = new HashMap<>();
    private final Map<BlockPos, UUID> positionToNetwork = new HashMap<>();
    
    // Dirty tracking for updates
    private final Set<UUID> networksNeedingUpdate = new HashSet<>();
    
    public static ResonanceNetworkManager get(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.getDataStorage().computeIfAbsent(
                tag -> load(serverLevel, tag),
                () -> new ResonanceNetworkManager(serverLevel),
                "resonance_networks"
            );
        }
        throw new IllegalArgumentException("Cannot get manager for client level");
    }
    
    // Called when a resonance node is placed
    public void onNodePlaced(BlockPos pos, IResonanceNode node) {
        // Check for adjacent networks
        Set<UUID> adjacentNetworks = new HashSet<>();
        
        for (Direction dir : node.getConnectionDirections()) {
            BlockPos adjacent = pos.relative(dir);
            UUID adjacentNetwork = positionToNetwork.get(adjacent);
            if (adjacentNetwork != null) {
                // Verify the adjacent node can connect back to us
                if (canConnect(adjacent, dir.getOpposite())) {
                    adjacentNetworks.add(adjacentNetwork);
                }
            }
        }
        
        if (adjacentNetworks.isEmpty()) {
            // Create new network with just this node
            createNetwork(pos, node);
        } else if (adjacentNetworks.size() == 1) {
            // Add to existing network
            UUID networkId = adjacentNetworks.iterator().next();
            addToNetwork(networkId, pos, node);
        } else {
            // Merge multiple networks
            mergeNetworks(adjacentNetworks, pos, node);
        }
        
        setDirty();
    }
    
    // Called when a resonance node is removed
    public void onNodeRemoved(BlockPos pos) {
        UUID networkId = positionToNetwork.remove(pos);
        if (networkId == null) return;
        
        ResonanceNetwork network = networks.get(networkId);
        if (network == null) return;
        
        network.removeNode(pos);
        
        // Check if network needs to be split
        if (network.needsSplitCheck()) {
            splitNetworkIfNeeded(network);
        }
        
        // Remove empty networks
        if (network.isEmpty()) {
            networks.remove(networkId);
        }
        
        setDirty();
    }
    
    // Get network at position
    public ResonanceNetwork getNetworkAt(BlockPos pos) {
        UUID networkId = positionToNetwork.get(pos);
        return networkId != null ? networks.get(networkId) : null;
    }
    
    // Tick all networks
    public void tick() {
        // First, update any networks that need recalculation
        for (UUID networkId : networksNeedingUpdate) {
            ResonanceNetwork network = networks.get(networkId);
            if (network != null) {
                network.recalculate();
            }
        }
        networksNeedingUpdate.clear();
        
        // Then tick all networks
        for (ResonanceNetwork network : networks.values()) {
            network.tick();
        }
    }
    
    private void createNetwork(BlockPos pos, IResonanceNode node) {
        UUID id = UUID.randomUUID();
        ResonanceNetwork network = new ResonanceNetwork(id, level);
        network.addNode(pos, node);
        networks.put(id, network);
        positionToNetwork.put(pos, id);
    }
    
    private void mergeNetworks(Set<UUID> toMerge, BlockPos newPos, IResonanceNode newNode) {
        // Pick the largest network as the base
        UUID baseId = toMerge.stream()
            .max(Comparator.comparingInt(id -> networks.get(id).size()))
            .orElseThrow();
        
        ResonanceNetwork base = networks.get(baseId);
        
        // Merge others into base
        for (UUID otherId : toMerge) {
            if (otherId.equals(baseId)) continue;
            
            ResonanceNetwork other = networks.remove(otherId);
            for (BlockPos pos : other.getAllPositions()) {
                base.addNode(pos, other.getNode(pos));
                positionToNetwork.put(pos, baseId);
            }
        }
        
        // Add the new node
        base.addNode(newPos, newNode);
        positionToNetwork.put(newPos, baseId);
        
        // Mark for recalculation
        networksNeedingUpdate.add(baseId);
    }
    
    private void splitNetworkIfNeeded(ResonanceNetwork network) {
        // Use BFS to find connected components
        Set<BlockPos> allPositions = new HashSet<>(network.getAllPositions());
        List<Set<BlockPos>> components = new ArrayList<>();
        
        while (!allPositions.isEmpty()) {
            BlockPos start = allPositions.iterator().next();
            Set<BlockPos> component = new HashSet<>();
            Queue<BlockPos> queue = new LinkedList<>();
            queue.add(start);
            
            while (!queue.isEmpty()) {
                BlockPos current = queue.poll();
                if (!allPositions.remove(current)) continue;
                component.add(current);
                
                IResonanceNode node = network.getNode(current);
                for (Direction dir : node.getConnectionDirections()) {
                    BlockPos neighbor = current.relative(dir);
                    if (allPositions.contains(neighbor) && 
                        canConnect(neighbor, dir.getOpposite())) {
                        queue.add(neighbor);
                    }
                }
            }
            
            components.add(component);
        }
        
        // If only one component, no split needed
        if (components.size() <= 1) return;
        
        // Keep the largest component in the original network
        components.sort((a, b) -> b.size() - a.size());
        Set<BlockPos> keepInOriginal = components.remove(0);
        
        // Clear the original network and re-add kept positions
        UUID originalId = network.getId();
        network.clear();
        for (BlockPos pos : keepInOriginal) {
            IResonanceNode node = getNodeFromLevel(pos);
            if (node != null) {
                network.addNode(pos, node);
            }
        }
        
        // Create new networks for remaining components
        for (Set<BlockPos> component : components) {
            UUID newId = UUID.randomUUID();
            ResonanceNetwork newNetwork = new ResonanceNetwork(newId, level);
            
            for (BlockPos pos : component) {
                IResonanceNode node = getNodeFromLevel(pos);
                if (node != null) {
                    newNetwork.addNode(pos, node);
                    positionToNetwork.put(pos, newId);
                }
            }
            
            networks.put(newId, newNetwork);
        }
    }
}

// Individual resonance network
public class ResonanceNetwork {
    private final UUID id;
    private final Level level;
    
    // Graph structure
    private final Map<BlockPos, IResonanceNode> nodes = new HashMap<>();
    private final Map<BlockPos, Set<BlockPos>> adjacency = new HashMap<>();
    
    // Computed state
    private final Map<BlockPos, ResonanceState> computedStates = new HashMap<>();
    private boolean needsRecalculation = true;
    
    // Sources and sinks for efficient iteration
    private final Set<BlockPos> sources = new HashSet<>();
    private final Set<BlockPos> sinks = new HashSet<>();
    
    public void addNode(BlockPos pos, IResonanceNode node) {
        nodes.put(pos, node);
        adjacency.put(pos, new HashSet<>());
        
        // Update adjacency for this and neighbors
        for (Direction dir : node.getConnectionDirections()) {
            BlockPos neighbor = pos.relative(dir);
            if (nodes.containsKey(neighbor)) {
                IResonanceNode neighborNode = nodes.get(neighbor);
                if (neighborNode.getConnectionDirections().contains(dir.getOpposite())) {
                    adjacency.get(pos).add(neighbor);
                    adjacency.get(neighbor).add(pos);
                }
            }
        }
        
        if (node.isSource()) sources.add(pos);
        if (node.isSink()) sinks.add(pos);
        
        needsRecalculation = true;
    }
    
    public void recalculate() {
        if (!needsRecalculation) return;
        needsRecalculation = false;
        
        // Clear previous state
        computedStates.clear();
        
        // Propagate from all sources
        for (BlockPos sourcePos : sources) {
            IResonanceGenerator generator = (IResonanceGenerator) nodes.get(sourcePos);
            if (generator.canGenerate()) {
                propagateFrom(sourcePos, generator.getGeneratedResonance());
            }
        }
        
        // Notify all nodes of their new state
        for (Map.Entry<BlockPos, ResonanceState> entry : computedStates.entrySet()) {
            IResonanceNode node = nodes.get(entry.getKey());
            if (node != null) {
                // This updates the block entity
                updateNodeState(entry.getKey(), entry.getValue());
            }
        }
    }
    
    private void propagateFrom(BlockPos source, ResonanceState initialState) {
        // BFS propagation with attenuation
        Map<BlockPos, Float> distances = new HashMap<>();
        PriorityQueue<PropagationStep> queue = new PriorityQueue<>(
            Comparator.comparingDouble(s -> s.distance)
        );
        
        queue.add(new PropagationStep(source, 0, initialState));
        distances.put(source, 0f);
        
        while (!queue.isEmpty()) {
            PropagationStep step = queue.poll();
            
            // Skip if we've found a shorter path
            Float knownDist = distances.get(step.pos);
            if (knownDist != null && knownDist < step.distance) continue;
            
            // Apply this state (combining with existing)
            ResonanceState existing = computedStates.getOrDefault(
                step.pos, ResonanceState.EMPTY);
            ResonanceState combined = existing.combine(step.state);
            computedStates.put(step.pos, combined);
            
            // Get node for tier info
            IResonanceNode node = nodes.get(step.pos);
            if (node == null) continue;
            
            // Stop if amplitude too low
            if (step.state.amplitude() < 0.1f) continue;
            
            // Propagate to neighbors
            for (BlockPos neighbor : adjacency.getOrDefault(step.pos, Set.of())) {
                float edgeLength = 1.0f;  // Could vary based on crystal type
                float newDist = step.distance + edgeLength;
                
                // Attenuate based on crystal tier
                IResonanceNode neighborNode = nodes.get(neighbor);
                if (neighborNode == null) continue;
                
                ResonanceState attenuated = step.state.attenuate(
                    neighborNode.getTier().attenuationPerBlock,
                    edgeLength
                );
                
                // Check capacity limits
                if (attenuated.amplitude() > neighborNode.getTier().maxAmplitude) {
                    // Over capacity - could trigger cascade
                    handleOverCapacity(neighbor, attenuated.amplitude());
                    attenuated = new ResonanceState(
                        attenuated.frequency(),
                        neighborNode.getTier().maxAmplitude,
                        attenuated.phase()
                    );
                }
                
                Float neighborDist = distances.get(neighbor);
                if (neighborDist == null || newDist < neighborDist) {
                    distances.put(neighbor, newDist);
                    queue.add(new PropagationStep(neighbor, newDist, attenuated));
                }
            }
        }
    }
    
    private record PropagationStep(BlockPos pos, float distance, ResonanceState state) {}
    
    public void tick() {
        // Advance phase for all states (visual effect)
        float deltaTime = 1.0f / 20.0f;  // One tick
        
        for (Map.Entry<BlockPos, ResonanceState> entry : computedStates.entrySet()) {
            ResonanceState ticked = entry.getValue().tick(deltaTime);
            entry.setValue(ticked);
        }
        
        // Apply consumption from sinks
        boolean consumed = false;
        for (BlockPos sinkPos : sinks) {
            IResonanceConsumer consumer = (IResonanceConsumer) nodes.get(sinkPos);
            if (consumer != null && consumer.isPowered()) {
                // Reduce amplitude at sink
                ResonanceState current = computedStates.get(sinkPos);
                if (current != null) {
                    float consumption = consumer.getAmplitudeConsumption();
                    float newAmp = Math.max(0, current.amplitude() - consumption);
                    computedStates.put(sinkPos, new ResonanceState(
                        current.frequency(), newAmp, current.phase()
                    ));
                    consumed = true;
                }
            }
        }
        
        // If consumption changed network significantly, recalculate
        // (This is a simplification - real impl might be more sophisticated)
        if (consumed) {
            needsRecalculation = true;
        }
    }
}
```

**Deliverables:**

- Network manager tracks all networks per level
- Networks merge and split correctly
- BFS propagation calculates resonance distribution

---

### 2.3 Wave Propagation Engine

```java
// Handles the physics simulation aspects
public class WavePropagationEngine {
    
    private final ResonanceNetwork network;
    private final Map<BlockPos, WaveState> waveStates = new HashMap<>();
    
    // For standing wave detection
    private final Map<BlockPos, RingBuffer<Float>> amplitudeHistory = new HashMap<>();
    private static final int HISTORY_SIZE = 40;  // 2 seconds at 20 TPS
    
    public void tick() {
        // Phase advancement
        advancePhases();
        
        // Interference calculation at junction points
        calculateInterference();
        
        // Standing wave detection
        detectStandingWaves();
        
        // Cascade detection
        checkForCascades();
    }
    
    private void advancePhases() {
        float dt = 1.0f / 20.0f;  // Tick time
        
        for (Map.Entry<BlockPos, WaveState> entry : waveStates.entrySet()) {
            WaveState state = entry.getValue();
            
            // Phase advances by 2π * frequency per second
            float phaseAdvance = 2 * (float)Math.PI * state.frequency * dt;
            float newPhase = (state.phase + phaseAdvance) % (2 * (float)Math.PI);
            
            entry.setValue(state.withPhase(newPhase));
        }
    }
    
    private void calculateInterference() {
        // Find junction points (nodes with 3+ connections)
        for (BlockPos pos : network.getAllPositions()) {
            Set<BlockPos> neighbors = network.getNeighbors(pos);
            if (neighbors.size() < 2) continue;  // Not a junction
            
            // Collect incoming waves
            List<WaveState> incomingWaves = new ArrayList<>();
            for (BlockPos neighbor : neighbors) {
                WaveState neighborState = waveStates.get(neighbor);
                if (neighborState != null && neighborState.amplitude > 0.01f) {
                    // Check if wave is traveling toward this junction
                    Direction flowDir = getFlowDirection(neighbor, pos);
                    if (flowDir != null) {
                        incomingWaves.add(neighborState);
                    }
                }
            }
            
            if (incomingWaves.size() < 2) continue;
            
            // Calculate superposition
            WaveState combined = superpose(incomingWaves);
            waveStates.put(pos, combined);
            
            // Check for destructive interference
            float maxPossible = incomingWaves.stream()
                .map(w -> w.amplitude)
                .reduce(0f, Float::sum);
            
            float interferenceRatio = combined.amplitude / maxPossible;
            
            if (interferenceRatio < 0.3f) {
                // Significant destructive interference - warn player
                triggerInterferenceWarning(pos);
            }
        }
    }
    
    private WaveState superpose(List<WaveState> waves) {
        // Group waves by frequency (with tolerance)
        Map<Float, List<WaveState>> byFrequency = new HashMap<>();
        float tolerance = 0.5f;  // Hz
        
        for (WaveState wave : waves) {
            Float matchingFreq = byFrequency.keySet().stream()
                .filter(f -> Math.abs(f - wave.frequency) < tolerance)
                .findFirst()
                .orElse(null);
            
            if (matchingFreq != null) {
                byFrequency.get(matchingFreq).add(wave);
            } else {
                List<WaveState> list = new ArrayList<>();
                list.add(wave);
                byFrequency.put(wave.frequency, list);
            }
        }
        
        // For each frequency group, do phasor addition
        List<WaveState> combinedByFreq = new ArrayList<>();
        
        for (Map.Entry<Float, List<WaveState>> entry : byFrequency.entrySet()) {
            float freq = entry.getKey();
            List<WaveState> group = entry.getValue();
            
            // Phasor addition
            float realSum = 0, imagSum = 0;
            for (WaveState w : group) {
                realSum += w.amplitude * Math.cos(w.phase);
                imagSum += w.amplitude * Math.sin(w.phase);
            }
            
            float resultAmp = (float)Math.sqrt(realSum*realSum + imagSum*imagSum);
            float resultPhase = (float)Math.atan2(imagSum, realSum);
            
            combinedByFreq.add(new WaveState(freq, resultAmp, resultPhase));
        }
        
        // If multiple frequencies remain, take the dominant one
        // (Full multi-frequency support is Phase 6)
        return combinedByFreq.stream()
            .max(Comparator.comparingDouble(w -> w.amplitude))
            .orElse(WaveState.EMPTY);
    }
    
    private void detectStandingWaves() {
        for (BlockPos pos : network.getAllPositions()) {
            WaveState current = waveStates.get(pos);
            if (current == null) continue;
            
            // Track amplitude over time
            RingBuffer<Float> history = amplitudeHistory.computeIfAbsent(
                pos, k -> new RingBuffer<>(HISTORY_SIZE)
            );
            history.add(current.amplitude);
            
            if (!history.isFull()) continue;
            
            // Standing wave signature: amplitude oscillates around a stable mean
            float mean = history.average();
            float variance = history.variance();
            float normalizedVariance = variance / (mean * mean + 0.001f);
            
            // Low variance = stable standing wave
            if (normalizedVariance < 0.1f && mean > 10f) {
                markAsStandingWave(pos, mean);
            } else {
                unmarkStandingWave(pos);
            }
        }
    }
    
    private void checkForCascades() {
        List<BlockPos> overloaded = new ArrayList<>();
        
        for (BlockPos pos : network.getAllPositions()) {
            WaveState state = waveStates.get(pos);
            if (state == null) continue;
            
            IResonanceNode node = network.getNode(pos);
            if (node == null) continue;
            
            float maxAmplitude = node.getTier().maxAmplitude;
            float ratio = state.amplitude / maxAmplitude;
            
            if (ratio > 1.5f) {
                // Severely overloaded - immediate cascade
                overloaded.add(pos);
            } else if (ratio > 1.0f) {
                // Moderately overloaded - accumulate stress
                accumulateStress(pos, ratio - 1.0f);
            } else {
                // Normal - relieve stress
                relieveStress(pos);
            }
        }
        
        if (!overloaded.isEmpty()) {
            triggerCascade(overloaded);
        }
    }
    
    private final Map<BlockPos, Float> stressAccumulator = new HashMap<>();
    private static final float STRESS_THRESHOLD = 10f;  // Seconds of mild overload
    
    private void accumulateStress(BlockPos pos, float amount) {
        float current = stressAccumulator.getOrDefault(pos, 0f);
        float newStress = current + amount / 20f;  // Per tick
        
        if (newStress >= STRESS_THRESHOLD) {
            triggerCascade(List.of(pos));
            stressAccumulator.remove(pos);
        } else {
            stressAccumulator.put(pos, newStress);
        }
    }
    
    private void triggerCascade(List<BlockPos> epicenters) {
        Level level = network.getLevel();
        Set<BlockPos> destroyed = new HashSet<>();
        Queue<BlockPos> toProcess = new LinkedList<>(epicenters);
        
        while (!toProcess.isEmpty()) {
            BlockPos pos = toProcess.poll();
            if (destroyed.contains(pos)) continue;
            
            // Destroy this crystal
            destroyed.add(pos);
            level.destroyBlock(pos, false);
            
            // Explosion effect
            level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                1.0f, Level.ExplosionInteraction.NONE);
            
            // Chance to propagate to neighbors
            WaveState state = waveStates.get(pos);
            float propagationChance = state != null ? 
                Math.min(0.8f, state.amplitude / 500f) : 0.2f;
            
            for (BlockPos neighbor : network.getNeighbors(pos)) {
                if (!destroyed.contains(neighbor) && 
                    level.random.nextFloat() < propagationChance) {
                    toProcess.add(neighbor);
                }
            }
        }
        
        // Notify network manager to rebuild
        ResonanceNetworkManager.get((ServerLevel)level).rebuildNetwork(network.getId());
    }
    
    public record WaveState(float frequency, float amplitude, float phase) {
        public static final WaveState EMPTY = new WaveState(0, 0, 0);
        
        public WaveState withPhase(float newPhase) {
            return new WaveState(frequency, amplitude, newPhase);
        }
    }
}
```

**Deliverables:**

- Wave physics simulation
- Interference calculations
- Standing wave detection
- Cascade failure system

---

### 2.4 Crystal Conduit Blocks

```java
// Base crystal block entity
public abstract class CrystalBlockEntity extends BlockEntity implements IResonanceNode {
    
    protected final CrystalTier tier;
    protected ResonanceState currentState = ResonanceState.EMPTY;
    protected Set<Direction> connections = EnumSet.noneOf(Direction.class);
    
    // For rendering
    protected float renderPhase = 0;
    protected float renderAmplitude = 0;
    
    public CrystalBlockEntity(BlockEntityType<?> type, BlockPos pos, 
                              BlockState state, CrystalTier tier) {
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
        // Called by network propagation
        this.currentState = state;
        
        // Smooth interpolation for rendering
        this.renderAmplitude = Mth.lerp(0.1f, renderAmplitude, state.amplitude());
        
        setChanged();
    }
    
    public void clientTick() {
        // Advance render phase smoothly
        if (currentState.isActive()) {
            float targetPhase = currentState.phase();
            renderPhase = Mth.lerp(0.2f, renderPhase, targetPhase);
        }
    }
    
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putFloat("frequency", currentState.frequency());
        tag.putFloat("amplitude", currentState.amplitude());
        tag.putFloat("phase", currentState.phase());
        
        int[] connectionArray = connections.stream()
            .mapToInt(Direction::get3DDataValue)
            .toArray();
        tag.putIntArray("connections", connectionArray);
    }
    
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        currentState = new ResonanceState(
            tag.getFloat("frequency"),
            tag.getFloat("amplitude"),
            tag.getFloat("phase")
        );
        
        connections = EnumSet.noneOf(Direction.class);
        for (int i : tag.getIntArray("connections")) {
            connections.add(Direction.from3DDataValue(i));
        }
    }
}

// Crystal Prism - straight conduit
public class CrystalPrismBlock extends DirectionalBlock implements IResonanceBlock {
    
    public CrystalPrismBlock(CrystalTier tier, Properties props) {
        super(props);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, 
                               BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING).getAxis()) {
            case X -> SHAPE_X;
            case Y -> SHAPE_Y;
            case Z -> SHAPE_Z;
        };
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Align to the face player is looking at
        Direction facing = context.getNearestLookingDirection().getOpposite();
        return defaultBlockState().setValue(FACING, facing);
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }
    
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, 
                        BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide()) {
            ResonanceNetworkManager.get(level).onNodePlaced(pos, 
                (IResonanceNode) level.getBlockEntity(pos));
        }
    }
    
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, 
                         BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide()) {
                ResonanceNetworkManager.get(level).onNodeRemoved(pos);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}

public class CrystalPrismBlockEntity extends CrystalBlockEntity {
    
    public CrystalPrismBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRYSTAL_PRISM.get(), pos, state, 
              getCrystalTier(state));
    }
    
    @Override
    public Set<Direction> getConnectionDirections() {
        Direction facing = getBlockState().getValue(CrystalPrismBlock.FACING);
        return EnumSet.of(facing, facing.getOpposite());
    }
    
    @Override
    public void onNetworkChanged() {
        // Update visual connections
        updateConnections();
        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), 
                              getBlockState(), Block.UPDATE_ALL);
    }
    
    private void updateConnections() {
        connections.clear();
        for (Direction dir : getConnectionDirections()) {
            BlockPos neighbor = worldPosition.relative(dir);
            if (level.getBlockEntity(neighbor) instanceof IResonanceNode node) {
                if (node.getConnectionDirections().contains(dir.getOpposite())) {
                    connections.add(dir);
                }
            }
        }
    }
}

// Crystal Node - junction with multiple connections
public class CrystalNodeBlock extends Block implements IResonanceBlock {
    
    // Property for each face's connection state
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    
    public CrystalNodeBlock(CrystalTier tier, Properties props) {
        super(props);
        registerDefaultState(stateDefinition.any()
            .setValue(NORTH, false)
            .setValue(SOUTH, false)
            .setValue(EAST, false)
            .setValue(WEST, false)
            .setValue(UP, false)
            .setValue(DOWN, false));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }
    
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                   LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        boolean connected = canConnectTo(level, neighborPos, direction.getOpposite());
        return state.setValue(getPropertyForDirection(direction), connected);
    }
    
    private boolean canConnectTo(LevelAccessor level, BlockPos pos, Direction fromDir) {
        if (level.getBlockEntity(pos) instanceof IResonanceNode node) {
            return node.getConnectionDirections().contains(fromDir);
        }
        return false;
    }
    
    private static BooleanProperty getPropertyForDirection(Direction dir) {
        return switch (dir) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
            case UP -> UP;
            case DOWN -> DOWN;
        };
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, 
                               BlockPos pos, CollisionContext context) {
        VoxelShape shape = CORE_SHAPE;
        
        if (state.getValue(NORTH)) shape = Shapes.or(shape, NORTH_SHAPE);
        if (state.getValue(SOUTH)) shape = Shapes.or(shape, SOUTH_SHAPE);
        if (state.getValue(EAST)) shape = Shapes.or(shape, EAST_SHAPE);
        if (state.getValue(WEST)) shape = Shapes.or(shape, WEST_SHAPE);
        if (state.getValue(UP)) shape = Shapes.or(shape, UP_SHAPE);
        if (state.getValue(DOWN)) shape = Shapes.or(shape, DOWN_SHAPE);
        
        return shape;
    }
}

public class CrystalNodeBlockEntity extends CrystalBlockEntity {
    
    @Override
    public Set<Direction> getConnectionDirections() {
        // All 6 directions
        return EnumSet.allOf(Direction.class);
    }
}
```

**Deliverables:**

- Crystal Prism (straight conduit) block + entity
- Crystal Node (junction) block + entity
- Automatic connection detection
- Network integration

---

### 2.5 Basic Visualization

```java
// Block entity renderer for crystals
public class CrystalBlockEntityRenderer<T extends CrystalBlockEntity> 
        implements BlockEntityRenderer<T> {
    
    private static final ResourceLocation GLOW_TEXTURE = 
        ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/block/crystal_glow.png");
    
    @Override
    public void render(T blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        
        ResonanceState state = blockEntity.getResonanceState();
        if (!state.isActive()) return;
        
        float amplitude = blockEntity.renderAmplitude;
        float phase = blockEntity.renderPhase;
        
        // Glow intensity based on amplitude
        float intensity = Mth.clamp(amplitude / 100f, 0.1f, 1.0f);
        
        // Pulse based on phase
        float pulse = (float)(Math.sin(phase) * 0.5 + 0.5);
        float finalIntensity = intensity * (0.7f + pulse * 0.3f);
        
        // Color based on frequency
        float[] color = getColorForFrequency(state.frequency());
        
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        
        // Render glow overlay
        VertexConsumer buffer = bufferSource.getBuffer(
            RenderType.entityTranslucentEmissive(GLOW_TEXTURE));
        
        renderGlowCube(poseStack, buffer, 
            color[0], color[1], color[2], finalIntensity,
            packedLight, packedOverlay);
        
        // Render energy particles inside crystal
        if (amplitude > 20f) {
            renderEnergyParticles(blockEntity, poseStack, bufferSource, 
                phase, amplitude, partialTick);
        }
        
        poseStack.popPose();
    }
    
    private float[] getColorForFrequency(float frequency) {
        // Map frequency to hue
        // 0-50 Hz = red/orange
        // 50-100 Hz = yellow/green
        // 100-200 Hz = cyan/blue
        // 200+ Hz = purple/magenta
        
        float hue;
        if (frequency < 50) {
            hue = frequency / 50f * 0.1f;  // 0-0.1 (red to orange)
        } else if (frequency < 100) {
            hue = 0.1f + (frequency - 50) / 50f * 0.2f;  // 0.1-0.3 (orange to green)
        } else if (frequency < 200) {
            hue = 0.3f + (frequency - 100) / 100f * 0.35f;  // 0.3-0.65 (green to blue)
        } else {
            hue = 0.65f + Math.min((frequency - 200) / 300f, 1.0f) * 0.2f;  // 0.65-0.85 (blue to purple)
        }
        
        return hsvToRgb(hue, 0.8f, 1.0f);
    }
    
    private void renderEnergyParticles(T blockEntity, PoseStack poseStack,
                                        MultiBufferSource bufferSource,
                                        float phase, float amplitude, float partialTick) {
        // Particles move along the crystal axis
        int particleCount = (int)(amplitude / 20f);
        particleCount = Mth.clamp(particleCount, 1, 10);
        
        Set<Direction> connections = blockEntity.getConnectionDirections();
        
        VertexConsumer buffer = bufferSource.getBuffer(
            RenderType.entityTranslucentEmissive(PARTICLE_TEXTURE));
        
        for (Direction dir : connections) {
            Vec3 axis = Vec3.atLowerCornerOf(dir.getNormal());
            
            for (int i = 0; i < particleCount; i++) {
                // Each particle at different phase offset
                float particlePhase = (phase + i * (float)Math.PI * 2 / particleCount) 
                    % ((float)Math.PI * 2);
                
                // Position along axis based on phase
                float t = (float)(Math.sin(particlePhase) * 0.5 + 0.5);  // 0-1
                
                Vec3 pos = axis.scale(t * 0.4 - 0.2);  // -0.2 to 0.2 from center
                
                poseStack.pushPose();
                poseStack.translate(pos.x, pos.y, pos.z);
                
                float size = 0.05f + amplitude / 1000f;
                renderParticle(poseStack, buffer, size);
                
                poseStack.popPose();
            }
        }
    }
    
    @Override
    public boolean shouldRenderOffScreen(T blockEntity) {
        return blockEntity.getResonanceState().amplitude() > 100;
    }
}

// Particle effects
public class ResonanceParticles {
    
    public static void spawnResonancePulse(Level level, BlockPos pos, 
                                            ResonanceState state) {
        if (!level.isClientSide()) return;
        
        float[] color = getColorForFrequency(state.frequency());
        int count = (int)(state.amplitude() / 10);
        
        for (int i = 0; i < count; i++) {
            double x = pos.getX() + 0.5 + (level.random.nextDouble() - 0.5) * 0.5;
            double y = pos.getY() + 0.5 + (level.random.nextDouble() - 0.5) * 0.5;
            double z = pos.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * 0.5;
            
            level.addParticle(
                new ResonanceParticleOptions(color[0], color[1], color[2]),
                x, y, z,
                0, 0.02, 0
            );
        }
    }
    
    public static void spawnCascadeEffect(Level level, BlockPos pos) {
        // Dramatic explosion particles
        for (int i = 0; i < 50; i++) {
            double angle = level.random.nextDouble() * Math.PI * 2;
            double speed = 0.2 + level.random.nextDouble() * 0.3;
            
            double vx = Math.cos(angle) * speed;
            double vy = level.random.nextDouble() * 0.5;
            double vz = Math.sin(angle) * speed;
            
            level.addParticle(
                ParticleTypes.END_ROD,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                vx, vy, vz
            );
        }
        
        // Shockwave ring
        spawnShockwaveRing(level, pos, 3.0f);
    }
}
```

**Deliverables:**

- Crystal block entity renderer with glow effects
- Color-coded by frequency
- Amplitude-based intensity
- Phase-based pulsing
- Particle effects

---

## Phase 3: Generation & Consumption

### 3.1 Generator Base

```java
public abstract class ResonanceGeneratorBlockEntity extends BlockEntity 
        implements IResonanceGenerator {
    
    protected final CrystalTier tier;
    protected float generatedFrequency;
    protected float generatedAmplitude;
    protected boolean active = false;
    
    // For smooth animation
    protected float targetAmplitude = 0;
    protected float currentAnimatedAmplitude = 0;
    
    @Override
    public ResonanceState getGeneratedResonance() {
        if (!canGenerate()) return ResonanceState.EMPTY;
        return new ResonanceState(generatedFrequency, generatedAmplitude, 0);
    }
    
    @Override
    public boolean isSource() {
        return true;
    }
    
    @Override
    public Set<Direction> getConnectionDirections() {
        // Override in subclasses for specific output faces
        return EnumSet.allOf(Direction.class);
    }
    
    public void serverTick() {
        boolean wasActive = active;
        
        // Subclass determines if generation is possible
        active = checkCanGenerate();
        
        if (active) {
            // Calculate output based on subclass logic
            updateGeneratedResonance();
        } else {
            generatedAmplitude = 0;
        }
        
        // Notify network if state changed
        if (wasActive != active || amplitudeChanged()) {
            notifyNetworkChanged();
        }
        
        targetAmplitude = active ? generatedAmplitude : 0;
    }
    
    public void clientTick() {
        // Smooth animation
        currentAnimatedAmplitude = Mth.lerp(0.1f, 
            currentAnimatedAmplitude, targetAmplitude);
    }
    
    protected abstract boolean checkCanGenerate();
    protected abstract void updateGeneratedResonance();
    
    private void notifyNetworkChanged() {
        if (level instanceof ServerLevel serverLevel) {
            ResonanceNetworkManager.get(serverLevel)
                .markNetworkDirty(worldPosition);
        }
    }
}
```

---

### 3.2 Ley Line Tap

```java
public class LeyLineTapBlock extends Block implements IResonanceBlock {
    
    // Multiblock structure (3x3x2)
    public static final BooleanProperty FORMED = BooleanProperty.create("formed");
    public static final BooleanProperty CONTROLLER = BooleanProperty.create("controller");
    
    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, 
                                             BlockPos pos, Player player,
                                             BlockHitResult hitResult) {
        if (!level.isClientSide() && state.getValue(CONTROLLER)) {
            // Check for ley line
            LeyLineData leyData = getLeyLineData(level, pos);
            
            if (leyData == null || !leyData.hasLeyLine()) {
                player.sendSystemMessage(Component.literal(
                    "§cNo ley line detected at this location"));
                return InteractionResult.SUCCESS;
            }
            
            // Try to form multiblock
            if (!state.getValue(FORMED)) {
                if (tryFormMultiblock(level, pos)) {
                    player.sendSystemMessage(Component.literal(
                        "§aLey Line Tap activated!"));
                } else {
                    player.sendSystemMessage(Component.literal(
                        "§cIncomplete structure"));
                }
            }
            
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
    
    private boolean tryFormMultiblock(Level level, BlockPos controllerPos) {
        // Check 3x3 base layer
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos checkPos = controllerPos.offset(x, 0, z);
                if (!isValidBaseBlock(level.getBlockState(checkPos))) {
                    return false;
                }
            }
        }
        
        // Check pillar corners on second layer
        BlockPos[] pillars = {
            controllerPos.offset(-1, 1, -1),
            controllerPos.offset(1, 1, -1),
            controllerPos.offset(-1, 1, 1),
            controllerPos.offset(1, 1, 1)
        };
        
        for (BlockPos pillar : pillars) {
            if (!isValidPillarBlock(level.getBlockState(pillar))) {
                return false;
            }
        }
        
        // Form the multiblock
        level.setBlock(controllerPos, 
            level.getBlockState(controllerPos)
                .setValue(FORMED, true), 
            Block.UPDATE_ALL);
        
        return true;
    }
}

public class LeyLineTapBlockEntity extends ResonanceGeneratorBlockEntity {
    
    private LeyLine connectedLeyLine;
    private boolean multiblockFormed = false;
    
    @Override
    protected boolean checkCanGenerate() {
        if (!multiblockFormed) return false;
        if (connectedLeyLine == null) {
            // Try to find ley line
            LeyLineData data = getLeyLineData(level, worldPosition);
            if (data != null && data.hasLeyLine()) {
                connectedLeyLine = data.getStrongestLine().orElse(null);
            }
        }
        return connectedLeyLine != null;
    }
    
    @Override
    protected void updateGeneratedResonance() {
        if (connectedLeyLine == null) return;
        
        // Base frequency from ley line type
        generatedFrequency = switch (connectedLeyLine.type()) {
            case MINOR -> 30f;
            case MAJOR -> 60f;
            case NEXUS -> 100f;
        };
        
        // Amplitude from ley line strength
        generatedAmplitude = connectedLeyLine.strength();
        
        // Nexus points get bonus
        LeyLineData data = getLeyLineData(level, worldPosition);
        if (data != null && data.getNexusPoint().isPresent()) {
            generatedAmplitude *= 2;
        }
    }
    
    @Override
    public Set<Direction> getConnectionDirections() {
        // Output from the top
        return EnumSet.of(Direction.UP);
    }
    
    public void onMultiblockFormed() {
        multiblockFormed = true;
        setChanged();
        notifyNetworkChanged();
    }
    
    public void onMultiblockBroken() {
        multiblockFormed = false;
        connectedLeyLine = null;
        setChanged();
        notifyNetworkChanged();
    }
}
```

---

### 3.3 Ambient Collector

```java
public class AmbientCollectorBlockEntity extends ResonanceGeneratorBlockEntity {
    
    // Collects slowly from the environment
    private float collectedEnergy = 0;
    private static final float COLLECTION_RATE = 0.5f;  // Per tick
    private static final float MAX_STORAGE = 100f;
    
    public AmbientCollectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.AMBIENT_COLLECTOR.get(), pos, state, CrystalTier.RAW);
    }
    
    @Override
    protected boolean checkCanGenerate() {
        return collectedEnergy > 10;  // Minimum threshold
    }
    
    @Override
    protected void updateGeneratedResonance() {
        // Collection rate depends on environment
        float rate = COLLECTION_RATE;
        
        // Bonus in magical biomes
        if (level.getBiome(worldPosition).is(ModTags.Biomes.HIGH_MAGIC)) {
            rate *= 2;
        }
        
        // Bonus at night
        if (level.isNight()) {
            rate *= 1.5f;
        }
        
        // Collect
        collectedEnergy = Math.min(MAX_STORAGE, collectedEnergy + rate);
        
        // Output
        generatedFrequency = 15f;  // Low, stable frequency
        generatedAmplitude = collectedEnergy * 0.5f;  // Max 50A
    }
    
    @Override
    public Set<Direction> getConnectionDirections() {
        return EnumSet.of(Direction.DOWN);  // Output from bottom
    }
}
```

---

### 3.4 & 3.5 Consumer Base & Test Consumer

```java
public abstract class ResonanceConsumerBlockEntity extends BlockEntity 
        implements IResonanceConsumer {
    
    protected final CrystalTier tier;
    protected ResonanceState receivedState = ResonanceState.EMPTY;
    protected final ResonanceRequirement requirement;
    
    protected boolean powered = false;
    protected float efficiency = 0;
    
    // Progress tracking for recipes
    protected int progress = 0;
    protected int maxProgress = 100;
    
    public ResonanceConsumerBlockEntity(BlockEntityType<?> type, BlockPos pos, 
                                        BlockState state, CrystalTier tier,
                                        ResonanceRequirement requirement) {
        super(type, pos, state);
        this.tier = tier;
        this.requirement = requirement;
    }
    
    @Override
    public ResonanceRequirement getRequirement() {
        return requirement;
    }
    
    @Override
    public ResonanceState getResonanceState() {
        return receivedState;
    }
    
    @Override
    public void receiveResonance(Direction from, ResonanceState state) {
        this.receivedState = state;
        updatePowerState();
    }
    
    protected void updatePowerState() {
        powered = requirement.isSatisfiedBy(receivedState);
        efficiency = requirement.getEfficiency(receivedState);
    }
    
    public void serverTick() {
        if (powered && canProcess()) {
            // Progress based on efficiency
            int progressRate = (int) Math.ceil(efficiency * getBaseProgressRate());
            progress += progressRate;
            
            if (progress >= maxProgress) {
                completeProcess();
                progress = 0;
            }
            
            setChanged();
        }
    }
    
    protected abstract boolean canProcess();
    protected abstract void completeProcess();
    protected int getBaseProgressRate() { return 1; }
    
    @Override
    public float getAmplitudeConsumption() {
        if (!powered || !canProcess()) return 0;
        return getBaseConsumption() * efficiency;
    }
    
    protected abstract float getBaseConsumption();
}

// Simple test consumer that just consumes resonance
public class ResonanceBeaconBlockEntity extends ResonanceConsumerBlockEntity {
    
    public ResonanceBeaconBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RESONANCE_BEACON.get(), pos, state,
              CrystalTier.REFINED,
              ResonanceRequirement.any(50));  // Just needs 50+ amplitude
    }
    
    @Override
    protected boolean canProcess() {
        return true;  // Always "processing"
    }
    
    @Override
    protected void completeProcess() {
        // Visual effect
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                ParticleTypes.END_ROD,
                worldPosition.getX() + 0.5,
                worldPosition.getY() + 1.5,
                worldPosition.getZ() + 0.5,
                5, 0.2, 0.5, 0.2, 0.02
            );
        }
    }
    
    @Override
    protected float getBaseConsumption() {
        return 5f;  // Consumes 5 amplitude per tick
    }
    
    @Override
    public Set<Direction> getConnectionDirections() {
        return EnumSet.of(Direction.DOWN);  // Input from below
    }
}
```

---

## Phases 4-9: Summary

I've given you detailed pseudo-code for the foundational phases. Here's what each remaining phase covers at a high level:

### Phase 4: First Machines

- Machine base with inventory handling
- GUI framework (AbstractContainerMenu + Screen)
- Recipe system integration
- Resonant Grinder, Crystal Purifier, Arcane Infuser

### Phase 5: Basic Logistics

- Drift Conduit (hollow tube, items float inside)
- Item transport tick system
- Funnel insertion/extraction
- Item rendering in transit

### Phase 6: Advanced Resonance

- Multi-frequency support
- Harmonic ratio detection and bonuses
- Resonant Chamber multiblock
- Full interference physics

### Phase 7: Advanced Logistics

- Frequency-based sorting
- Item attunement data
- Grabber entity
- Storage Nexus network

### Phase 8: Golems

- Golem entity with custom AI
- Task/instruction system
- Pathfinding integration
- Charging/maintenance

### Phase 9: Polish

- Patchouli guidebook
- Sound design
- Config options
- JEI integration
- Cross-mod compat

---

Want me to expand any specific phase with the same level of detail? Phase 5 (Logistics) or Phase 4 (Machines) would probably be most useful next since they build directly on what we've covered.