package ru.jamsys.component;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.jamsys.App;


class StatisticAggregatorTest {

    static ConfigurableApplicationContext context;

    @BeforeAll
    static void beforeAll() {
        String[] args = new String[]{};
        context = SpringApplication.run(App.class, args);
    }

    @Test
    void test1() {
        StatisticAggregator stat = get();
    }

    private StatisticAggregator get() {
        return context.getBean(StatisticAggregator.class);
    }
}