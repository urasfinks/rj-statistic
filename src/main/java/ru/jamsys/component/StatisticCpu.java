package ru.jamsys.component;

import com.sun.management.OperatingSystemMXBean;
import lombok.Data;
import org.springframework.stereotype.Component;
import ru.jamsys.Util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

@Component
@Data
public class StatisticCpu {

    public volatile double cpuUsage;

    public StatisticCpu(StatisticAggregator statisticAggregator) {
        new Thread(() -> {
            while (true) {
                OperatingSystemMXBean operatingSystemMXBean =
                        (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
                RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
                int availableProcessors = operatingSystemMXBean.getAvailableProcessors();
                long prevUpTime = runtimeMXBean.getUptime();
                long prevProcessCpuTime = operatingSystemMXBean.getProcessCpuTime();
                Util.sleepMillis(1000);
                operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
                long upTime = runtimeMXBean.getUptime();
                long processCpuTime = operatingSystemMXBean.getProcessCpuTime();
                long elapsedCpu = processCpuTime - prevProcessCpuTime;
                long elapsedTime = upTime - prevUpTime;
                cpuUsage = Math.min(99F, elapsedCpu / (elapsedTime * 10000F * availableProcessors));
                statisticAggregator.get().setObject("StatisticCpu", cpuUsage);
            }
        }).start();
    }
}
