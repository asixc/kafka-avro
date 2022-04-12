## 1 Crear el fichero docker-compose

```shell
docker-compose up -d
```
## 2 Añadimos la dependencia apache avro
```shell
<!-- https://mvnrepository.com/artifact/org.apache.avro/avro -->
<dependency>
    <groupId>org.apache.avro</groupId>
    <artifactId>avro</artifactId>
    <version>1.11.0</version>
</dependency>
```
## 3 Generamos las clases avro mediante el test: 
 Para eso hemos añadido las entidades en com.exmaple.domain.Employee y com.exmaple.domain.Car
Creamos el test: 
```java
public class AvroSchemaTest {

    @Test
    void generateSchema() throws Exception {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()); // Necesario para LocalDate añadir el modulo (.registerModule(new JavaTimeModule())) + la dependencia:
        /*
        <!--	Java 8 Date/time	-->
		<dependency>
			<groupId>com.fasterxml.jackson.datatype</groupId>
			<artifactId>jackson-datatype-jsr310</artifactId>
		</dependency>
         */
        AvroSchemaGenerator generator = new AvroSchemaGenerator();
        mapper.acceptJsonFormatVisitor(Employee.class, generator);
        Schema avroSchema = generator.getGeneratedSchema().getAvroSchema();

        System.out.println(avroSchema.toString(true));
    }
}
```
Copiamos lo que imprime por consola y creamos el fichero resources.avro.employee.avsc y pegamos.
Una vez hecho esto podemos borrar las entidades Employee y Car (en mi caso la he movido a un directorio \kafka-avro\entities dentro del proyecto por si las necesito de nuevo)

Siguiente paso es hacer "mvn clean" del proyecto y a continuacion "mvn compile" esto nos debe de generar target.generated-sources.com.exmples.Car & Employee en una carpeta azul para poder usar estas clases por ejemplo en el controller.

!Importante tener puesta la version en el plugin "spring-boot-maven-plugin" para que generated-sources aparezca en azul al realizar "mvn compile" : 
```shell
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <version>2.6.6</version>
</plugin>
```

## 4 Creamos un producer en el controller para recibir por Rest a los empleados:

```java
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
```
## 5 Añadimos una propiedad más para el consumer:
```shell
# Property para que lea/reciba en el consumer como avro
spring.kafka.consumer.properties.specific.avro.reader=true
```

## 6 Añadimos un consumer:
```java
@Component
public class Consumer {
    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    @KafkaListener(topics = "springavrotopic")
    public void onEmployeeSave(ConsumerRecord<String, Employee> msg){
        Employee emp = msg.value();
        log.info("Employee recive for kafka:[{}]", emp);
    }
}
```