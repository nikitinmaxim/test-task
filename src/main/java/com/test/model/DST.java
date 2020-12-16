package com.test.model;

public enum DST {
    E("Europe"),
    A("US/Canada"),
    S("South Africa"),
    O("Australia"),
    Z("New Zealand"),
    N("None"),
    U("Unknown");

    private final String value;

    DST(String value) {
        this.value = value;
    }
}
