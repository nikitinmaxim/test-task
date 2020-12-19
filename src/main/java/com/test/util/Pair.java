package com.test.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class Pair <K,V> {
    private final K first;
    private final V second;

}
