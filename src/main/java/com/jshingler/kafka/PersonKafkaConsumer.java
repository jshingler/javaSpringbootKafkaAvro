package com.jshingler.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PersonKafkaConsumer {

    private Person receivedPerson;

    @KafkaListener(topics = "person-topic", groupId = "test-group")
    public void consume(Person person) {
        this.receivedPerson = person;
    }

    public Person getReceivedPerson() {
        return receivedPerson;
    }
}
