/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.rics.uninova.kafka.library;

import java.io.IOException;
import static pt.rics.uninova.kafka.library.Constants.PARTITIONS;
import static pt.rics.uninova.kafka.library.Constants.REPLICATION;
import pt.rics.uninova.kafka.library.model.DataRecord;

/**
 *
 * @author joao
 */
public class ProducerExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Please provide command line arguments: configPath topic");
            System.exit(1);
        }
        String configFile = args[0];
        Producer producer = new Producer(configFile);
        
        String topic = args[1];
        producer.createTopic(topic, PARTITIONS, REPLICATION, producer.loadConfig(configFile)); //Ignored if topic already exists.
        
        // Produce sample data
        final int numMessages = 10;
        for (int i = 0; i < numMessages; i++) {
            String key = "Products";
            String id = "product_" + i;
            String description;
            String specs;
            if (i%2 !=0 ){
                description = "Shoe";
                specs = "White, Blue, Red";
            }
            else{
                description = "Computer";
                specs = "Asus";
            }
            
            DataRecord record = new DataRecord(id, description, specs);
            
            System.out.printf("Producing record: %s\t%s%n", key, record);
            producer.produce(topic, key, record);
        }
        producer.flush();
        
        System.out.printf(numMessages + " messages were produced to topic %s%n", topic);
        
        producer.close();
    }
    
}
