package com.jivesoftware.jive.hbasewaltest;

import com.jivesoftware.os.jive.utils.ordered.id.ConstantWriterIdProvider;
import com.jivesoftware.os.jive.utils.ordered.id.OrderIdProvider;
import com.jivesoftware.os.jive.utils.ordered.id.OrderIdProviderImpl;
import com.jivesoftware.os.jive.utils.row.column.value.store.api.RowColumnValueStore;
import org.codehaus.jackson.map.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.codehaus.jackson.node.ObjectNode;

/**
 *
 */
public class ProducerMain {

    private final Config config;
    private final ObjectMapper mapper;

    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                new ProducerMain(args).run();
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
        } else {
            System.out.println("You need one arg - the path to the config file");
        }
    }

    public ProducerMain(String[] args) throws IOException {
        this.mapper = new ObjectMapper();
        this.config = mapper.readValue(new File(args[0]), Config.class);
    }

    public void run() throws IOException {
        RowColumnValueStore<Long, WalKey, Integer, ObjectNode, Exception> rcvs;
        rcvs = new WALStoreProvider().provideWALStore(config, mapper);

        OrderIdProvider idProvider = new OrderIdProviderImpl(new ConstantWriterIdProvider(config.writerId));
        WalWriter walWriter = new WalWriter(rcvs);
        Producer producer = new Producer(walWriter, new EventProvider(), config.numPartitions, config.batchSize, idProvider,
            config.pauseMillis);
        producer.start();
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(config.secondsToRun));
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.out);
        }
        producer.stop();
    }
}
