package ru.jamsys.component;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.jamsys.App;
import ru.jamsys.Util;


class StatisticAggregatorTest {

    static ConfigurableApplicationContext context;

    @BeforeAll
    static void beforeAll() {
        String[] args = new String[]{};
        context = SpringApplication.run(App.class, args);
        App.initContext(context, true);
    }

    @Test
    void test1() {
        StatisticAggregator stat = get();
        stat.get(1).setObject("hello", "world");
        Assertions.assertEquals("[]", Util.jsonObjectToString(stat.flush()));
        stat.get(2).setObject("hello", "world");
        Assertions.assertEquals("[{\"timestamp\":1,\"counter\":{},\"object\":{\"hello\":\"world\"}}]", Util.jsonObjectToString(stat.flush()));
        stat.get(3).setObject("hello", "world");
        stat.get(3).setObject("hello2", "world2");
        Assertions.assertEquals("[{\"timestamp\":2,\"counter\":{},\"object\":{\"hello\":\"world\"}}]", Util.jsonObjectToString(stat.flush()));
        stat.get(4).setObject("hello", "world");
        Assertions.assertEquals("[{\"timestamp\":3,\"counter\":{},\"object\":{\"hello\":\"world\"}},{\"timestamp\":3,\"counter\":{},\"object\":{\"hello2\":\"world2\"}}]", Util.jsonObjectToString(stat.flush()));
    }

    private StatisticAggregator get() {
        return context.getBean(StatisticAggregator.class);
    }
}