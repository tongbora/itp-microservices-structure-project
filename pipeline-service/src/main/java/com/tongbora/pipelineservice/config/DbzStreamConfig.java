package com.tongbora.pipelineservice.config;

import ITP.CORE_BANKING.RECORD_XML.Envelope;
import ITP.CORE_BANKING.RECORD_XML.Key;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class DbzStreamConfig {

    @Bean
    public Consumer<Message<Envelope>> captureEnvelope() {
        return record -> {
            System.out.println("Dbz envelope: " + record.getPayload()
                    .getAfter());
        };
    }

    @Bean
    public Function<Message<Object>, Record> consumeDbzEvent(ObjectMapper objectMapper) {
        return record -> {
            try {
                DebeziumEnvelope<Record> capturedRecord =
                        objectMapper.readValue(record.getPayload().toString(),
                                new TypeReference<>(){});
                return switch (capturedRecord.getOp()) {
                    case "r", "c" -> {
                        System.out.println("Prepare to insert new record");
                        Record after = capturedRecord.getAfter();
                        System.out.println(after.getXmldata().getName());
                        yield after;
                    }
                    case "u" -> {
                        System.out.println("Prepare to update existing record");
                        Record after = capturedRecord.getAfter();
                        System.out.println("Updated: " + after.getXmldata().getName());
                        yield after;
                    }
                    case "d" -> {
                        System.out.println("Prepare to delete existing record");
                        System.out.println("Delete ID = " + capturedRecord.getBefore().getRecid());
                        yield capturedRecord.getBefore();
                    }
                    default -> throw new IllegalStateException("Invalid Operation..!");
                };
            } catch (JsonProcessingException e) {
                System.out.println("Error deserialized");
                throw new RuntimeException("Error deserialized");
            }
        };
    }

}
