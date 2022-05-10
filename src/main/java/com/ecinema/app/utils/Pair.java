package com.ecinema.app.utils;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Pair<K, V> {
    private K first;
    private V second;
}
