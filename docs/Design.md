## Full System Design: **Resonance Magic

---

## The Physics of Resonance

### Core Properties

Every point in your resonance network has three measurable values:

|Property|Description|Analogy|
|---|---|---|
|**Frequency (Hz)**|How fast the magic oscillates|Create's RPM|
|**Amplitude (A)**|The "strength" of the wave|Stress capacity|
|**Phase (φ)**|Where in the cycle the wave currently is|No Create equivalent - this is new|

Phase becomes critical when networks merge or loop back on themselves. Two waves at the same frequency but opposite phase _cancel out entirely_. Same phase? They reinforce.

### Wave Behavior

Resonance follows actual wave physics (simplified for gameplay):

**Superposition** - When two resonance signals meet at a junction, they combine:

- Same frequency, same phase → amplitudes add (constructive interference)
- Same frequency, opposite phase → amplitudes subtract (destructive interference)
- Different frequencies → creates a "beat frequency" equal to the difference, which may be usable or may just be noise

**Attenuation** - Amplitude decreases over distance. Crystal quality determines loss rate. A pristine crystal might lose 1% amplitude per block; a flawed one might lose 10%.

**Reflection** - When resonance hits a dead end or incompatible component, some bounces back. This reflected wave can interfere with incoming waves, creating standing wave patterns.

---

## Crystal Infrastructure

### Crystal Types (Progression Tiers)

**Tier 1: Raw Crystals**

- Found in geodes underground
- Limited frequency range (1-50 Hz)
- High attenuation (5% per block)
- Low amplitude ceiling (100 A)
- Visually rough, cloudy

**Tier 2: Refined Crystals**

- Crafted by running raw crystals through a Resonant Purifier
- Wider frequency range (1-200 Hz)
- Medium attenuation (2% per block)
- Medium amplitude ceiling (500 A)
- Visually clearer, faint inner glow

**Tier 3: Synthetic Crystals**

- Grown in a Crystal Gestation Chamber over real-time hours
- Can be "tuned" during growth to favor certain frequency ranges
- Very low attenuation (0.5% per block)
- High amplitude ceiling (2000 A)
- Visually perfect, pulsing with color matching their tuned frequency

**Tier 4: Ley-Infused Crystals**

- Require direct ley line exposure during growth
- Near-zero attenuation
- Can carry multiple frequencies simultaneously without interference
- Extremely expensive, late-game

### Crystal Shapes (Functional Variants)

Crystals can be cut into different shapes, each serving a purpose:

**Prisms (Straight Conductors)**

- Basic transmission
- 1 input face, 1 output face
- Resonance passes straight through

**Nodes (Junctions)**

- 6 connection faces (any can be input or output)
- Resonance distributes to all connected outputs
- Amplitude divides equally among outputs

**Lenses (Amplifiers)**

- Consume a catalyst material (glowstone, blaze powder, etc.)
- Boost amplitude at cost of resources
- Can also focus multiple inputs into one stronger output

**Filters**

- Only pass a specific frequency range
- Everything outside the range reflects back
- Configurable via tuning interface

**Phase Shifters**

- Delay the wave by a configurable amount
- Critical for synchronizing networks
- Used to prevent destructive interference at merge points

---

## Generation Methods

### Passive Generators

**Ley Line Tap**

- Worldgen creates ley lines as invisible paths through the world
- Tap structure built over a ley line intersection
- Output depends on intersection strength (minor: 30 Hz/50 A, major: 60 Hz/200 A, nexus: 100 Hz/500 A)
- Completely passive, never depletes
- Finding good ley intersections is a major exploration goal

**Celestial Accumulator**

- Large multiblock, needs sky access
- Output varies with:
    - Time of day (peaks at midnight)
    - Moon phase (full moon = 4x output)
    - Weather (clear > cloudy > rain)
    - Y-level (higher = better)
- Produces high frequency, low amplitude - good for precision work

**Ambient Resonance Collector**

- Tiny output, but works anywhere
- Collects "background" magical vibration
- Useful for bootstrapping or powering small devices
- Output increases in magical biomes

### Active Generators

**Soul Engine**

- Consumes mob drops or uses mob spawner proximity
- Different mob types produce different frequencies:
    - Zombies: Low frequency, high amplitude (grunt work)
    - Skeletons: Medium frequency, medium amplitude (general purpose)
    - Blazes: High frequency, high amplitude (premium fuel)
    - Endermen: Erratic frequency, very high amplitude (dangerous but powerful)
- Has a "soul buffer" that smooths out the erratic input

**Ritual Circle**

- Player-constructed multiblock from chalked patterns and focus items
- Requires player activation (or redstone + bound catalyst)
- Fully configurable frequency output
- Amplitude depends on ritual tier and sacrificed materials
- Can produce frequencies impossible to get elsewhere

**Resonance Engine**

- Mechanical device - here's your Create crossover potential
- Converts kinetic energy (or even Forge Energy) into resonance
- Inefficient but reliable
- Frequency controlled by engine configuration
- Lets players bootstrap into the system with existing infrastructure

---

## Machines & Devices

### Processing Machines

**Resonant Grinder**

- Frequency: 10-30 Hz (low)
- Amplitude: 200+ A (high)
- Crushes ores, doubles output at optimal resonance
- Sub-optimal frequency = slower processing, wrong frequency = jammed

**Arcane Infuser**

- Frequency: Exactly 60 Hz (precise)
- Amplitude: 100 A minimum
- Imbues items with magical properties
- Being off by even 2 Hz degrades the infusion quality

