import groovy.json.JsonSlurper
import java.util.concurrent.TimeUnit

// Katalon launches and reads Kafka's official console consumer via Docker.
// Kafka is the broker; Katalon acts as the test consumer by controlling the
// consumer process and validating its output. No additional JARs needed.
String topic = 'qa-technical-test'
String id = UUID.randomUUID().toString()
String payload = groovy.json.JsonOutput.toJson([id:id, event:'ORDER_CREATED', amount:1250])

// Start a consumer first so it is subscribed before publishing.
def consumerCmd = ['docker', 'exec', 'kafka-test', '/opt/kafka/bin/kafka-console-consumer.sh',
    '--bootstrap-server', 'localhost:9092', '--topic', topic, '--from-beginning', '--timeout-ms', '20000']
Process consumer = new ProcessBuilder(consumerCmd).redirectErrorStream(true).start()
Thread.sleep(3000)

// A separate producer sends a message. The Katalon-controlled consumer must receive it.
def producerCmd = ['docker', 'exec', '-i', 'kafka-test', '/opt/kafka/bin/kafka-console-producer.sh',
    '--bootstrap-server', 'localhost:9092', '--topic', topic]
Process producer = new ProcessBuilder(producerCmd).redirectErrorStream(true).start()
producer.outputStream.withWriter('UTF-8') { it.write(payload + '\n') }
assert producer.waitFor(15, TimeUnit.SECONDS) : 'Kafka producer timed out'
assert producer.exitValue() == 0 : 'Kafka producer failed: ' + producer.inputStream.text

assert consumer.waitFor(30, TimeUnit.SECONDS) : 'Kafka consumer timed out'
String output = consumer.inputStream.getText('UTF-8')
boolean found = output.readLines().any { line ->
    try {
        def item = new JsonSlurper().parseText(line)
        return item instanceof Map && item.id == id && item.event == 'ORDER_CREATED' && item.amount == 1250
    } catch (Exception ignored) { return false }
}
assert found : 'Expected Kafka message not received. Consumer output: ' + output
println 'PASS: Kafka consumer received and validated message ' + id
