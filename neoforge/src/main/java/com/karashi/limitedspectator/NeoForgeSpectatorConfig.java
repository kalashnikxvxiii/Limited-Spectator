package com.karashi.limitedspectator;

import java.util.List;

/**
 * NeoForge implementation of SpectatorConfig.
 * Adapts the ModConfig static values to the SpectatorConfig interface.
 */
public class NeoForgeSpectatorConfig implements SpectatorConfig {
    
    private static final NeoForgeSpectatorConfig INSTANCE = new NeoForgeSpectatorConfig();
    
    private NeoForgeSpectatorConfig() {
        // Singleton
    }
    
    public static NeoForgeSpectatorConfig getInstance() {
        return INSTANCE;
    }
    
    @Override
    public boolean isDistanceLimitEnabled() {
        return ModConfig.maxDistance >= 0;
    }
    
    @Override
    public double getMaxDistance() {
        return ModConfig.maxDistance;
    }
    
    @Override
    public boolean shouldTeleportBackOnExceed() {
        return ModConfig.teleportBackOnExceed;
    }
    
    @Override
    public boolean isDimensionTravelAllowed() {
        return ModConfig.allowDimensionTravel;
    }
    
    @Override
    public boolean shouldResetPositionOnLogout() {
        return ModConfig.resetPositionOnLogout;
    }
    
    @Override
    public boolean isInvulnerabilityEnabled() {
        return ModConfig.enableInvulnerability;
    }
    
    @Override
    public boolean isFlightEnabled() {
        return ModConfig.enableFlight;
    }
    
    @Override
    public String getSpectatorGamemode() {
        return ModConfig.spectatorGamemode;
    }
    
    @Override
    public boolean isPvpAllowed() {
        return ModConfig.allowPvp;
    }
    
    @Override
    public boolean isItemDropAllowed() {
        return ModConfig.allowItemDrop;
    }
    
    @Override
    public boolean isItemPickupAllowed() {
        return ModConfig.allowItemPickup;
    }
    
    @Override
    public boolean isInventoryCraftingAllowed() {
        return ModConfig.allowInventoryCrafting;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<String> getInteractableBlocks() {
        return (List<String>) ModConfig.interactableBlocks;
    }
    
    @Override
    public int getSpectatorCommandPermissionLevel() {
        return ModConfig.spectatorCommandPermissionLevel;
    }
    
    @Override
    public int getSurvivalCommandPermissionLevel() {
        return ModConfig.survivalCommandPermissionLevel;
    }
    
    @Override
    public boolean isOpRequiredForSpectator() {
        return ModConfig.requireOpForSpectator;
    }
    
    @Override
    public boolean shouldUseActionBarMessages() {
        return ModConfig.useActionBarMessages;
    }
    
    @Override
    public boolean shouldShowDistanceWarnings() {
        return ModConfig.showDistanceWarnings;
    }
}
