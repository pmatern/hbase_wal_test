package com.jivesoftware.jive.hbasewaltest;

import com.jivesoftware.os.jive.utils.row.column.value.store.api.DefaultRowColumnValueStoreMarshaller;
import com.jivesoftware.os.jive.utils.row.column.value.store.api.RowColumnValueStore;
import com.jivesoftware.os.jive.utils.row.column.value.store.api.RowColumnValueStoreMarshaller;
import com.jivesoftware.os.jive.utils.row.column.value.store.api.timestamper.CurrentTimestamper;
import com.jivesoftware.os.jive.utils.row.column.value.store.hbase.HBaseSetOfSortedMapsImplInitializer;
import com.jivesoftware.os.jive.utils.row.column.value.store.marshall.api.TypeMarshaller;
import com.jivesoftware.os.jive.utils.row.column.value.store.marshall.primatives.IntegerTypeMarshaller;
import com.jivesoftware.os.jive.utils.row.column.value.store.marshall.primatives.LongTypeMarshaller;
import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

/**
 *
 */
public class WALStoreProvider {
    
    public RowColumnValueStore<Long, WalKey, Integer, ObjectNode, Exception> provideWALStore(
        Config config, ObjectMapper mapper) throws IOException {
        return new HBaseSetOfSortedMapsImplInitializer(hBaseConfig(config)).initialize("waltest", "wal", "f", marshaller(mapper), 
            new CurrentTimestamper());
    }
    
    private RowColumnValueStoreMarshaller<Long, WalKey, Integer, ObjectNode> marshaller(ObjectMapper mapper) {
        return new DefaultRowColumnValueStoreMarshaller<>(
            new LongTypeMarshaller(), walKeyMarshaller(), new IntegerTypeMarshaller(), valueMarshaller(mapper));
    }
    
    private TypeMarshaller<WalKey> walKeyMarshaller() {
        return new TypeMarshaller<WalKey>() {
            @Override
            public WalKey fromBytes(byte[] bytes) throws Exception {
                return WalKey.fromBytes(bytes);
            }

            @Override
            public byte[] toBytes(WalKey t) throws Exception {
                return t.toBytes();
            }

            @Override
            public WalKey fromLexBytes(byte[] lexBytes) throws Exception {
                return fromBytes(lexBytes);
            }

            @Override
            public byte[] toLexBytes(WalKey t) throws Exception {
                return toBytes(t);
            }
        };
    }
    
    private TypeMarshaller<ObjectNode> valueMarshaller(final ObjectMapper mapper) {
        return new TypeMarshaller<ObjectNode>() {
            @Override
            public ObjectNode fromBytes(byte[] bytes) throws Exception {
                return mapper.readValue(bytes, ObjectNode.class);
            }

            @Override
            public byte[] toBytes(ObjectNode t) throws Exception {
                return mapper.writeValueAsBytes(t);
            }

            @Override
            public ObjectNode fromLexBytes(byte[] lexBytes) throws Exception {
                return fromBytes(lexBytes);
            }

            @Override
            public byte[] toLexBytes(ObjectNode t) throws Exception {
                return toBytes(t);
            }
        };
    }
    
    private HBaseSetOfSortedMapsImplInitializer.HBaseSetOfSortedMapsConfig hBaseConfig(final Config config) {
        return  new HBaseSetOfSortedMapsImplInitializer.HBaseSetOfSortedMapsConfig() {

            @Override
            public String getHBaseZookeeperQuorum() {
                return config.hbaseQuorum;
            }

            @Override
            public void setHBaseZookeeperQuorum(String hbaseZookeeperQuorum) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Integer getHBaseZookeeperPort() {
                return config.hbasePort;
            }

            @Override
            public String name() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void applyDefaults() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            
             @Override
            public void setHBaseZookeeperPort(Integer hbaseZookeeperPort) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }
}
