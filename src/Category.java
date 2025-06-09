package src;

public enum Category {
    MENTAL_HEALTH("MENTAL-HEALTH"),
    ACADEMIC_SUPPORT("ACADEMIC-SUPPORT"),
    FINANCIAL_AID("FINANCIAL-AID");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    // Method to get the display name ("MENTAL-HEALTH")
    public String getDisplayName() {
        return displayName;
    }

    
    @Override
    public String toString() {
        return displayName;
    }
} 