package com.person98.mineMonTCG.card.imlp;

import lombok.Getter;

@Getter
public class Rarity {
    private final String id;
    private final String displayName;
    private final int weight;

    public Rarity(String id, String displayName, int weight) {
        this.id = id;
        this.displayName = displayName;
        this.weight = weight;
    }
} 