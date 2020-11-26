/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.rics.uninova.kafka.library;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import pt.rics.uninova.kafka.library.model.DataRecord;

/**
 *
 * @author joao
 */
public class ConsumerExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        if (args.length != 2) {
            System.out.println("Please provide command line arguments: configPath");
            System.exit(1);
        }
        
        Consumer consumer = new Consumer(args[0]);
        String topic = args[1];
        /* If more than one topic, use an ArrayList
        ArrayList topics = new ArrayList<String>();
        topics.add("topic1")
        topics.add("topic2")
        topics.add("topic3")
        */
        
        consumer.subscribe(topic);
        
        Long total_count = 0L;
        
        try {
            while (true) {
                ConsumerRecords<String, DataRecord> records = consumer.consume();
                for (ConsumerRecord<String, DataRecord> record : records) {
                    String key = record.key();
                    DataRecord value = record.value();
                    /*****************************************/
                    /*    Do something with the data here    */
                    /*****************************************/
                    total_count += 1;
                    System.out.printf("Consumed record with key %s and value %s, and updated total count to %d%n", key, value, total_count);
                }
            }
        } finally {
            consumer.close();
        }
    }
    
}
