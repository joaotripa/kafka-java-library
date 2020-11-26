/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.rics.uninova.kafka.library;

import io.confluent.kafka.serializers.KafkaJsonDeserializerConfig;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import pt.rics.uninova.kafka.library.model.DataRecord;

/**
 *
 * @author joao
 */
public class Consumer {
    
    protected final Properties props;
    
    private final org.apache.kafka.clients.consumer.Consumer<String, DataRecord> consumer;
    
    public Consumer(String configFile) throws IOException {
        
        // Load properties from a local configuration file
        // Create the configuration file (e.g. at '$HOME/.confluent/java.config') with configuration parameters
        // to connect to your Kafka cluster, which can be on your local host, Confluent Cloud, or any other cluster.
        // Follow these detailed instructions to properly create this file: https://github.com/confluentinc/configuration-templates/tree/master/README.md
        props = loadConfig(configFile);

        // Add additional properties.
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaJsonDeserializer");
        props.put(KafkaJsonDeserializerConfig.JSON_VALUE_TYPE, DataRecord.class);
        //Change consumer group ID if multiple consumers subscribe the same topic
        //or only one will receive the messages
        //the others will only receive if the first goes offline
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-x"); 
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        consumer = new KafkaConsumer<String, DataRecord>(props);
    }
    
    public void subscribe(String topic){
        consumer.subscribe(Arrays.asList(topic));
    }
    
    public void subscribe(ArrayList<String> topic){
        consumer.subscribe(topic);
    }
    
    public void unsubscribe(){
        consumer.unsubscribe();
    }
    
    
    public ConsumerRecords<String, DataRecord> consume(){
        return consumer.poll(Duration.ofMillis(100));           
    }
    
    public void close(){
        consumer.close();
    }
    
    public static Properties loadConfig(String configFile) throws IOException {
        if (!Files.exists(Paths.get(configFile))) {
          throw new IOException(configFile + " not found.");
        }
        final Properties cfg = new Properties();
        try (InputStream inputStream = new FileInputStream(configFile)) {
            cfg.load(inputStream);
        }
        return cfg;
    }
    
}
