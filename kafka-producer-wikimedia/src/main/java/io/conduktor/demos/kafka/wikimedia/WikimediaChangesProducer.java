package io.conducktor.kafka.wiki;

import java.net.URI;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.launchdarkly.okhttp.eventsource.EventHandler;   // ← 3.x package (with okhttp)
import com.launchdarkly.okhttp.eventsource.EventSource;    // ← 3.x package (with okhttp)
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * Streams Wikimedia RecentChange SSE and forwards lines to a Kafka topic.
 * Requires:
 *  - Kafka broker at localhost:9092
 *  - Dependency: com.launchdarkly:okhttp-eventsource:3.x (classic EventHandler API)
 *  - Send a proper User-Agent header to avoid 403 from Wikimedia
 */
public class WikimediaChangesProducer {
    public static void main(String[] args) throws InterruptedException {
        // Kafka producer config
        final String bootstrapServers = "localhost:9092";
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Create the producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // Target topic
        final String topic = "wikimedia.recentchange";

        // Wikimedia requires a meaningful User-Agent
        Headers requestHeaders = new Headers.Builder()
                .add("User-Agent", "user-wiki-stream/1.0 (user@example.com)")
                .build();

        // Create event handler to send SSE messages to Kafka
        OkHttpClient client = new OkHttpClient();
        EventHandler eventHandler = new WikimediaChangeHandler(producer, topic);
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";

        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url))
                .client(client)
                .headers(requestHeaders)                   // 3.x expects a single Headers object
                .reconnectTime(Duration.ofSeconds(3));

        EventSource eventSource = builder.build();

        // Start the SSE client in another thread
        eventSource.start();

        // Stream for 10 minutes
        TimeUnit.MINUTES.sleep(10);

        // Graceful shutdown
        eventSource.close();
        producer.flush();
        producer.close();
    }
}
