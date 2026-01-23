package com.karashi.limitedspectator;

import net.minecraft.commands.CommandSourceStack;

/**
 * Cross-version compatibility helper for permission checks.
 * Handles differences in CommandSourceStack.hasPermission() between MC versions.
 * 
 * This class provides a safe wrapper around permission checks that works across
 * Minecraft 1.21.x versions, including 1.21.1, 1.21.10, and 1.21.11.
 */
public class PermissionHelper {
    
    /**
     * Check if a command source has the required permission level.
     * 
     * This method uses reflection to safely call hasPermission() regardless of
     * the exact Minecraft version, providing compatibility across 1.21.x versions.
     * 
     * @param source The command source to check
     * @param level The required permission level (0-4)
     * @return true if the source has the required permission level
     */
    public static boolean hasPermission(CommandSourceStack source, int level) {
        try {
            // Try to call hasPermission(int) - works on most 1.21.x versions
            return source.hasPermission(level);
        } catch (NoSuchMethodError e) {
            // Fallback: If method signature changed, use reflection
            try {
                var method = CommandSourceStack.class.getMethod("hasPermission", int.class);
                return (boolean) method.invoke(source, level);
            } catch (Exception ex) {
                // Ultimate fallback: check if source has an entity (player)
                // If no entity, assume it's console/server and grant permission
                // If entity exists, check permission level 0 (all players)
                return source.getEntity() == null || level == 0;
            }
        }
    }
    
    /**
     * Check if a command source requires OP status.
     * 
     * @param source The command source to check
     * @param requireOp Whether OP is required
     * @param permissionLevel The fallback permission level if OP is not required
     * @return true if the source meets the requirements
     */
    public static boolean checkPermission(CommandSourceStack source, boolean requireOp, int permissionLevel) {
        if (requireOp) {
            // Require OP level 2 (game master)
            return hasPermission(source, 2);
        } else {
            // Use configured permission level
            return hasPermission(source, permissionLevel);
        }
    }
}
