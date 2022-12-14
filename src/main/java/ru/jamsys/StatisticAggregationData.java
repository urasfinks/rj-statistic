package ru.jamsys;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

//@JsonAlias({"name", "wName"})
//@JsonIgnore
//@JsonProperty("cpu_load")

public class StatisticAggregationData {

    @Getter
    private final long timestamp;
    @Getter
    private final Map<String, AtomicInteger> counter = new ConcurrentHashMap<>();
    @Getter
    private final Map<String, Object> object = new ConcurrentHashMap<>();

    public StatisticAggregationData(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setCounter(String name, Integer count) {
        if (!counter.containsKey(name)) {
            counter.computeIfAbsent(name, (key) -> new AtomicInteger(count));
        } else {
            counter.get(name).set(count);
        }
    }

    public void setCounter(String name, Long count) {
        setCounter(name, count.intValue());
    }

    public void setCounter(String name, Double count) {
        setCounter(name, count.intValue());
    }

    public void incCounter(String name) {
        if (!counter.containsKey(name)) {
            counter.computeIfAbsent(name, (key) -> new AtomicInteger(1));
        } else {
            counter.get(name).incrementAndGet();
        }
    }

    public void setObject(String name, Object o){
        object.put(name, o);
    }

}
