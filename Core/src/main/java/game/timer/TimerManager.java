package game.timer;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gongshengjun
 * @date 2021/5/19 15:26
 */
public class TimerManager {

    private final AtomicInteger threadNo = new AtomicInteger(0);

    private ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            r -> new Thread(r, "Timer_Thread_" + threadNo.incrementAndGet()));


    private TimerManager() {}

    enum Singleton {
        /**
         * 枚举单例
         */
        INSTANCE;
        private final TimerManager processor;

        Singleton() {
            this.processor = new TimerManager();
        }
        TimerManager getProcessor() {
            return processor;
        }
    }

    public TimerManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public static void main(String[] args) {

    }

}
