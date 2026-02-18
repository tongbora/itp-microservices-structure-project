package com.tongbora.pipelineservice.config;

import lombok.Data;

@Data
public class DebeziumEnvelope<T> {
    private String op;  // "c", "r", "u", "d"
    private T before;
    private T after;
}
