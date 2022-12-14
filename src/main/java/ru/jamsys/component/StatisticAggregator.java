package ru.jamsys.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.jamsys.StatisticAggregationData;
import ru.jamsys.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
public class StatisticAggregator {

    ConcurrentLinkedDeque<StatisticAggregationData> queue = new ConcurrentLinkedDeque<>();
    volatile Long lastTimestamp = null;

    public StatisticAggregator(SchedulerGlobal schedulerGlobal) {
        final StatisticAggregator self = this;
        schedulerGlobal.add(() -> self.removeOldTime());
    }

    public StatisticAggregationData get() {
        long timestamp = Util.getTimestamp();
        return get(timestamp);
    }

    public StatisticAggregationData get(long timestamp) {
        lastTimestamp = timestamp;
        StatisticAggregationData statisticAggregationData = new StatisticAggregationData(timestamp);
        queue.add(statisticAggregationData);
        return statisticAggregationData;
    }

    public List<StatisticAggregationData> flush() {
        /*
         * Что бы небыло прерывайний, надо сгружать до последнего изменения, потому что последнее возможно ещё не завершилось
         * */
        long copyLastTimestamp = new Long(lastTimestamp).longValue();
        List<StatisticAggregationData> ret = new ArrayList<>();
        if (copyLastTimestamp == 0) {
            return ret;
        }
        while (true) {
            StatisticAggregationData statisticAggregationData = queue.peekFirst();
            if (statisticAggregationData == null) {
                break;
            }
            long timestamp = statisticAggregationData.getTimestamp();
            if (timestamp < copyLastTimestamp) {
                ret.add(statisticAggregationData);
                queue.remove(statisticAggregationData);
            } else {
                break;
            }
        }
        return ret;
    }

    private void removeOldTime() {
        if (queue.size() > 1000) {
            queue.removeFirst();
        }
    }

}
