package com.coffee.americanote.global;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Degree {

    STRONG("강함"),
    MEDIUM("중간"),
    WEAK("약함");

    private final String label;

    Degree(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    private static final Map<String, Degree> BY_LABEL =
            Stream.of(values()).collect(Collectors.toMap(Degree::getLabel, e -> e));

    public static Degree valueOfLabel(String label) {
        return BY_LABEL.get(label);
    }
}