**Harmonic Synthesizer**

- Requires TWO input frequencies simultaneously
- Crafts items that need "combined" magic
- The ratio of frequencies matters - 2:1 creates different items than 3:2

**Crystal Growth Chamber**

- Very low frequency (1-5 Hz), sustained over long periods
- Amplitude determines growth speed
- Frequency determines crystal properties
- Set it and forget it machine

**Essence Distillery**

- Extracts raw magical essence from plants, mobs drops, etc.
- Medium frequency range
- Output fluid used as catalyst in other machines

### Utility Devices

**Resonance Relay**

- Wireless transmission
- Paired relays must be tuned to the same frequency
- Range depends on amplitude (more amplitude = further)
- Phase drift is a problem over long distances

**Dimensional Anchor**

- Keeps chunks loaded
- Requires constant resonance feed
- Higher amplitude = more chunks

**Temporal Dilator**

- Speeds up or slows down tile entities in an area
- Frequency determines speedup factor
- Extreme amplitude costs
- Cannot affect players or mobs

**Warding Projector**

- Creates a protective field
- Frequency determines what's blocked:
    - Low freq: blocks mobs
    - Medium freq: blocks projectiles
    - High freq: blocks players
- Amplitude determines field size

### Golem System

**Golem Core Fabricator**

- Creates animated golem cores
- Frequency imprints "personality":
    - 20 Hz: Worker (mining, farming)
    - 40 Hz: Guardian (combat)
    - 80 Hz: Courier (item transport)
    - 120 Hz: Scholar (research automation)
- Amplitude determines golem durability/capability

**Resonance Charger**

- Golems slowly lose their resonance imprint
- Must return to charger periodically
- Wireless charging possible with Relays

---

## Advanced Mechanics

### Harmonic Relationships

Certain frequency ratios create special effects:

|Ratio|Name|Effect|
|---|---|---|
|1:1|Unison|Pure reinforcement, no special effect|
|2:1|Octave|20% efficiency bonus on all machines in the network|
|3:2|Perfect Fifth|Unlocks "harmonic crafting" recipes|
|4:3|Perfect Fourth|Reduces attenuation by 50%|
|5:4|Major Third|Increases amplitude ceiling by 25%|

Building a network that maintains these ratios across different branches becomes a real puzzle.

### Standing Waves & Resonant Chambers

If you create a closed loop of crystals with the right length, you can establish a standing wave:

- Standing waves don't attenuate (the wave reinforces itself)
- Can store amplitude indefinitely
- Tapping a standing wave too hard collapses it
- Can be used as "batteries" of sorts

**Resonant Chamber** - A dedicated multiblock for creating controlled standing waves. Feed it resonance, it builds up over time. Extract carefully.

### Interference Management

When networks get complex, interference becomes a real challenge:

**Problem:** You have two generators at different frequencies feeding into the same machine.

**Solutions:**

1. Use filters to isolate branches
2. Use phase shifters to time the waves so they don't collide
3. Use frequency converters (Harmonic Nodes) to match frequencies
4. Use Tier 4 crystals that handle multiple frequencies in parallel channels

### Resonance Cascade (Failure State)

If amplitude exceeds crystal capacity, or if interference patterns become too chaotic:

1. Warning signs: crystals vibrate visibly, emit particles, make sounds
2. Escalation: nearby crystals start cracking
3. Cascade: chain reaction of shattering crystals
4. Explosion: final release of stored energy - damages blocks and entities

This encourages careful network design and monitoring.

---

## Measurement & Control

**Resonance Meter (Handheld)**

- Point at any crystal
- Shows current frequency, amplitude, phase
- Shows wave visualization

**Oscilloscope Block**

- Place next to crystal
- GUI shows real-time wave graph
- Can monitor multiple points
- Set up alerts for out-of-spec conditions

**Resonance Controller**

- Redstone integration
- Can enable/disable branches
- Can adjust Harmonic Nodes via redstone signal
- Allows automated frequency management

**Tuning Fork (Tool)**

- Right-click crystals to adjust filters, shifters, etc.
- Shows compatible frequency ranges
- Can "ping" a network to trace connections

---

## Progression Arc

**Early Game:**

- Find crystal geodes
- Build basic ambient collectors
- Power simple machines (grinder, basic infuser)
- Single-frequency networks only

**Mid Game:**

- Locate ley lines, build taps
- Refine crystals for better transmission
- Learn to manage multiple frequencies
- First golems
- Introduce harmonic relationships

**Late Game:**

- Grow synthetic crystals for specific applications
- Build Resonant Chambers for amplitude storage
- Complex multi-frequency networks
- Ritual circles for exotic frequencies
- Dimensional manipulation (anchors, teleportation)

**End Game:**

- Ley-infused crystals
- Frequency ranges beyond normal limits
- Self-sustaining resonant loops
- Integration with other mods' power systems
- Creative-tier multiblocks

---

## Visual & Audio Design Notes

The mod lives or dies on _feeling_ magical and mechanical simultaneously:

**Visuals:**

- Crystals should shimmer/pulse at their frequency (visible difference between 20 Hz and 60 Hz)
- Higher amplitude = brighter glow
- Interference patterns visible as chaotic flickering
- Standing waves shown as stable, beautiful patterns
- Near-cascade crystals glow angry red with cracks

**Audio:**

- Each frequency has a distinct hum/tone
- Harmonically related frequencies sound "musical" together
- Dissonant frequencies sound harsh
- Amplitude affects volume
- Cascade warning is an escalating discordant screech
