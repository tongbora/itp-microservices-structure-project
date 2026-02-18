package com.tongbora.pipelineservice.config;
//import co.istad.dara.pipeline_service.stream.avro.DebeziumMessage;
//import co.istad.dara.pipeline_service.stream.avro.ProductAvro;
import org.apache.avro.generic.GenericRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class StreamConfig {

    // Supplier for producing message into kafka topic
    // Function for processing message and send to destination kafka topic
    // Consumer for consuming message from kafka topic

    @Bean
    public Consumer<Product> processProduct() {
        return product -> {
            System.out.println("obj product: " + product.getCode());
            System.out.println("obj product: " + product.getQty());
        };
    }

    @Bean
    public Function<Product,Product> processProductDetail(){
        return product -> {
            System.out.println("old product: " + product.getCode());
            System.out.println("old product: " + product.getQty());

            // Processing change data
            product.setCode("Dara's code: " + product.getCode());

            // Return process data
            return product;
        };
    }


    // A simple processor: Takes a string, makes it uppercase, and sends it on
    @Bean
    public Consumer<String> processMessage() {
        return input -> {
            System.out.println("Processing: " + input);
        };
    }

    // Consume from debezium
    @Bean
    public Consumer<GenericRecord> processDebeziumProduct() {
        return record -> {
            try {
                System.out.println("=== Debezium Event (Avro) ===");

                // Extract operation type
                String operation = record.get("op").toString();
                System.out.println("Operation: " + operation);

                // Extract source metadata
                GenericRecord source = (GenericRecord) record.get("source");
                if (source != null) {
                    System.out.println("Database: " + source.get("db"));
                    System.out.println("Table: " + source.get("table"));
                }

                // Handle different operations
                switch (operation) {
                    case "c":  // CREATE
                    case "r":  // READ (initial snapshot)
                        GenericRecord afterCreate = (GenericRecord) record.get("after");
                        System.out.println("New Product: " + extractProduct(afterCreate));
                        handleNewProduct(afterCreate);
                        break;

                    case "u":  // UPDATE
                        GenericRecord before = (GenericRecord) record.get("before");
                        GenericRecord after = (GenericRecord) record.get("after");
                        System.out.println("Before: " + extractProduct(before));
                        System.out.println("After: " + extractProduct(after));
                        handleUpdateProduct(before, after);
                        break;

                    case "d":  // DELETE
                        GenericRecord beforeDelete = (GenericRecord) record.get("before");
                        System.out.println("Deleted: " + extractProduct(beforeDelete));
                        handleDeleteProduct(beforeDelete);
                        break;

                    default:
                        System.out.println("Unknown operation: " + operation);
                }

            } catch (Exception e) {
                System.err.println("Error processing Debezium message: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }

    private void handleNewProduct(GenericRecord product) {
        if (product == null) return;


        System.out.println("Processing new product:");
        System.out.println("  ID: " + extractValue(product.get("id")));
        System.out.println("  Code: " + extractValue(product.get("code")));
        System.out.println("  Qty: " + extractValue(product.get("qty")));
    }

    private void handleUpdateProduct(GenericRecord before, GenericRecord after) {
        if (before == null || after == null) return;

        System.out.println("Processing product update:");
        System.out.println("  Old Code: " + extractValue(before.get("code")) +
                " -> New Code: " + extractValue(after.get("code")));
        System.out.println("  Old Qty: " + extractValue(before.get("qty")) +
                " -> New Qty: " + extractValue(after.get("qty")));
    }

    private void handleDeleteProduct(GenericRecord product) {
        if (product == null) return;

        System.out.println("Processing product deletion:");
        System.out.println("  Deleted ID: " + extractValue(product.get("id")));
        System.out.println("  Deleted Code: " + extractValue(product.get("code")));
    }

    // Helper method to extract Avro values (handles union types)
    private Object extractValue(Object value) {
        if (value instanceof GenericRecord) {
            GenericRecord record = (GenericRecord) value;
            // Try common Avro union field names
            if (record.hasField("string")) return record.get("string");
            if (record.hasField("int")) return record.get("int");
            if (record.hasField("long")) return record.get("long");
        }
        return value;
    }

    // Helper to create readable product string
    private String extractProduct(GenericRecord product) {
        if (product == null) return "null";

        return String.format("Product{id=%s, code=%s, qty=%s}",
                extractValue(product.get("id")),
                extractValue(product.get("code")),
                extractValue(product.get("qty"))
        );
    }

}
