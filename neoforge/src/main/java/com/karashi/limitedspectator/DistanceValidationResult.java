package com.karashi.limitedspectator;

import net.minecraft.world.phys.Vec3;

/**
 * Result of distance validation for a spectator player.
 * Immutable value object.
 */
public record DistanceValidationResult(
    ValidationStatus status,
    double currentDistance,
    double maxDistance,
    Vec3 targetPosition
) {
    
    public enum ValidationStatus {
        WITHIN_LIMIT,
        EXCEEDED,
        NO_LIMIT,
        NOT_IN_SPECTATOR_MODE
    }
    
    public static DistanceValidationResult withinLimit(double distance, double maxDistance) {
        return new DistanceValidationResult(ValidationStatus.WITHIN_LIMIT, distance, maxDistance, null);
    }
    
    public static DistanceValidationResult exceeded(double distance, double maxDistance, Vec3 targetPosition) {
        return new DistanceValidationResult(ValidationStatus.EXCEEDED, distance, maxDistance, targetPosition);
    }
    
    public static DistanceValidationResult noLimit() {
        return new DistanceValidationResult(ValidationStatus.NO_LIMIT, 0, -1, null);
    }
    
    public static DistanceValidationResult notInSpectatorMode() {
        return new DistanceValidationResult(ValidationStatus.NOT_IN_SPECTATOR_MODE, 0, 0, null);
    }
    
    public boolean isExceeded() {
        return status == ValidationStatus.EXCEEDED;
    }
    
    public boolean isWithinLimit() {
        return status == ValidationStatus.WITHIN_LIMIT;
    }
}
