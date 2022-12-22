package ru.jamsys;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StatisticAggregatorData {

    long timestamp = System.currentTimeMillis();
    List<Object> list = new ArrayList<>();

}
