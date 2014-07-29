package com.jivesoftware.jive.hbasewaltest;

import com.jivesoftware.os.jive.utils.ordered.id.OrderIdProvider;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;

/**
 * Produces batches of events across the supplied number of partitions. 
 */
public class Producer {

    private final WalWriter writer;
    private final EventProvider eventProvider;
    private final ExecutorService pool;
    private final int numPartitions;
    private final int batchSize;
    private final OrderIdProvider idProvider;
    private final long pauseMillis;

    public Producer(WalWriter writer, EventProvider eventProvider, int numPartitions, int batchSize, OrderIdProvider idProvider,
        long pauseMillis) {
        this.writer = writer;
        this.eventProvider = eventProvider;
        this.pool = Executors.newFixedThreadPool(numPartitions); //no real need for pool size to match partition count
        this.numPartitions = numPartitions;
        this.batchSize = batchSize;
        this.idProvider = idProvider;
        this.pauseMillis = pauseMillis;
    }

    public void start() {
        for (int i = 0; i < numPartitions; i++) {
            pool.execute(produce());
        }
    }

    private Runnable produce() {
        return new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        writer.writeEventsToWal(eventBatch());
                        Thread.sleep(pauseMillis);
                    } catch (Exception ex) {
                        ex.printStackTrace(System.out);
                    }
                }
            }
        };
    }

    /**
     * creates a batch of events spread across partitions
     * @return 
     */
    private List<Event> eventBatch() {
        List<Event> batch = new ArrayList<>(batchSize);
        for (int i=0; i<batchSize; i++) {
            Event event = new Event(i, new WalKey(idProvider.nextId(), (short)(i%batchSize)), eventProvider.provideEvent());
            batch.add(event);
        }
        
        return batch;
    }

    public void stop() {
        pool.shutdownNow();
    }
}
