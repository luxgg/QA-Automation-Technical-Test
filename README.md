# QA Automation Technical Test — Katalon

One Katalon Studio project covering (1) REST API producer and consumer and (2) Kafka consumer validation.

## Requirements
- Katalon Studio (Windows; project uses Groovy scripts)
- Python 3 installed and on PATH
- Docker Desktop running, Docker CLI on PATH
- Git (for publishing)

## 1. Start local REST API
Open PowerShell in this repository:

```powershell
python config/mock_api.py
```

Leave it running. This is an in-memory API on `http://127.0.0.1:8088`. `POST /messages` saves a message and `GET /messages/{id}` reads it back. This avoids using a public fake API that does not actually persist POSTs.

## 2. Start Kafka
Open a **second** PowerShell window:

```powershell
docker run -d --name kafka-test -p 9092:9092 apache/kafka:4.3.1
docker exec kafka-test /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --if-not-exists --topic qa-technical-test --partitions 1 --replication-factor 1
```

If `kafka-test` already exists and is stopped, use `docker start kafka-test` instead of `docker run`.
Wait until the broker is ready; check with `docker logs kafka-test`.

## 3. Open and run Katalon
1. Open Katalon Studio > File > Open Project, select `QA-Automation-Technical-Test.prj`.
2. Open `Test Suites/TS_All_Tests` and click Run.
3. Both test cases should pass if Python API, Kafka, and Docker are running.

### Test cases
- `TC_REST_Producer_Consumer`: Katalon POSTs unique JSON to local API (expects 201), then GETs it (expects 200) and validates `id` and `message`.
- `TC_Kafka_Consumer`: Katalon launches Kafka's official console consumer inside Docker and reads its messages; a separate console producer sends a unique JSON event; Katalon validates that its consumer received the event with matching ID, event type, and amount.

**Implementation note:** The Kafka test uses the official Kafka CLI consumer invoked by Katalon's Groovy `ProcessBuilder`, rather than a direct Java KafkaConsumer client. This still tests message consumption under Katalon control, but disclose this distinction if an assessor specifically requires the Kafka Java consumer library.

## 4. Upload to public GitHub
Create a **public** repository on github.com (do not initialize with a README). In this project directory:

```powershell
git init
git add .
git commit -m "Add Katalon REST API and Kafka consumer tests"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/QA-Automation-Technical-Test.git
git push -u origin main
```

Replace `YOUR_USERNAME` with your GitHub username. Submit your actual public repository URL, not this example.

## Notes
- Tested execution is **not claimed**; run locally and record real test results before submitting.
- The local REST API is intentionally in-memory; restarting it clears records.
- Kafka topic retains messages across runs; the test uses a unique UUID to find its own event.
- Docker CLI must be available to Katalon via PATH. If Katalon was already open when Docker was installed, restart Katalon.
