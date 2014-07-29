package com.jivesoftware.jive.hbasewaltest;

import com.jivesoftware.os.jive.utils.row.column.value.store.api.RowColumnValueStore;
import org.codehaus.jackson.map.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;
import org.codehaus.jackson.node.ObjectNode;

/**
 *
 */
public class ConsumerMain {

    private final Config config;
    private final ObjectMapper mapper;

    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                new ConsumerMain(args).run();
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
        } else {
            System.out.println("You need one arg - the path to the config file");
        }
    }

    public ConsumerMain(String[] args) throws IOException {
        this.mapper = new ObjectMapper();
        this.config = mapper.readValue(new File(args[0]), Config.class);
    }

    public void run() throws IOException {
        RowColumnValueStore<Long, WalKey, Integer, ObjectNode, Exception> rcvs;
        rcvs = new WALStoreProvider().provideWALStore(config, mapper);

        WalReader walReader = new WalReader(rcvs);
        List<Consumer> consumers = new ArrayList<>();
        
        for (int i = 0; i < config.numPartitions; i++) {
            Consumer consumer = new Consumer(walReader, (short)i, mapper, config.pauseMillis);
            consumer.start();
            consumers.add(consumer);
        }
        
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(config.secondsToRun));
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.out);
        }
        
        for (Consumer consumer : consumers) {
            consumer.stop();
        }
    }
}
