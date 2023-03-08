package com.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.BasicExecutor;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SequencerTest {

    private final int bufferSize = 4;
    private final SingleProducerSequencer sequencer =
            new SingleProducerSequencer(bufferSize, new BlockingWaitStrategy());
    private final EventFactory<Event> factory = Event::new;

    @Test
    public void testNest() {
        final Disruptor<Event> disruptor = new Disruptor<>(factory, 4, DaemonThreadFactory.INSTANCE, ProducerType.SINGLE, new BlockingWaitStrategy());
        assertEquals(0, sequencer.next());
        assertEquals(1, sequencer.next());
        assertEquals(4, sequencer.next(3));
        assertEquals(6, sequencer.next(2));
        disruptor.handleEventsWith((event, sequence, end) -> {
            System.out.println("消费" + event);
        });
        disruptor.start();
        RingBuffer<Event> ringBuffer = disruptor.getRingBuffer();
        for (int i = 0; i < 100; i++) {
            final int num = i;
            disruptor.publishEvent((event, sequence) -> {
                event.setValue(sequence + ": event-" + num);
            });
        }
        System.out.println(disruptor.getCursor());
        for (int i = 0; i < 10; i++) {
            System.out.println(ringBuffer.get(i));
        }
    }

}
