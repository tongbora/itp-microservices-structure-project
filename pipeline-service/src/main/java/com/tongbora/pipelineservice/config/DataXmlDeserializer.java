package com.tongbora.pipelineservice.config;

public class DataXmlDeserializer extends XmlStringDeserializer<Data>{
    public DataXmlDeserializer() {
        super(Data.class);
    }
}
