package com.project.auth.domain.infra.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthDltListeners {

    private final DltLogger dlt;

    @KafkaListener(topics = "auth.token-delete.command.dlt", groupId = "auth-service")
    public void onTokenDeleteCmdDlt(
            ConsumerRecord<String,String> rec,
            @Header(name = KafkaHeaders.DLT_ORIGINAL_TOPIC, required = false)   String oTopic,
            @Header(name = KafkaHeaders.DLT_ORIGINAL_PARTITION, required = false) Integer oPart,
            @Header(name = KafkaHeaders.DLT_ORIGINAL_OFFSET, required = false)   Long oOffset,
            @Header(name = KafkaHeaders.DLT_EXCEPTION_FQCN, required = false)    String exClass,
            @Header(name = KafkaHeaders.DLT_EXCEPTION_MESSAGE, required = false) String exMsg
    ){
        dlt.logAndCount("auth.token-delete.command.dlt", rec, oTopic, oPart, oOffset, exClass, exMsg);
    }
}