## **Resonance Logistics: Complete Transport System**

---

## Core Philosophy

Create's logistics feel satisfying because you _see_ items moving through physical space. Conveyors, chutes, mechanical arms - it's tangible. Many magic mods fail here by making items teleport invisibly through pipes.

Our system needs to be **visually rich** and **mechanically interesting** while using resonance as the driving force. Items don't teleport - they're _carried on waves_.

---

## Primary Transport: Drift Streams

### The Concept

Items placed in a resonating crystal network don't just sit there - they get caught in the wave and _drift_ along it. Imagine items floating in a visible stream of magical energy, bobbing slightly as the wave carries them.

### How It Works

**Drift Conduits** are hollow crystal tubes. When resonance flows through them, items inside get pushed in the direction of wave propagation.

Key properties:

|Factor|Effect on Transport|
|---|---|
|**Frequency**|Speed of item movement. 20 Hz = slow drift, 100 Hz = rapid flow|
|**Amplitude**|Carrying capacity. Low amplitude can only move light items; high amplitude moves anything|
|**Wave Direction**|Items always move with the wave, never against it|

### Visual Design

- Translucent crystal tubes (like Create's glass pipes but crystalline)
- Visible energy stream inside, color-coded by frequency
- Items float in the center of the stream, gently bobbing
- Higher frequency = faster visible wave pulses
- Higher amplitude = brighter, more intense stream

### Conduit Types

**Basic Drift Conduit**

- Straight sections, corners, junctions
- Items follow the dominant wave direction at junctions
- Can only handle one frequency at a time

**Shielded Drift Conduit**

- Items don't get affected by external resonance
- Prevents interference when running conduits near machines
- More expensive to craft

**Express Conduit**

- Lower internal friction
- Items move faster for the same frequency
- Made from refined crystals

**Bulk Conduit**

- Larger diameter
- Can carry multiple item stacks simultaneously
- Requires more amplitude to operate

---

## Item Insertion & Extraction

### Resonant Funnels

Funnels sit at the interface between inventories and drift streams.

**Intake Funnel**

- Pulls items from adjacent inventory into the stream
- Extraction rate tied to amplitude (more amplitude = faster extraction)
- Can be filtered (more on filtering later)
- Requires minimum amplitude to activate at all

**Output Funnel**

- Catches items from the stream and deposits into adjacent inventory
- "Catches" items that match its filter
- Unmatched items continue past
- Full inventory? Items continue past (no jamming)

**Bidirectional Funnel**

- Can both insert and extract
- Direction determined by wave direction
- Useful for buffer chests

### Resonant Grabbers (The "Mechanical Arm" Equivalent)

For more complex insertion/extraction, **Grabbers** are floating magical "hands" that pick up and place items.

**How they work:**

- Tethered to a crystal node
- Resonance animates them
- Configurable pickup and dropoff points
- Frequency determines operation speed
- Amplitude determines reach distance

**Grabber Types:**

_Basic Grabber_

- Single pickup point, single dropoff point
- Moves one item at a time
- 3-block reach

_Articulated Grabber_

- Multiple pickup points OR multiple dropoff points
- Round-robin or priority-based operation
- 5-block reach

_Precision Grabber_

- Can interact with specific slots
- Required for complex machine feeding
- Slower but more controllable

_Golem Grabber_

- A tiny golem on a tether
- Can be given complex instructions
- Can pick up items from the ground
- Has its own small inventory buffer

### Void Funnel

For disposal:

- Items entering are destroyed
- Releases small amount of ambient resonance (energy recovery)
- Safety valve for overflow situations

---

## Sorting & Filtering

### Frequency-Based Sorting (The Clever Bit)

Items can be **attuned** to specific frequencies. When an attuned item enters a junction where multiple frequencies are present, it follows _its_ frequency.

**Attunement Station:**

- Imprints a frequency onto items
- Attuned items glow faintly with their frequency color
- Attunement persists until removed or item is processed

**Sorting Junction:**

- Multiple conduit outputs, each carrying different frequencies
- Attuned items automatically route to matching frequency
- Unattuned items follow the highest-amplitude path

This creates a powerful sorting paradigm:

1. Items come in from various sources
2. Pass through Attunement Stations that tag them by type
3. Enter a sorting junction with frequency-separated outputs
4. Each output leads to appropriate storage/processing

### Filter Modules

For more traditional sorting, conduits accept **Filter Modules**:

**Whitelist Filter**

- Only matching items can pass
- Non-matching items bounce back (reflected like resonance!)

**Blacklist Filter**

- Matching items bounce back
- Everything else passes

**Tag Filter**

- Filter by ore dictionary / item tags
- "All ores," "all gems," etc.

**NBT Filter**

- Match specific NBT data
- Enchantments, custom names, etc.

**Frequency Filter**

- Only pass items attuned to specific frequency
- Works with the attunement system

### Overflow Handling

When a destination is full, items need somewhere to go:

**Overflow Valve**

- Detects when downstream is backed up
- Redirects items to secondary path
- Essential for robust systems

**Buffer Node**

- Small internal inventory (9 slots)
- Absorbs bursts of items
- Releases slowly when downstream clears

**Resonant Backpressure**

- Optional behavior: full destination increases "resistance"
- Items slow down approaching full inventories
- Eventually backs up to source
- More realistic wave physics

---

## Storage Systems

### Basic Storage

**Resonant Chest**

- Normal chest but with conduit connectivity
- Can receive from and output to drift streams
- Frequency selector determines which stream it interacts with

**Sorting Cabinet**

- Multi-slot storage with per-slot filtering
- Each slot can have a whitelist
- Items auto-sort on entry
- Great for compact organized storage

### Bulk Storage

**Crystal Vault**

- Massive single-item storage (think drawer / storage bus)
- Stores up to 1 million of one item type
- Void mode: destroys overflow
- Shows item and count holographically

**Dimensional Cache**

- Uses resonance to compress space
- Stores many item types, large quantities
- Requires constant resonance to maintain
- Resonance failure = items spill out violently
- Late-game premium storage

### Network Storage (Logistics Network)

**Storage Nexus**

- Central hub connected to multiple storage blocks
- Maintains an index of all stored items
- Conduits connected to nexus can request/deposit items
- Basically AE2/RS but with resonance flavor

**How it works:**

- Nexus requires high amplitude, low frequency (stable base carrier)
- Each connected storage contributes to the network
- Request Terminal (see below) queries the nexus
- Items are transmitted through dedicated high-frequency channels

**Request Terminal**

- Player interface to network storage
- Search items, request crafting
- View system status
- Requires direct crystal connection to nexus

**Requisition Funnel**

- Automation interface to network storage
- Can be configured to keep a chest stocked with items
- Pulls from network automatically

**Stock Monitor**

- Redstone output based on item quantities
- "If iron < 1000, output redstone"
- Triggers automated resupply systems

---

## Fluid & Essence Transport

Not just items - magical fluids need moving too.

### Essence (Magical Fluids)

Various processes produce **Essence** - liquid magic in different flavors:

- **Vital Essence** - from mob processing
- **Natural Essence** - from plant processing
- **Mineral Essence** - from ore processing
- **Arcane Essence** - pure distilled magic
- **Elemental Essences** - fire, water, earth, air

### Essence Conduits

**How they differ from item conduits:**

- Carry continuous flow rather than discrete items
- Flow rate determined by amplitude
- Frequency determines _which_ essence type (each essence resonates at a specific frequency)
- Multiple essences can flow through Tier 4 conduits simultaneously

**Essence Tank**

- Stores large quantities of one essence type
- Displays fill level visually
- Auto-outputs when connected to lower tanks (gravity optional)

**Essence Mixer**

- Combines multiple essences
- Frequency ratios determine output mixture
- Required for advanced crafting

### Pressure Dynamics (Optional Complexity)

For players who want more depth:

- Essence has _pressure_ based on tank fill level
- Flows from high pressure to low pressure
- Pumps (resonance-powered) add pressure
- Valves control flow
- Creates interesting fluid dynamics puzzles

---

## Long-Distance Transport

### The Problem

Amplitude attenuates over distance. A 500A signal becomes useless after 50 blocks of basic conduit. Moving items across your base, let alone across the world, needs solutions.

### Solution 1: Relay Chains

**Resonance Amplifier**

- Placed inline in conduit
- Consumes fuel/catalyst to boost amplitude
- Frequency-locked (only amplifies its tuned frequency)
- Place every 20-30 blocks for long runs

### Solution 2: Wireless Transport

**Drift Relay**

- Paired wireless conduit endpoints
- Items dematerialize at one end, rematerialize at the other
- Requires matched frequency AND phase
- Range based on amplitude (100A = 50 blocks, 1000A = 500 blocks)
- Cross-dimensional with special materials

**How phase matters for wireless:**

- Phase drift over distance is real
- Out-of-phase relays = items lost in transit
- Phase synchronizer block keeps paired relays aligned
- Creates interesting technical challenges

### Solution 3: Ley Line Piggyback

**Ley Line Couriers**

- Special items that "ride" natural ley lines
- Package items at one ley tap, receive at another
- Instant transport anywhere on the same ley network
- Limited capacity (courier has internal inventory)
- Must be retrieved and returned
- Late-game automated version uses golems

### Solution 4: Teleposition

**Teleposition Pad**

- Instant point-to-point item teleport
- Requires identical frequency on both pads
- Items appear to "phase shift" out and in
- Very high amplitude cost
- Good for bulk transport of single item types

---

## Golem Logistics

Golems deserve special attention as they're the "smart" logistics option.

### Courier Golems

**Behavior:**

- Assigned a home Golem Dock
- Given a route (list of locations to visit)
- Picks up items from sources, delivers to destinations
- Returns to dock when inventory empty or energy low

**Advantages over conduits:**

- No infrastructure needed between points
- Can navigate complex terrain
- Can pick up dropped items
- "Smart" - can make decisions

**Disadvantages:**

- Slower than conduits
- Require maintenance (recharging)
- Can be killed by mobs
- Limited inventory

### Golem Instructions

**Instruction Tablet**

- Written orders for golems
- Simple scripting language:

```
TAKE ingotIron FROM chest@north
DELIVER TO furnace@smithy
IF inventory FULL THEN WAIT
IF slot#0 EMPTY THEN RETURN HOME
```

**Visual Programming Alternative**

- Flowchart-style GUI
- Connect condition blocks to action blocks
- More accessible but less flexible

### Golem Types for Logistics

|Golem|Specialty|
|---|---|
|**Pack Golem**|Large inventory (27 slots), slow|
|**Swift Golem**|Small inventory (9 slots), fast|
|**Collector Golem**|Picks up ground items in area|
|**Sorter Golem**|Stationary, sorts items between adjacent inventories|
|**Stock Golem**|Keeps target inventory filled from source|

---

## Integration With Machines

### Input/Output Specifications

Every machine should have clear I/O:

**Defined Faces:**

- Machines have specific sides for input/output
- Configurable via Tuning Fork
- Visual indicators show current configuration

**Example - Resonant Grinder:**

- Top: Item input
- Sides: Essence output (for byproducts)
- Bottom: Item output
- Back: Resonance connection

### Frequency Synchronization

Machines have frequency requirements. Logistics connecting to them should match:

**Mismatched frequency consequences:**

- Items hesitate at the interface (slower transfer)
- Severe mismatch = items rejected entirely
- Incentivizes cohesive network design

**Frequency Adapter:**

- Placed at machine input/output
- Converts between logistics frequency and machine frequency
- Has small efficiency cost
- "Good enough" solution for mixed networks

### Auto-Processing Chains

The goal is smooth automation:

```
[Ore Input] 
    → Drift Conduit 
    → Resonant Grinder (frequency 25Hz)
    → Output to conduit
    → Arcane Infuser (frequency 60Hz, needs adapter)
    → Output Funnel
    → Storage
```

Each step needs:

1. Correct resonance supply
2. Compatible frequency or adaptation
3. Sufficient amplitude for transport speed

---

## Monitoring & Control

### Logistics Monitoring

**Flow Meter**

- Placed inline in conduit
- Shows items/second passing through
- Comparator output based on flow rate
- Useful for detecting bottlenecks

**Network Visualizer**

- Handheld device
- Highlights all connected conduits
- Shows flow direction
- Color-codes by frequency
- Essential debugging tool

**Logistics Controller**

- Centralized monitoring block
- GUI shows all connected funnels, junctions, storage
- Can enable/disable branches
- Set up alerts for low stock, overflow, etc.

### Redstone Integration

**Resonance Gate**

- Blocks item flow when redstone signal received
- Instant stop (items in transit continue to destination)
- Essential for conditional routing

**Flow Diverter**

- 1 input, 2 outputs
- Redstone controls which output is active
- Switchable routing

**Comparator Output**

- Most logistics blocks support comparator
- Signal strength based on:
    - Fill level (storage)
    - Flow rate (conduits)
    - Queue depth (funnels)

---

## Failure States & Challenges

### Jam Conditions

**Item Overflow:**

- Conduits have max capacity
- Too many items = backup
- Eventually items start "spilling" (popping out as entities)
- Design with buffers and overflow routing!

**Frequency Clash:**

- Two different frequencies entering same conduit
- Items get caught in interference
- Random deflection, slowdown, or ejection
- Isolate frequencies or use Tier 4 conduits

### Maintenance Requirements

**Crystal Wear:**

- High-traffic conduits slowly degrade
- Efficiency drops over time
- Eventually crack and break
- Replaced by maintenance golems or player

**Resonance Storms:**

- Rare event in areas of high resonance activity
- Temporary frequency fluctuations
- Logistics may route incorrectly briefly
- Shielded conduits are immune

---

## Progression Through Logistics

**Early Game:**

- Manual transport with Resonant Pouch (personal inventory extension)
- Basic funnels and short conduit runs
- Simple point-to-point automation

**Mid Game:**

- Sorting systems with attunement
- Grabbers for complex machine feeding
- Buffer management
- First golems

**Late Game:**

- Network storage via Nexus
- Wireless transport
- Cross-dimensional logistics
- Fully automated processing chains

**End Game:**

- Self-maintaining systems (golem maintenance crews)
- Ley line courier networks
- Massive throughput bulk transport
- Integration with other mods

---

## Recipe Complexity Examples

To give you a sense of crafting progression:

**Basic Drift Conduit (8):**

- 6 Glass
- 2 Raw Crystal
- Shaped like a pipe

**Intake Funnel:**

- 1 Hopper
- 4 Refined Crystal
- 1 Resonance Core

**Courier Golem Core:**

- 1 Golem Core (base item)
- 4 Ender Pearl
- 4 Refined Crystal
- Requires Harmonic Synthesizer at 40Hz/80Hz

**Storage Nexus:**

- 4 Synthetic Crystal (attuned to data frequencies)
- 4 Ley-Infused Crystal
- 1 Nether Star
- Complex ritual crafting
