package com.karashi.limitedspectator;

/**
 * Interface for spectator configuration.
 * Implementations will be provided by each mod loader.
 */
public interface SpectatorConfig {
    
    /**
     * @return true if distance limit is enabled
     */
    boolean isDistanceLimitEnabled();
    
    /**
     * @return the maximum distance in blocks
     */
    double getMaxDistance();
    
    /**
     * @return true if player should be teleported back when exceeding distance
     */
    boolean shouldTeleportBackOnExceed();
    
    /**
     * @return true if dimension travel is allowed
     */
    boolean isDimensionTravelAllowed();
    
    /**
     * @return true if position should be reset on logout
     */
    boolean shouldResetPositionOnLogout();
    
    /**
     * @return true if invulnerability is enabled
     */
    boolean isInvulnerabilityEnabled();
    
    /**
     * @return true if flight is enabled
     */
    boolean isFlightEnabled();
    
    /**
     * @return the spectator gamemode ("ADVENTURE" or "SPECTATOR")
     */
    String getSpectatorGamemode();
    
    /**
     * @return true if PvP is allowed
     */
    boolean isPvpAllowed();
    
    /**
     * @return true if item dropping is allowed
     */
    boolean isItemDropAllowed();
    
    /**
     * @return true if item pickup is allowed
     */
    boolean isItemPickupAllowed();
    
    /**
     * @return true if inventory crafting is allowed
     */
    boolean isInventoryCraftingAllowed();
    
    /**
     * @return list of interactable block IDs
     */
    java.util.List<String> getInteractableBlocks();
    
    /**
     * @return permission level for spectator command
     */
    int getSpectatorCommandPermissionLevel();
    
    /**
     * @return permission level for survival command
     */
    int getSurvivalCommandPermissionLevel();
    
    /**
     * @return true if OP is required for spectator commands
     */
    boolean isOpRequiredForSpectator();
    
    /**
     * @return true if action bar messages should be used
     */
    boolean shouldUseActionBarMessages();
    
    /**
     * @return true if distance warnings should be shown
     */
    boolean shouldShowDistanceWarnings();
}
