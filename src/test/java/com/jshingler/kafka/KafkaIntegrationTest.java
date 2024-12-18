package com.jshingler.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"test-topic"}, bootstrapServersProperty = "spring.kafka.bootstrap-servers")
@DirtiesContext
public class KafkaIntegrationTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final BlockingQueue<ConsumerRecord<String, String>> records = new LinkedBlockingQueue<>();

    @KafkaListener(topics = "test-topic", groupId = "test-group")
    public void consume(ConsumerRecord<String, String> record) {
        records.add(record);
    }

    @Test
    public void testKafkaProducerAndConsumer() throws InterruptedException {
        String testMessage = "Hello, Kafka!";

        // Send a message
        kafkaTemplate.send("test-topic", testMessage);

        // Consume the message
        ConsumerRecord<String, String> received = records.poll(10, TimeUnit.SECONDS);

        assertThat(received).isNotNull();
        assertThat(received.value()).isEqualTo(testMessage);
    }
}
