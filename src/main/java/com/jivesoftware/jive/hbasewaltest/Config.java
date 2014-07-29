package com.jivesoftware.jive.hbasewaltest;

import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

/**
 *
 */
public class Config {
    public String hbaseQuorum = "localhost";
    public int hbasePort = 2181;
    public int batchSize = 15;
    public int numPartitions = 20;
    public long secondsToRun = 60;
    public long pauseMillis = 500;
    public int writerId = 1;
    
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
        try {
            System.out.println(mapper.writeValueAsString(new Config()));
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
        
    }
}
