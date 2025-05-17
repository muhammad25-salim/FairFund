package project.utils;

import javafx.scene.paint.Color;

public class ColorManager {
    
    // General colors

    public static final Color BACKGROUND_COLOR = Color.web("#FFFFFF"); // Default light mode background
    public static final Color TEXT_COLOR = Color.web("#FFFFFF"); // Default text color for light mode
    public static final Color TEXT_COLOR2 = Color.web("#000000"); // Default text color for light mode
    public static final Color ACCENT_COLOR = Color.web("#3498db");
    public static final Color BORDER_COLOR = Color.web("#E0E0E0");

    // Dark mode colors
    public static final Color DARK_BACKGROUND_COLOR = Color.web("#2c3e50"); // Dark mode background
    public static final Color DARK_TEXT_COLOR = Color.web("#FFFFFF"); // Dark mode text color

    // Other colors for UI elements
    public static final Color BUTTON_COLOR = Color.web("#2ecc71"); // Green button
    public static final Color BUTTON_HOVER_COLOR = Color.web("#27ae60"); // Green button hover

    public static final Color ERROR_COLOR = Color.web("#e74c3c"); // Red for errors
    public static final Color SUCCESS_COLOR = Color.web("#2ecc71"); // Green for success
    public static final Color WARNING_COLOR = Color.web("#f39c12"); // Yellow for warnings
    
    // Additional colors used across the application
    public static final Color TRANSPARENT_BACKGROUND = Color.TRANSPARENT; // Transparent background
    public static final Color LIGHT_GRAY_BG = Color.web("#F5F5F5"); // Light gray background
    public static final Color MEDIUM_GRAY = Color.web("#707070"); // Medium gray
    public static final Color DARK_GRAY = Color.web("#333333"); // Dark gray text
    public static final Color LIGHT_BG_GRADIENT_START = Color.web("#F8F9FA"); // Light gradient start
    public static final Color LIGHT_BG_GRADIENT_END = Color.web("#E9ECEF"); // Light gradient end
    public static final Color LIGHT_BORDER = Color.web("#DEE2E6"); // Light borders
    public static final Color HOVER_BLUE_START = Color.web("#D0E8F2"); // Hover effect start color
    public static final Color HOVER_BLUE_END = Color.web("#B3E0F2"); // Hover effect end color
    public static final Color WHITE_SEMI_TRANSPARENT = Color.web("rgba(255, 255, 255, 0.2)"); // Semi-transparent white
    public static final Color WHITE_VERY_TRANSPARENT = Color.web("rgba(255, 255, 255, 0.1)"); // Very transparent white
    public static final Color WHITE_OPAQUE = Color.web("rgba(255, 255, 255, 0.9)"); // More opaque white
    public static final Color WHITE_MEDIUM = Color.web("rgba(255, 255, 255, 0.5)"); // Medium white
    public static final Color BLACK_SEMI_TRANSPARENT = Color.web("rgba(0, 0, 0, 0.3)"); // Semi-transparent black
    
    // Color theme constants
    private static Color PRIMARY_COLOR_VALUE = Color.web("#238BFA");  //
    private static Color SECONDARY_COLOR_VALUE = Color.web("#00AEEF");
    private static Color PRIMARY_HOVER_COLOR_VALUE = Color.web("#165cc4");
    
    // Properties that use the primary color or are affected by theme changes
    public static final Color PRIMARY_COLOR = PRIMARY_COLOR_VALUE;  //
    public static final Color SECONDARY_COLOR = SECONDARY_COLOR_VALUE;
    public static final Color PRIMARY_HOVER_COLOR = PRIMARY_HOVER_COLOR_VALUE;
    
    // PUBLIC GETTERS - use these instead of direct field access
    public static Color getPrimaryColor() {
        return PRIMARY_COLOR_VALUE;  //
    }
    
    public static Color getSecondaryColor() {
        return SECONDARY_COLOR_VALUE;
    }
    
    public static Color getPrimaryHoverColor() {
        return PRIMARY_HOVER_COLOR_VALUE;
    }
    
    /**
     * Updates the primary theme color and related colors
     * @param newPrimaryColor The new primary color to use
     */
    public static void updatePrimaryTheme(Color newPrimaryColor) {
        // Simply update the private static variables directly
        PRIMARY_COLOR_VALUE = newPrimaryColor;  //
        
        // Calculate and update secondary color (slightly lighter)
        SECONDARY_COLOR_VALUE = newPrimaryColor.deriveColor(1.0, 1.0, 1.2, 1.0);
        
        // Calculate and update hover color (slightly darker)
        PRIMARY_HOVER_COLOR_VALUE = newPrimaryColor.deriveColor(1.0, 1.0, 0.8, 1.0);
        
        // Save the selected theme to preferences
        saveThemePreference(toRgbString(newPrimaryColor));
    }
    
    /**
     * Saves the selected theme color preference
     */
    private static void saveThemePreference(String colorRgb) {
        try {
            java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(ColorManager.class);
            prefs.put("primaryThemeColor", colorRgb);
            prefs.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Loads the saved theme color preference if available
     * Should be called at application startup
     */
    public static void loadSavedTheme() {
        try {
            java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(ColorManager.class);
            String savedColor = prefs.get("primaryThemeColor", null);
            
            if (savedColor != null && !savedColor.isEmpty()) {
                // Parse the RGB string
                String rgb = savedColor.replace("rgb(", "").replace(")", "");
                String[] components = rgb.split(",");
                if (components.length == 3) {
                    int r = Integer.parseInt(components[0].trim());
                    int g = Integer.parseInt(components[1].trim());
                    int b = Integer.parseInt(components[2].trim());
                    Color savedTheme = Color.rgb(r, g, b);
                    
                    // Set the theme colors directly
                    PRIMARY_COLOR_VALUE = savedTheme;
                    SECONDARY_COLOR_VALUE = savedTheme.deriveColor(1.0, 1.0, 1.2, 1.0);
                    PRIMARY_HOVER_COLOR_VALUE = savedTheme.deriveColor(1.0, 1.0, 0.8, 1.0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Helper function to convert Color to RGB string
    public static String toRgbString(Color color) {
        return String.format("rgb(%d, %d, %d)", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
    }
    
    // Helper function to convert Color to RGBA string with opacity
    public static String toRgbaString(Color color, double opacity) {
        return String.format("rgba(%d, %d, %d, %.1f)", 
            (int) (color.getRed() * 255), 
            (int) (color.getGreen() * 255), 
            (int) (color.getBlue() * 255),
            opacity);
    }
    
    /**
     * Returns the up-to-date value of the primary color
     * This is important to ensure we always get the latest value, not a cached reference
     */
    public static Color getUpdatedPrimaryColor() { //
        return PRIMARY_COLOR_VALUE;
    }
    
    /**
     * Returns the up-to-date value of the secondary color
     */
    public static Color getUpdatedSecondaryColor() {
        return SECONDARY_COLOR_VALUE;
    }
    
    /**
     * Returns the up-to-date value of the primary hover color
     */
    public static Color getUpdatedHoverColor() {
        return PRIMARY_HOVER_COLOR_VALUE;
    }
}
