/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.rics.uninova.kafka.library;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.TopicExistsException;
import pt.rics.uninova.kafka.library.model.DataRecord;

/**
 *
 * @author joao
 */
public class Producer {
    
    protected final Properties props;
    
    private static org.apache.kafka.clients.producer.Producer<String, DataRecord> producer;
            
    public Producer(String configFile) throws IOException {
        // Load properties from a local configuration file
        // Create the configuration file (e.g. at '$HOME/.confluent/java.config') with configuration parameters
        // to connect to your Kafka cluster, which can be on your local host, Confluent Cloud, or any other cluster.
        // Follow these detailed instructions to properly create this file: https://github.com/confluentinc/configuration-templates/tree/master/README.md
        props = loadConfig(configFile);

        // Add additional properties.
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaJsonSerializer");

        producer = new KafkaProducer<String, DataRecord>(props);
    }
    
    
    public Properties loadConfig(String configFile) throws IOException {
        if (!Files.exists(Paths.get(configFile))) {
          throw new IOException(configFile + " not found.");
        }
        final Properties cfg = new Properties();
        try (InputStream inputStream = new FileInputStream(configFile)) {
          cfg.load(inputStream);
        }
        return cfg;
    }
    
    // Create topic in Confluent Cloud 
    // Only Kafka Producers should create new topics
    public void createTopic(final String topic,
                            final int partitions,
                            final int replication,
                            final Properties cloudConfig) {
        final NewTopic newTopic = new NewTopic(topic, partitions, (short) replication);
        try (final AdminClient adminClient = AdminClient.create(cloudConfig)) {
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
        } catch (final InterruptedException | ExecutionException e) {
            // Ignore if TopicExistsException, which may be valid if topic exists
            if (!(e.getCause() instanceof TopicExistsException)) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public void produce(String topic, String key, DataRecord record){
        producer.send(new ProducerRecord<String, DataRecord>(topic, key, record), new Callback() {
            @Override
            public void onCompletion(RecordMetadata m, Exception e) {
              if (e != null) {
                e.printStackTrace();
              } else {
                System.out.printf("Produced record to topic %s partition [%d] @ offset %d%n", m.topic(), m.partition(), m.offset());
              }
            }
        });
    }
    
    public void flush(){
        producer.flush();
    }
    
    public void close(){
        producer.close();
    }
    
    
}
