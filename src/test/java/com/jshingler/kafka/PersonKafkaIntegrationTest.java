package com.jshingler.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"person-topic"}, bootstrapServersProperty = "spring.kafka.bootstrap-servers")
@DirtiesContext
public class PersonKafkaIntegrationTest {

    @Autowired
    private PersonKafkaProducer kafkaProducer;

    @Autowired
    private PersonKafkaConsumer kafkaConsumer;

    @Test
    public void testSendAndConsumePerson() throws InterruptedException {
        // **IMPORTANT:** ValidatedString must be registered before parsing the schema
        // Could be done in application startup to minimize impact
        ValidatedString.register();
        // Given
        Person person = new Person("John", "Doe", "123 Main Street");

        // When
//        kafkaProducer.sendPerson("person-topic", person);
//        Exception exception = assertThrows(java.lang.IllegalArgumentException.class, () -> {
            kafkaProducer.sendPerson("person-topic", person);
//        });

        // Wait for the consumer to process the message
        Thread.sleep(3000);

        // Then
//        assertThat(kafkaConsumer.getReceivedPerson()).isNotNull();
//        assertThat(kafkaConsumer.getReceivedPerson()).isEqualTo(person);
//        Exception exception2 = assertThrows(RuntimeException.class, () -> {
        Exception exception = assertThrows(java.lang.IllegalArgumentException.class, () -> {

            kafkaConsumer.getReceivedPerson();
        });
//
//        // Optionally verify the exception message
        assertThat(exception.getMessage()).contains("Expected error message");
    }
}
