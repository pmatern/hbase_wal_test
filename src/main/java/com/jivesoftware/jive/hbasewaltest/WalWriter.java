package com.jivesoftware.jive.hbasewaltest;

import com.jivesoftware.os.jive.utils.row.column.value.store.api.RowColumnValueStore;
import com.jivesoftware.os.jive.utils.row.column.value.store.api.TenantRowColumValueTimestampAdd;
import com.jivesoftware.os.jive.utils.row.column.value.store.api.timestamper.ConstantTimestamper;
import org.codehaus.jackson.node.ObjectNode;
import java.util.List;
import java.util.ArrayList;

/**
 *
 */
public class WalWriter {
    private final RowColumnValueStore<Long, WalKey, Integer, ObjectNode, Exception> rcvs;
    

    public WalWriter(RowColumnValueStore<Long, WalKey, Integer, ObjectNode, Exception> rcvs) {
        this.rcvs = rcvs;
    }
    
    public void writeEventsToWal(List<Event> events) throws Exception {
        List<TenantRowColumValueTimestampAdd<Long, WalKey, Integer, ObjectNode>> toAdd = new ArrayList<>();
        for (Event toWrite : events) {
            long tenant = toWrite.getTenantId();
            
            toAdd.add(new TenantRowColumValueTimestampAdd<>(
                tenant, toWrite.getKey(), 0, toWrite.getEventData(), new ConstantTimestamper(System.currentTimeMillis())));
            
        }
        
        rcvs.multiRowsMultiAdd(toAdd);
    }
    
    
}
