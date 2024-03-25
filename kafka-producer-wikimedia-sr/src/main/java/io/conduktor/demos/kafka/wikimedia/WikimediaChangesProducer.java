package io.conduktor.demos.kafka.wikimedia;

import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.EventHandler;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class WikimediaChangesProducer {
    public static void main(String[] args) throws InterruptedException {

        // create Producer properties
        Properties properties = new Properties();

        // Upstash cluster properties
        properties.put("bootstrap.servers", "https://driving-anemone-14951-us1-kafka.upstash.io:9092");
        properties.put("sasl.mechanism", "SCRAM-SHA-256");
        properties.put("security.protocol", "SASL_SSL");
        properties.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"ZHJpdmluZy1hbmVtb25lLTE0OTUxJOOUD-QWV9p9W116XVhMgWQkRVx_FvKlBac\" password=\"YWUxMDAyMTAtYWNhOS00OTg5LWEwMmQtOWE4NzdlYmFmZTIx\";");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // create the Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        String topic = "wikimedia.recentchange";

        //docker properties
//        properties.setProperty("bootstrap.servers", "kafka:19092");
//        properties.setProperty("security.protocol", "SASL_SSL");
//        properties.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"your-username\" password=\"your-password\";");
//        properties.setProperty("sasl.mechanism", "PLAIN");
//
        // set producer properties
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer", StringSerializer.class.getName());

        //set additional producer properties (for kafka <= 2.8.x)
//        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
//        properties.put(ProducerConfig.ACKS_CONFIG, "all"); //same as setting -1
//        properties.put(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));

//        //optimizing producer messages
//        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy"); //snappy is fast and efficient - by default, it is none
//        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "20"); //default 16
//        properties.put(ProducerConfig.LINGER_MS_CONFIG, Integer.toString(32 * 1024));
//
//        //High throughput options
//        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, Integer.toString(32 * 1024));
//        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, Integer.toString(60 * 1000)); //1 minute

        //handle the timer events to generate the producer messages gathered from the provided URL
        EventHandler eventHandler = new WikimediaChangeHandler(producer, topic);
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url));
        EventSource eventSource = builder.build();


        //start the producer in another thread
        eventSource.start();

        //handle the events in the Handler
        TimeUnit.MINUTES.sleep(10);
    }
}
