package com.jshingler.kafka;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.FailedDeserializationInfo;
//import org.springframework.util.function.Function;

import java.util.function.BiConsumer;

public class CustomErrorHandlingDeserializer extends ErrorHandlingDeserializer<GenericRecord> {

    public CustomErrorHandlingDeserializer() {
        super();
    }

    @Override
    public GenericRecord deserialize(String topic, Headers headers, byte[] data) {
        try {

            return super.deserialize(topic, headers, data);
        } catch (SerializationException e) {
            // Here you can handle the error, e.g., log it, send to a dead letter queue, or return a default object
            // For More Info: https://chatgpt.com/g/g-p-67660ee324548191b0bd27c1ba5aed95-kafka-avro/c/67774e7f-4f14-8010-86bb-30800dfd5107
            // @DLTHandler can simplify
            System.err.println("Deserialization error: " + e.getMessage());
            // Optionally, return a default object or null
            return null;
        }
    }

//    @Override
    protected void handleDeserializationException(BiConsumer<FailedDeserializationInfo, Exception> consumer, FailedDeserializationInfo info, Exception exception) {
        if (exception instanceof IllegalArgumentException && exception.getMessage().contains("String does not match the pattern")) {
            // Specific handling for regex validation failure
            System.err.println("Validation error: " + exception.getMessage());
            // Here you could add logic to send the message to a dead-letter topic or log it differently
        } else {
            // General handling for other deserialization errors
//            super.handleDeserializationException(consumer, info, exception);
            System.err.println("Deserialization error: " + exception.getMessage());
        }
    }

//    // Function to create a default object or handle the failure in some specific way
//    private final Function<FailedDeserializationInfo, GenericRecord> failedDeserializationFunction = info -> {
//        // Example: Return a default record or log the failure
//        System.err.println("Failed to deserialize: " + new String(info.getData()));
//        return new GenericData.Record(info.getSchema());
//    };
//
//    @Override
//    protected Function<FailedDeserializationInfo, GenericRecord> failedDeserializationFunction() {
//        return failedDeserializationFunction;
//    }
}
