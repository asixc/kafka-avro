package com.example.controller;

import com.example.Employee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    private final KafkaTemplate<String, Employee> kafkaTemplate;

    public EmployeeController(KafkaTemplate<String, Employee> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping()
    public ResponseEntity<String> produceMsg(@RequestBody Employee employee) {
        log.info("Call controller post: [{}]", employee);

        ListenableFuture<SendResult<String, Employee>> future = this.kafkaTemplate.sendDefault(employee);

        future.addCallback(
                result -> logResult(result),  // Success callback
                exception -> log.error("Error sending, message", exception) // Error callback
        );
        return ResponseEntity.ok("Sent employee..");
    }

    private void logResult(SendResult<String, Employee> result) {
        log.info("partition {}", result.getRecordMetadata().partition());
        log.info("offset {}", result.getRecordMetadata().offset());
    }


}
