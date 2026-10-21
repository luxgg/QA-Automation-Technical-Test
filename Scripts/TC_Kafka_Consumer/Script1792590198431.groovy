import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerRecords
import java.util.Properties
import java.time.Duration
import java.util.Collections

Properties props = new Properties()

props.put("bootstrap.servers", "localhost:9092")
props.put("group.id", "katalon-qa-test")
props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
props.put("auto.offset.reset", "earliest")

KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)

try {
	consumer.subscribe(Collections.singletonList("qa-test-topic"))

	boolean found = false

	for (int i = 0; i < 10 && !found; i++) {
		ConsumerRecords<String, String> records =
			consumer.poll(Duration.ofSeconds(2))

		records.each { record ->
			println("Received: " + record.value())

			if (record.value().contains("Test Message 123")) {
				found = true
			}
		}
	}

	assert found : "Expected Kafka message not received"

	println("Kafka Consumer Testing PASSED")

} finally {
	consumer.close()
}