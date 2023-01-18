package io.conduktor.demos.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemoCooperative {

    private static final Logger log = LoggerFactory.getLogger(ConsumerDemoCooperative.class.getSimpleName());

    public static void main(String[] args) {
        log.info("I am a Kafka Consumer!");

        String groupId = "my-java-application";
        String topic = "demo_java";

        // create Producer Properties
        Properties properties = new Properties();

        // connect to Localhost
//        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");

        // connect to Conduktor Playground
        properties.setProperty("bootstrap.servers", "cluster.playground.cdkt.io:9092");
        properties.setProperty("security.protocol", "SASL_SSL");
        properties.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"your-username\" password=\"your-password\";");
        properties.setProperty("sasl.mechanism", "PLAIN");

        // create consumer configs
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());
        properties.setProperty("group.id", groupId);
        properties.setProperty("auto.offset.reset", "earliest");
        properties.setProperty("partition.assignment.strategy", CooperativeStickyAssignor.class.getName());
//        properties.setProperty("group.instance.id", "...."); // strategy for static assignments


        // create a consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // get a reference to the main thread
        final Thread mainThread = Thread.currentThread();

        // adding the shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                log.info("Detected a shutdown, let's exit by calling consumer.wakeup()...");
                consumer.wakeup();

                // join the main thread to allow the execution of the code in the main thread
                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });



        try {
            // subscribe to a topic
            consumer.subscribe(Arrays.asList(topic));
            // poll for data
            while (true) {

                ConsumerRecords<String, String> records =
                        consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, String> record : records) {
                    log.info("Key: " + record.key() + ", Value: " + record.value());
                    log.info("Partition: " + record.partition() + ", Offset: " + record.offset());
                }

            }

        } catch (WakeupException e) {
            log.info("Consumer is starting to shut down");
        } catch (Exception e) {
            log.error("Unexpected exception in the consumer", e);
        } finally {
            consumer.close(); // close the consumer, this will also commit offsets
            log.info("The consumer is now gracefully shut down");
        }


    }
}
