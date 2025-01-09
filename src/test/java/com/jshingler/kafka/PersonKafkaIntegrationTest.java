package com.jshingler.kafka;

import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

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

        try {
            // When
            kafkaProducer.sendPerson("person-topic", person);

            // Then
            Awaitility.await()
                    .atMost(10, TimeUnit.SECONDS)
                    .untilAsserted(() -> {
                        assertThat(kafkaConsumer.getReceivedPerson()).isNotNull();
                        assertThat(kafkaConsumer.getReceivedPerson()).isEqualTo(person);
                    });
        } catch (ConditionTimeoutException e) {
            System.out.println("Awaitility timed out waiting for the condition to be met.");
            e.printStackTrace();
            fail("Timed out waiting for the condition to be met.");
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();

        }
    }
}
