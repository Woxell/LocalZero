package com.localzero.api.enumeration;

public enum EcoActionType {
    BIKED_TO_WORK("Biked to work", 2.5f),
    USED_REUSABLE_BAGS("Used reusable bags", 0.2f),
    COMPOSTED_FOOD_WASTE("Composted food waste", 1.1f),
    RECYCLED_ELECTRONICS("Recycled electronics", 4.3f),
    TOOK_PUBLIC_TRANSPORT("Took public transport", 1.8f);

    private final String label;
    private final float carbonSavings;

    EcoActionType(String label, float carbonSavings) {
        this.label = label;
        this.carbonSavings = carbonSavings;
    }

    public String getLabel() {
        return label;
    }

    public float getCarbonSavings() {
        return carbonSavings;
    }

    public static EcoActionType fromLabel(String label) {
        for (EcoActionType type : values()) {
            if (type.label.equalsIgnoreCase(label)) {
                return type;
            }
        }
        return null;
    }
}
