package com.jshingler.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PersonKafkaProducer {

    private final KafkaTemplate<String, Person> kafkaTemplate;

    @Autowired
    public PersonKafkaProducer(KafkaTemplate<String, Person> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPerson(String topic, Person person) {
        kafkaTemplate.send(topic, person);
    }
}
