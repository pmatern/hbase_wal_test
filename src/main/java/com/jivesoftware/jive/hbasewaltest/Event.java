package com.jivesoftware.jive.hbasewaltest;

import org.codehaus.jackson.node.ObjectNode;

/**
 *
 */
public class Event {
    private final long tenantId;
    private final WalKey key;
    private final ObjectNode eventData;

    public Event(long tenantId, WalKey key, ObjectNode eventData) {
        this.tenantId = tenantId;
        this.key = key;
        this.eventData = eventData;
    }

    public WalKey getKey() {
        return key;
    }

    public long getTenantId() {
        return tenantId;
    }

    public ObjectNode getEventData() {
        return eventData;
    }
    
    
}
