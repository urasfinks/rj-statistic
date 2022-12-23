package ru.jamsys;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StatisticAggregatorData {

    String name = getClass().getSimpleName();

    long timestamp = System.currentTimeMillis();

    List<Object> list = new ArrayList<>();

}
