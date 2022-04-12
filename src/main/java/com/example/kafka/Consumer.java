package com.example.kafka;

import com.example.Employee;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    @KafkaListener(topics = "springavrotopic")
    public void onEmployeeSave(ConsumerRecord<String, Employee> msg){
        Employee emp = msg.value();
        log.info("Employee recive for kafka:[{}]", emp);
    }
}
