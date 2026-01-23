package com.karashi.limitedspectator;

import java.util.List;

/**
 * Fabric implementation of SpectatorConfig.
 * Reads configuration from CommonConfig (TOML file) or uses hardcoded defaults.
 */
public class FabricSpectatorConfig implements SpectatorConfig {
    
    private static final FabricSpectatorConfig INSTANCE = new FabricSpectatorConfig();
    private final CommonConfig config;
    
    private FabricSpectatorConfig() {
        this.config = CommonConfig.getInstance();
    }
    
    public static FabricSpectatorConfig getInstance() {
        return INSTANCE;
    }
    
    @Override
    public boolean isDistanceLimitEnabled() {
        return config.maxDistance >= 0;
    }
    
    @Override
    public double getMaxDistance() {
        return config.maxDistance;
    }
    
    @Override
    public boolean shouldTeleportBackOnExceed() {
        return config.teleportBackOnExceed;
    }
    
    @Override
    public boolean isDimensionTravelAllowed() {
        return config.allowDimensionTravel;
    }
    
    @Override
    public boolean shouldResetPositionOnLogout() {
        return config.resetPositionOnLogout;
    }
    
    @Override
    public boolean isInvulnerabilityEnabled() {
        return config.enableInvulnerability;
    }
    
    @Override
    public boolean isFlightEnabled() {
        return config.enableFlight;
    }
    
    @Override
    public String getSpectatorGamemode() {
        return config.spectatorGamemode;
    }
    
    @Override
    public boolean isPvpAllowed() {
        return config.allowPvp;
    }
    
    @Override
    public boolean isItemDropAllowed() {
        return config.allowItemDrop;
    }
    
    @Override
    public boolean isItemPickupAllowed() {
        return config.allowItemPickup;
    }
    
    @Override
    public boolean isInventoryCraftingAllowed() {
        return config.allowInventoryCrafting;
    }
    
    @Override
    public List<String> getInteractableBlocks() {
        return config.interactableBlocks;
    }
    
    @Override
    public int getSpectatorCommandPermissionLevel() {
        return config.spectatorCommandPermissionLevel;
    }
    
    @Override
    public int getSurvivalCommandPermissionLevel() {
        return config.survivalCommandPermissionLevel;
    }
    
    @Override
    public boolean isOpRequiredForSpectator() {
        return config.requireOpForSpectator;
    }
    
    @Override
    public boolean shouldUseActionBarMessages() {
        return config.useActionBarMessages;
    }
    
    @Override
    public boolean shouldShowDistanceWarnings() {
        return config.showDistanceWarnings;
    }
}
