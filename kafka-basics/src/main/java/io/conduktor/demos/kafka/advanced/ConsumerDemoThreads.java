package io.conduktor.demos.kafka.advanced;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class ConsumerDemoThreads {

    public static void main(String[] args)  {
        ConsumerDemoWorker consumerDemoWorker = new ConsumerDemoWorker();
        new Thread(consumerDemoWorker).start();
        Runtime.getRuntime().addShutdownHook(new Thread(new ConsumerDemoCloser(consumerDemoWorker)));
    }

    private static class ConsumerDemoWorker implements Runnable {

        private static final Logger log = LoggerFactory.getLogger(ConsumerDemoWorker.class);

        private CountDownLatch countDownLatch;
        private Consumer<String, String> consumer;

        @Override
        public void run() {
            countDownLatch = new CountDownLatch(1);
            final Properties properties = new Properties();

//        properties.setProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
            properties.setProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "cluster.playground.cdkt.io:9092");
            properties.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
            properties.setProperty(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"6hiWlwa3a3RibZIBq2lNEP\" password=\"8a575166-4c7d-4900-ad2c-f2b4a510f0ce\";");
            properties.setProperty(SaslConfigs.SASL_MECHANISM, "PLAIN");
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "my-sixth-application");
            properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

            consumer = new KafkaConsumer<>(properties);
            consumer.subscribe(Collections.singleton("demo_java"));

            final Duration pollTimeout = Duration.ofMillis(100);

            try {
                while (true) {
                    final ConsumerRecords<String, String> consumerRecords = consumer.poll(pollTimeout);
                    for (final ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                        log.info("Getting consumer record key: '" + consumerRecord.key() + "', value: '" + consumerRecord.value() + "', partition: " + consumerRecord.partition() + " and offset: " + consumerRecord.offset() + " at " + new Date(consumerRecord.timestamp()));
                    }
                }
            } catch (WakeupException e) {
                log.info("Consumer poll woke up");
            } finally {
                consumer.close();
                countDownLatch.countDown();
            }
        }

        void shutdown() throws InterruptedException {
            consumer.wakeup();
            countDownLatch.await();
            log.info("Consumer closed");
        }

    }

    private static class ConsumerDemoCloser implements Runnable {

        private static final Logger log = LoggerFactory.getLogger(ConsumerDemoCloser.class);

        private final ConsumerDemoWorker consumerDemoWorker;

        ConsumerDemoCloser(final ConsumerDemoWorker consumerDemoWorker) {
            this.consumerDemoWorker = consumerDemoWorker;
        }

        @Override
        public void run() {
            try {
                consumerDemoWorker.shutdown();
            } catch (InterruptedException e) {
                log.error("Error shutting down consumer", e);
            }
        }
    }
}