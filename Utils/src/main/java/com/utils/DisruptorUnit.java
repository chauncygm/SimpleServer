package com.utils;

import cn.hutool.core.thread.NamedThreadFactory;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.jctools.util.UnsafeAccess;
import org.openjdk.jol.info.ClassLayout;

import java.util.Arrays;
import java.util.concurrent.ThreadFactory;

/**
 * @author by user
 * @description TODO
 * @date 2023/1/29 18:09
 */
public class DisruptorUnit {
    private static final int DEFAULT_BUFFER_SIZE = 8 * 1024;
    private static final EventFactory<Event> eventFactory = Event::new;
    private static final ThreadFactory factory = new NamedThreadFactory("logic", false);

    private int x = 10;


    public static void main(String[] args) {
        int i1 = UnsafeAccess.UNSAFE.arrayIndexScale(Object[].class);
        System.out.println("i1 = " + i1);
        new DisruptorUnit().test1();
        final Disruptor<Event> disruptor = new Disruptor<>(eventFactory, DEFAULT_BUFFER_SIZE, factory, ProducerType.SINGLE, new SleepingWaitStrategy());
        disruptor.handleEventsWith((event, sequence, endOfBatch) -> {
            System.out.println("event.uuid = " + event.uuid);
        });
        disruptor.start();

        for (int i = 0; i < 10; i++) {
            final int id = i;
            disruptor.publishEvent((event, sequence) -> {
                event.uuid = id;
                System.out.println("event = " + event);
            });
        }
    }

    public void test1() {
        new Thread(() -> {
            long lastTime = 0L;
            int lastValue = this.x;
            while (true) {
                long now = System.currentTimeMillis();
                if (this.x != lastValue || now - lastTime > 1000) {
                    System.out.println(this.x + "---> " + now);
                    lastTime = now;
                    lastValue = this.x;
                }
            }
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.x = 1000;
            System.out.println("x = 1000 ---->" + System.currentTimeMillis());
        }).start();
    }
}
