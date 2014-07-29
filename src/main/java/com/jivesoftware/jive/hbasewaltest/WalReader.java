package com.jivesoftware.jive.hbasewaltest;

import com.jivesoftware.os.jive.utils.base.interfaces.CallbackStream;
import com.jivesoftware.os.jive.utils.row.column.value.store.api.TenantRowColumnValueAndTimestamp;
import com.jivesoftware.os.jive.utils.row.column.value.store.api.RowColumnValueStore;
import com.jivesoftware.os.jive.utils.row.column.value.store.api.TenantIdAndRow;
import java.nio.ByteBuffer;
import org.codehaus.jackson.node.ObjectNode;

/**
 *
 */
public class WalReader {

    private final RowColumnValueStore<Long, WalKey, Integer, ObjectNode, Exception> rcvs;

    public WalReader(RowColumnValueStore<Long, WalKey, Integer, ObjectNode, Exception> rcvs) {
        this.rcvs = rcvs;
    }

    public void readEventsFromWal(TenantIdAndRow<Long, WalKey> startKey, short partition, final WalStream stream) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(Short.SIZE);
        buffer.putShort(partition);
        rcvs.<Long>rowScan(startKey, buffer.array(), 0, partition,
            new CallbackStream<TenantRowColumnValueAndTimestamp<Long, WalKey, Integer, ObjectNode, Long>>() {

            @Override
            public TenantRowColumnValueAndTimestamp<Long, WalKey, Integer, ObjectNode, Long> 
                callback(TenantRowColumnValueAndTimestamp<Long, WalKey, Integer, ObjectNode, Long> v) throws Exception {
                
                if (v == null) {
                    stream.stream(null, null);
                }
                else {
                    TenantIdAndRow<Long, WalKey> tenantIdAndRow = new TenantIdAndRow<>(v.getTenant(), v.getRow());
                    stream.stream(tenantIdAndRow, v.getValue());
                }
                
                
                return v;
            }
        });
    }

    public interface WalStream {

        public void stream(TenantIdAndRow<Long, WalKey> tenantAndRow, ObjectNode event);
    }
}
