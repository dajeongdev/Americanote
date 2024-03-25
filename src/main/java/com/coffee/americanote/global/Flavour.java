package com.coffee.americanote.global;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Flavour {

    CARAMELLY("카라멜"),
    CHOCOLATY("초콜릿"),
    WINEY("와인"),
    FRUITY("과일"),
    HERBY("허브"),
    MALTY("맥아"),
    NUTTY("견과류"),
    FLORAL("꽃향"),
    SMOKY("스모키"),
    SPICY("향신료");

    private final String label;

    Flavour(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    private static final Map<String, Flavour> BY_LABEL =
            Stream.of(values()).collect(Collectors.toMap(Flavour::getLabel, e -> e));

    public static Flavour valueOfLabel(String label) {
        return BY_LABEL.get(label);
    }
}
