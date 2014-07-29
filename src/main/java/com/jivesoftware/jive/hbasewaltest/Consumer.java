package com.jivesoftware.jive.hbasewaltest;

import com.jivesoftware.jive.hbasewaltest.WalReader.WalStream;
import com.jivesoftware.os.jive.utils.row.column.value.store.api.TenantIdAndRow;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

/**
 * Consumes events from a single partition
 */
public class Consumer {

    private final WalReader reader;
    private final short partition;
    private final ExecutorService pool;
    private volatile TenantIdAndRow<Long, WalKey> cursor;
    private final WalReader.WalStream stream;
    private final ObjectMapper mapper;
    private final long pauseMillis;

    public Consumer(WalReader reader, short partition, ObjectMapper mapper, long pauseMillis) {
        this.reader = reader;
        this.partition = partition;
        this.pool = Executors.newSingleThreadExecutor();
        this.stream = stream();
        this.mapper = mapper;
        this.pauseMillis = pauseMillis;
    }

    public void start() {
        pool.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        readEvents();
                        Thread.sleep(pauseMillis);
                    } catch (Exception ex) {
                        ex.printStackTrace(System.out);
                    }
                }
            }
        });
    }

    private void readEvents() throws Exception {
        reader.readEventsFromWal(cursor, partition, stream);
    }

    private WalStream stream() {
        return new WalStream() {
            @Override
            public void stream(TenantIdAndRow<Long, WalKey> tenantAndRow, ObjectNode event) {
                if (tenantAndRow != null && event != null) {
                    cursor = tenantAndRow;
                    try {
                        System.out.println(mapper.writeValueAsString(event));
                    } catch (IOException ex) {
                        ex.printStackTrace(System.out);
                    }
                }
            }
        };
    }

    public void stop() {
        pool.shutdownNow();
    }
}
