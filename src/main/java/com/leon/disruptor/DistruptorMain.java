package com.leon.disruptor;

// This is the biggest behavioural difference between queues and the Disruptor. When you have multiple consumers listening on the same Disruptor
// all events are published to all consumers in contrast to a queue where a single event will only be sent to a single consumer.
// The behaviour of the Disruptor is intended to be used in cases where you need to independent multiple parallel operations on the same data.
// The canonical example from LMAX is where we have three operations, journal writing (writing the input data to a persistent journal file),
// replication (sending the input data to another machine to ensure that there is a remote copy of the data), and business logic (the real processing work).
// 3 Event Handlers listening (JournalConsumer, ReplicationConsumer and ApplicationConsumer) to the Disruptor,
// each of these Event Handlers will receive all of the messages available in the Disruptor (in the same order).

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import java.time.Duration;
import java.time.Instant;

public class DistruptorMain
{
    public static void main(final String[] args)
    {
        // The factory for the event
        DistruptorEventFactory factory = new DistruptorEventFactory();

        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 4096;

        // Construct the Disruptor
        Disruptor<DistruptorEvent> disruptor = new Disruptor<>(factory, bufferSize,
                DaemonThreadFactory.INSTANCE, ProducerType.SINGLE, new BlockingWaitStrategy());

        // Connect the handler
        // You can also use a lambda expression:
        // disruptor.handleEventsWith((event, sequence, endOfBatch) -> System.out.println("Event: " + event));

        disruptor.handleEventsWith(new ReplicationEventHandler(), new JournalEventHandler())
                .then(new PositionEventHandler());

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<DistruptorEvent> ringBuffer = disruptor.getRingBuffer();

        PositionEventProducer producer = new PositionEventProducer(ringBuffer);

        Instant currentTimeStamp = Instant.now();

        int max = 10;

        for (int count = 0; count < max; count++)
        {
            // You can also use a lambda expression:
            //final int value = count;
            // ringBuffer.publishEvent((event, sequence, buffer) -> event.setPositionRequest(PositionRequest.lockCashPosition(value, value)));

            producer.onData(PositionRequest.lockCashPosition(count, count));
        }

        // Need a sequence barrier here so that below message is printed last.
        System.out.println(String.format("Time taken to process %d events is %dms.", max, Duration.between(currentTimeStamp, Instant.now()).toMillis()));
        // disruptor.halt();
        disruptor.shutdown();
    }
}
