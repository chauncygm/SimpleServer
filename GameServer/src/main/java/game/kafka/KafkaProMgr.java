package game.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class KafkaProMgr {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProMgr.class);

    private static final Object ob = new Object();

    public static void main(String[] args) throws InterruptedException {
//        String topic = "test2";
//        Properties props = new Properties();
//        props.put("bootstrap.servers", "110.40.157.124:9092");
//        props.put("acks", "all");
//        props.put("retries", 0);
//        props.put("batch.size", 16384);
//        props.put("linger.ms", 1);
//        props.put("buffer.memory", 33554432);
//        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        Producer<String, String> producer = new KafkaProducer<String, String>(props);
//        producer.send(new ProducerRecord<String, String>(topic, "0", "0"), (meteData, ex) -> {
//            System.out.println("发送完成" +meteData);
//        });
//        producer.close();
//        lockSupport();
//        testPark();
        test();
//        showMap();
    }

    private static void lockSupport() throws InterruptedException {
        logger.info("step0");

        Thread thread1 = new Thread(() -> {
            KafkaProMgr.forUntil(10 * 1000);

            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
                logger.info("sleep interrupt !");
                throw new RuntimeException(e);
            }
            logger.info("Thread1 start.");
            logger.info("thread1: " + Thread.currentThread().isInterrupted());
            logger.info(String.valueOf(Thread.currentThread().isInterrupted()));
            LockSupport.park(Thread.currentThread());
            logger.info("Thread1 is over.");
        });
        thread1.start();

        thread1.interrupt();
//        logger.info("step1");
//        Thread.sleep(1000);
//        logger.info("step2");
//        Thread.currentThread().interrupt();
//        logger.info("step3");
//        thread1.interrupt();
//        logger.info("step4： interrupt: " + Thread.currentThread().isInterrupted());
//
//        Thread.sleep(1000);
//        logger.info("main thread1: " + thread1.isInterrupted());
//        logger.info("over");

//        logger.info("step2");
    }

    private static void showMap() {
        ConcurrentSkipListMap<String, Integer> map = new ConcurrentSkipListMap<>();
        map.put("ab", 10);
        map.put("bc", 11);
        map.put("ag", 12);
        map.put("pb", 13);
        map.put("cf", 14);
        map.forEach((a, b) -> {
            System.out.println(a + ":" + b);
        });
        System.out.println("");
    }

    private static void forUntil(long delay) {
        long now = System.currentTimeMillis();
        for (;;) {
            long time = System.currentTimeMillis();
            if (time > now + delay) break;
        }
    }

    private static void testPark() {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 2, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());

        Thread thread = new Thread(() -> {
            logger.info("park start");
            LockSupport.parkUntil(ob, System.currentTimeMillis() + 2000);
            logger.info("park over");
        });
        thread.start();

        forUntil(1000);
        logger.info("main start");
        Object blocker = LockSupport.getBlocker(thread);
        logger.info((ob == blocker) + "");
        thread.interrupt();
        logger.info(thread.isInterrupted() + "");
//        LockSupport.unpark(thread);
        logger.info("main over");
    }

    private static void test() {
        int a = -1 << 29;
        int b = 1 << 29;
        int c = 2 << 29;
        int d = 3 << 29;
        logger.info(Integer.toBinaryString(-1));
        logger.info(Integer.toBinaryString(a));
        logger.info(Integer.toBinaryString(b));
        logger.info(Integer.toBinaryString(c));
        logger.info(Integer.toBinaryString(d));
    }
}
