package com.karashi.limitedspectator.quilt;

import com.karashi.limitedspectator.SpectatorConfig;
import com.karashi.limitedspectator.fabric.FabricSpectatorConfig;

import java.util.List;

/**
 * Quilt implementation of SpectatorConfig.
 * 
 * Quilt is fully compatible with Fabric API, so we delegate to
 * the Fabric implementation. This demonstrates the power of the
 * multi-loader architecture: zero code duplication through composition.
 * 
 * In the future, this could be extended to use Quilt-specific
 * features like QSL (Quilt Standard Libraries) if needed.
 */
public class QuiltSpectatorConfig implements SpectatorConfig {
    
    private static final QuiltSpectatorConfig INSTANCE = new QuiltSpectatorConfig();
    private final FabricSpectatorConfig delegate = FabricSpectatorConfig.getInstance();
    
    private QuiltSpectatorConfig() {}
    
    public static QuiltSpectatorConfig getInstance() {
        return INSTANCE;
    }
    
    // All methods delegate to FabricSpectatorConfig
    // This demonstrates perfect code reuse through composition
    
    @Override
    public boolean isDistanceLimitEnabled() {
        return delegate.isDistanceLimitEnabled();
    }
    
    @Override
    public double getMaxDistance() {
        return delegate.getMaxDistance();
    }
    
    @Override
    public boolean shouldTeleportBackOnExceed() {
        return delegate.shouldTeleportBackOnExceed();
    }
    
    @Override
    public boolean isDimensionTravelAllowed() {
        return delegate.isDimensionTravelAllowed();
    }
    
    @Override
    public boolean shouldResetPositionOnLogout() {
        return delegate.shouldResetPositionOnLogout();
    }
    
    @Override
    public boolean isInvulnerabilityEnabled() {
        return delegate.isInvulnerabilityEnabled();
    }
    
    @Override
    public boolean isFlightEnabled() {
        return delegate.isFlightEnabled();
    }
    
    @Override
    public String getSpectatorGamemode() {
        return delegate.getSpectatorGamemode();
    }
    
    @Override
    public boolean isPvpAllowed() {
        return delegate.isPvpAllowed();
    }
    
    @Override
    public boolean isItemDropAllowed() {
        return delegate.isItemDropAllowed();
    }
    
    @Override
    public boolean isItemPickupAllowed() {
        return delegate.isItemPickupAllowed();
    }
    
    @Override
    public boolean isInventoryCraftingAllowed() {
        return delegate.isInventoryCraftingAllowed();
    }
    
    @Override
    public List<String> getInteractableBlocks() {
        return delegate.getInteractableBlocks();
    }
    
    @Override
    public int getSpectatorCommandPermissionLevel() {
        return delegate.getSpectatorCommandPermissionLevel();
    }
    
    @Override
    public int getSurvivalCommandPermissionLevel() {
        return delegate.getSurvivalCommandPermissionLevel();
    }
    
    @Override
    public boolean isOpRequiredForSpectator() {
        return delegate.isOpRequiredForSpectator();
    }
    
    @Override
    public boolean shouldUseActionBarMessages() {
        return delegate.shouldUseActionBarMessages();
    }
    
    @Override
    public boolean shouldShowDistanceWarnings() {
        return delegate.shouldShowDistanceWarnings();
    }
}
