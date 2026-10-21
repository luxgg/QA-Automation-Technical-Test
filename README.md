# QA Automation Technical Test — Katalon

This project contains automated REST API and Kafka consumer testing using Katalon Studio.

## Tools and Technologies

- Katalon Studio
- Groovy
- REST API (JSONPlaceholder)
- Apache Kafka
- Java KafkaConsumer
- GitHub

## Test Scenarios

### 1. REST API Producer and Consumer

Test Case: `TC_REST_Producer_Consumer`

Endpoint: `https://jsonplaceholder.typicode.com/posts`

**Test Steps:**
1. Send a POST request with JSON data.
2. Verify the POST response status code is 201 (Created).
3. Send a GET request to `/posts/1`.
4. Verify the GET response status code is 200 (OK).

**Expected Result:**
- POST request returns HTTP 201.
- GET request returns HTTP 200.
- Test case passes when both status code validations succeed.

**Note:** JSONPlaceholder is a mock REST API. POST requests do not permanently save data, so the GET request validates an existing resource rather than retrieving the newly created POST data.

### 2. Kafka Consumer Testing

Test Case: `TC_Kafka_Consumer`

**Kafka Configuration:**
- Bootstrap Server: `localhost:9092`
- Topic: `qa-test-topic`
- Consumer Group: `katalon-qa-test`
- Auto Offset Reset: `earliest`

**Test Steps:**
1. Start the local Apache Kafka broker.
2. Create the Kafka topic `qa-test-topic`.
3. Send a message containing `Test Message 123` using Kafka Console Producer.
4. Run `TC_Kafka_Consumer` in Katalon Studio.
5. The KafkaConsumer subscribes to the topic and polls for messages.
6. Validate that a received message contains `Test Message 123`.

**Expected Result:**
- Kafka consumer successfully receives the expected message.
- The assertion passes when the expected message is found.
- The consumer connection is closed after execution.

## How to Run

### REST API Testing
1. Open the project in Katalon Studio.
2. Open `Test Cases/TC_REST_Producer_Consumer`.
3. Click Run.
4. Check the execution result in Log Viewer.

### Kafka Testing

1. Start Apache Kafka locally on port 9092.
2. Open Command Prompt in the Kafka installation directory.
3. Create the topic:

```cmd
bin\windows\kafka-topics.bat --create --if-not-exists --topic qa-test-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

4. Start Kafka Console Producer:

```cmd
bin\windows\kafka-console-producer.bat --topic qa-test-topic --bootstrap-server localhost:9092
```

5. Enter the following message and press Enter:

```text
Test Message 123
```

6. Open `Test Cases/TC_Kafka_Consumer` in Katalon Studio.
7. Click Run and check the execution result.

**Dependency:** The Kafka Java client libraries must be available to Katalon Studio.

## Test Execution Results

Both test cases were executed successfully in Katalon Studio.

- `TC_REST_Producer_Consumer`: PASSED
- `TC_Kafka_Consumer`: PASSED

## Repository Structure

```text
QA-Automation-Technical-Test/
├── Scripts/
│   ├── TC_REST_Producer_Consumer/
│   └── TC_Kafka_Consumer/
├── Test Cases/
├── Profiles/
├── config/
├── QA-Automation-Technical-Test.prj
└── README.md
```

## Conclusion

This project demonstrates REST API request automation and Kafka message consumption validation using Katalon Studio and Groovy.
