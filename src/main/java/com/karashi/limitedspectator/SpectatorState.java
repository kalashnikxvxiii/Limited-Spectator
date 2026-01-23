package com.karashi.limitedspectator;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Immutable record representing the state of a player in spectator mode.
 * This is a pure data class with no loader dependencies.
 */
public record SpectatorState(
    Vec3 startPosition,
    ResourceKey<Level> startDimension,
    long startTime
) {
    /**
     * Creates a new spectator state with the current timestamp.
     */
    public static SpectatorState create(Vec3 position, ResourceKey<Level> dimension) {
        return new SpectatorState(position, dimension, System.currentTimeMillis());
    }
    
    /**
     * Gets the duration in milliseconds since spectator mode was activated.
     */
    public long getDuration() {
        return System.currentTimeMillis() - startTime;
    }
}
