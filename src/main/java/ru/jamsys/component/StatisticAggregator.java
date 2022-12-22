package ru.jamsys.component;

import org.springframework.stereotype.Component;
import ru.jamsys.AbstractCoreComponent;
import ru.jamsys.App;
import ru.jamsys.StatisticAggregatorData;

import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class StatisticAggregator extends AbstractCoreComponent {

    private Broker broker;
    private final ConcurrentLinkedQueue<Object> queue = new ConcurrentLinkedQueue<>();

    public void add(Object o) {
        if (o != null) {
            queue.add(o);
        }
    }

    @Override
    public void flushStatistic() {
        StatisticAggregatorData statisticAggregatorData = new StatisticAggregatorData();
        while (true) {
            Object peek = queue.peek();
            if (peek != null) {
                statisticAggregatorData.getList().add(peek);
            } else {
                break;
            }
        }
        if (broker == null) {
            broker = App.context.getBean(Broker.class);
        }
        broker.addElement(StatisticAggregatorData.class, statisticAggregatorData);
    }

}
