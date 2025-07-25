# 🌀 Spring Batch REST Job

This Spring Boot + Spring Batch project reads customer data from a database in chunks and sends it to a REST API. It includes:

- ✅ Scheduled job execution
- ✅ Chunk-based processing
- ✅ Retry on failure (`RetryTemplate`)
- ✅ Fallback to file + DB logging
- ✅ Email notifications for failed items
- ✅ Job auditing with `StepExecutionListener`

---

## 📦 Features

| Feature               | Description                                                                 |
|----------------------|-----------------------------------------------------------------------------|
| Scheduled Job         | Runs every 60 seconds (`@Scheduled`)                                        |
| Chunk Processing      | Reads 10 records per chunk and writes to REST API                          |
| Retry Logic           | Retries 3 times with 2s delay before fallback                               |
| Failure Handling      | Logs to a file, DB table, and sends email on permanent failure             |
| Audit Logging         | Logs read/write/skip counts before and after each step                     |

---

| Feature                   | Description                             |
| ------------------------- | --------------------------------------- |
| ✅ SQL file-based query    | Multi-join complex SQL from `.sql` file |
| ✅ Custom `ItemReader`     | Reads and maps SQL result rows to DTOs  |
| ✅ Chunked REST submission | Sends each chunk to a remote API        |
| ✅ Retry mechanism         | Uses `RetryTemplate` for API failures   |
| ✅ Exception handling      | Logs, retries, and stores failed items  |
| ✅ Step listener auditing  | Step-level logging and error metrics    |
| ✅ Dockerfile              | For containerized app build & run       |
| ✅ Test data               | H2 or PostgreSQL with dummy SQL data    |
| ✅ Postman Collection      | To test API endpoints easily            |
| ✅ README.md               | Setup, usage, and architecture overview |


## 🚀 How to Run

bash

mvn spring-boot:run

Or using your IDE (IntelliJ/VSCode), run BatchRestApplication.java.

### 1. Clone & Build

```bash
git clone https://github.com/kannanbe-code/spring-batch.git
cd spring-batch
mvn clean install

Retry Logic:

If the REST call fails:

Retries 3 times (2 seconds apart)

If still fails: logs to failed-records.log and failed_customers table

Sends an email to the admin

REST API Target:

This batch job expects an API endpoint at:

bash
POST http://localhost:8081/api/customers
Make sure your REST server is running and accessible.

Email Configuration:

Update application.yml with real SMTP settings if needed:

spring:
  mail:
    host: smtp.example.com
    port: 587
    username: user@example.com
    password: password
    properties:
      mail.smtp.auth: true
      mail.smtp.starttls.enable: true

H2 Console:

Access at: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:batchdb

Technologies:

Spring Boot 3.x

Spring Batch

Spring Data JPA

Spring Retry

WebClient (Reactive)

H2 Database

Java 17

Output Logs:

src/main/resources/failed-records.log — Log of failed REST submissions

failed_customers DB table — Detailed errors

✅ Summary of All Test Types Added

| Test File                       | Purpose                             |
| ------------------------------- | ----------------------------------- |
| `CustomerRepositoryTest`        | JPA insert/read test                |
| `RestCustomerWriterTest`        | Retry + WebClient mocking           |
| `EmailServiceTest`              | Validates email sending             |
| `JobCompletionNotificationTest` | Auditing listener behavior          |
| `SpringBatchIntegrationTest`    | Full job execution & data flow test |


All tests use Spring Boot Test and mock WebClient/Email where needed. You can run them via:

bash

./mvnw test

✅ Step 2: Run Tests with Coverage
From the root of your project, run:
./mvnw clean verify

This will:

Run all your unit & integration tests

Generate a code coverage report at:
target/site/jacoco/index.html
Open index.html in your browser to view line-by-line coverage of each class and method.
