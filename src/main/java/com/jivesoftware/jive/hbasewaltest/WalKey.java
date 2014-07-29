package com.jivesoftware.jive.hbasewaltest;

import java.nio.ByteBuffer;

/**
 *
 */
public class WalKey implements Comparable<WalKey> {
    private final long snowflake;
    private final short partition;

    public WalKey(long snowflake, short partition) {
        this.snowflake = snowflake;
        this.partition = partition;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.snowflake ^ (this.snowflake >>> 32));
        hash = 97 * hash + this.partition;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WalKey other = (WalKey) obj;
        if (this.snowflake != other.snowflake) {
            return false;
        }
        if (this.partition != other.partition) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(WalKey o) {
        int comparison =  Long.compare(this.snowflake, o.snowflake);
        if (comparison == 0) {
            comparison = Short.compare(this.partition, o.partition);
        }
        
        return comparison;
    }
    
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(Short.SIZE+Long.SIZE);
        buffer.putShort(partition);
        buffer.putLong(snowflake);
        return buffer.array();
    }
    
    public static WalKey fromBytes(byte[] bytes) {
        if (bytes.length == Short.SIZE + Long.SIZE) {
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            short partition = buffer.getShort();
            long snowflake = buffer.getLong();
            
            return new WalKey(snowflake, partition);
        }
        throw new IllegalArgumentException("Supplied bytes must contain a short and a long");
    }
    
    
    
}
