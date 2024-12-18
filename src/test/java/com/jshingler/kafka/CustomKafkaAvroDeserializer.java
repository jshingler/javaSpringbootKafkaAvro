package com.jshingler.kafka;

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.avro.Schema;

/**
 * This code is not thread safe and should not be used in production environment
 */
//https://gist.github.com/ivlahek/dee47cd2b5bd7e943fe039bbc29d27a2
//https://medium.com/@igorvlahek1/no-need-for-schema-registry-in-your-spring-kafka-tests-a5b81468a0e1
//https://github.com/ivlahek/kafka-avro-without-registry
// Find Constants here
//https://github.com/ivlahek/kafka-avro-without-registry/blob/master/showcase-service/src/main/java/hr/ivlahek/showcase/Constants.java
public class CustomKafkaAvroDeserializer extends KafkaAvroDeserializer {
    @Override
    public Object deserialize(String topic, byte[] bytes) {
        if (topic.equals(Constants.PERSON_TOPIC)) {
            this.schemaRegistry = getMockClient(Person.SCHEMA$);
        }
//        if (topic.equals(Constants.EVENT_2_TOPIC)) {
//            this.schemaRegistry = getMockClient(Event2.SCHEMA$);
//        }
//        if (topic.equals(Constants.EVENT_3_TOPIC)) {
//            this.schemaRegistry = getMockClient(Event3.SCHEMA$);
//        }
//        if (topic.equals(Constants.EVENT_4_TOPIC)) {
//            this.schemaRegistry = getMockClient(Event4.SCHEMA$);
//        }
        return super.deserialize(topic, bytes);
    }

    private static SchemaRegistryClient getMockClient(final Schema schema$) {
        return new MockSchemaRegistryClient() {
            @Override
            public synchronized Schema getById(int id) {
                return schema$;
            }
        };
    }
}


/*
Sample Config:

spring.kafka.bootstrap-servers = ${spring.embedded.kafka.brokers}
kafka.bootstrap-servers = ${spring.embedded.kafka.brokers}

spring.kafka.producer.properties.schema.registry.url= not-used
spring.kafka.producer.value-serializer = hr.ivlahek.showcase.mock.CustomKafkaAvroSerializer
spring.kafka.producer.key-serializer = org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.group-id = showcase-producer-id

spring.kafka.consumer.properties.schema.registry.url= not-used
spring.kafka.consumer.value-deserializer = hr.ivlahek.showcase.mock.CustomKafkaAvroDeserializer
spring.kafka.consumer.key-deserializer = org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.group-id = showcase-consumer-id
spring.kafka.auto.offset.reset = earliest

spring.kafka.producer.auto.register.schemas= true
spring.kafka.properties.specific.avro.reader= true
 */
