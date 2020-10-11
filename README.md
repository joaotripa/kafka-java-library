# Kafka Java

A Kafka Library for Java clients

For more information see (https://docs.confluent.io/current/tutorials/examples/clients/docs/java.html#client-examples-java)

## Prerequisites

Kafka Cluster running: 
    - on a local machine
    - in a cloud environment

Java

Maven

## Setup

Create a local file (for example, at $HOME/.confluent/lbrkafka.config) with configuration parameters to connect to your Kafka cluster.

    - Template configuration file for Confluent Cloud

        # Kafka
        bootstrap.servers={{ BROKER_ENDPOINT }}
        security.protocol=SASL_SSL
        sasl.mechanisms=PLAIN
        sasl.username={{ CLUSTER_API_KEY }}
        sasl.password={{ CLUSTER_API_SECRET }}


    - Template configuration file for local host

        # Kafka
        bootstrap.servers=localhost:9092

Inilialize Kafka Cluster 
	
	cd ../kafka-java-library/kafka-docker
	docker-compose up -d

Compile Java Code

	mvn clean package

## Running the Producer

mvn exec:java -Dexec.mainClass="pt.rics.uninova.kafka.library.ProducerExample" \
-Dexec.args="$HOME/.confluent/lbrkafka.config test1"

## Running the Consumer

mvn exec:java -Dexec.mainClass="pt.rics.uninova.kafka.library.ConsumerExample" \
-Dexec.args="$HOME/.confluent/lbrkafka.config test1"

## Running from Netbeans IDE

Clean Project

Run with Producer configurations

Run with Consumer configurations
