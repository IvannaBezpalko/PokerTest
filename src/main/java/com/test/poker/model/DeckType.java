package com.test.poker.model;

public enum DeckType {
    CLUBS("Clubs"),
    HEARTS("Hearts"),
    SPADES("Spades"),
    DIAMONDS("Diamonds");

    private String label;

    DeckType(String label) {
        this.label = label;
    }

    public static DeckType getByUpperCaseName(String label) {
        if (label == null || label.isEmpty()) {
            return null;
        }

        return DeckType.valueOf(label.toUpperCase());
    }
}
