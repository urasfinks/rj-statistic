package ru.jamsys.component;

import org.springframework.stereotype.Component;
import ru.jamsys.StatisticAggregationData;
import ru.jamsys.Util;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StatisticAggregator {

    Map<Long, StatisticAggregationData> timeLine = new ConcurrentHashMap();

    public StatisticAggregator(SchedulerGlobal schedulerGlobal) {
        final StatisticAggregator self = this;
        schedulerGlobal.add(() -> self.removeOldTime());
    }

    public StatisticAggregationData get() {
        return get(Util.getTimestamp());
    }

    public StatisticAggregationData get(Long timestamp) {
        if (!timeLine.containsKey(timestamp)) {
            timeLine.computeIfAbsent(timestamp, (key) -> new StatisticAggregationData(key));
        }
        return timeLine.get(timestamp);
    }

    private void removeOldTime() {
        Long[] longs = timeLine.keySet().toArray(new Long[0]);
        long old = Util.getTimestamp() - 60;
        Arrays.stream(longs).forEach((l) -> {
            if (l < old) {
                timeLine.remove(l);
            }
        });
    }

}
