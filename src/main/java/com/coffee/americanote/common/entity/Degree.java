package com.coffee.americanote.common.entity;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Degree {

    STRONG("강함", 1.0),
    MEDIUM("중간", 0.5),
    WEAK("약함", 0.0);

    private final String label;
    private final Double weight;

    Degree(String label, Double weight) {
        this.label = label;
        this.weight = weight;
    }

    public String getLabel() {
        return label;
    }

    public Double getWeight() {
        return weight;
    }

    private static final Map<String, Degree> BY_LABEL =
            Stream.of(values()).collect(Collectors.toMap(Degree::getLabel, e -> e));

    public static Degree valueOfLabel(String label) {
        return BY_LABEL.get(label);
    }

    public static final Map<Double, Degree> BY_WEIGHT =
            Stream.of(values()).collect(Collectors.toMap(Degree::getWeight, e -> e));

    public static Degree valueOfWeight(Double weight) {
        return BY_WEIGHT.get(weight);
    }
}
