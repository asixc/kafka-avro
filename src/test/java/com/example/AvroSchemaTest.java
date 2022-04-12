package com.example;

import com.example.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.avro.schema.AvroSchemaGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.avro.Schema;
import org.junit.jupiter.api.Test;



public class AvroSchemaTest {

    @Test
    void generateSchema() throws Exception {
        // ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()); // Necesario para LocalDate añadir el modulo (.registerModule(new JavaTimeModule())) + la dependencia:
        /*
        <!--	Java 8 Date/time	-->
		<dependency>
			<groupId>com.fasterxml.jackson.datatype</groupId>
			<artifactId>jackson-datatype-jsr310</artifactId>
		</dependency>
         */
//         AvroSchemaGenerator generator = new AvroSchemaGenerator();
//
//         mapper.acceptJsonFormatVisitor(Employee.class, generator);
//         Schema avroSchema = generator.getGeneratedSchema().getAvroSchema();
//
//         System.out.println(avroSchema.toString(true));
    }
}
