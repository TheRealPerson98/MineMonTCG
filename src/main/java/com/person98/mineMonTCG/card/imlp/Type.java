package com.person98.mineMonTCG.card.imlp;

import lombok.Getter;
import java.util.List;

@Getter
public class Type {
    private final String id;
    private final String displayName;
    private final List<String> weakAgainst;
    private final List<String> strongAgainst;

    public Type(String id, String displayName, List<String> weakAgainst, List<String> strongAgainst) {
        this.id = id;
        this.displayName = displayName;
        this.weakAgainst = weakAgainst;
        this.strongAgainst = strongAgainst;
    }

    public boolean isWeakAgainst(Type other) {
        return weakAgainst.contains(other.getId());
    }

    public boolean isStrongAgainst(Type other) {
        return strongAgainst.contains(other.getId());
    }

    public double getDamageMultiplierAgainst(Type other) {
        if (isStrongAgainst(other)) return 2.0;
        if (isWeakAgainst(other)) return 0.5;
        return 1.0;
    }
} 