# Shared Payment Processing System

A Spring Boot-based API that enables secure, real-time processing of payments by parents towards students (both shared and uniquely assigned). Built with atomic, transactional updates and integrated with Swagger for documentation.

---

## 🚀 Features

- **Secure REST API** protected by Spring Security (`ROLE_ADMIN`)
- **Atomic Multi-Entity Payment Processing** (Parent ↔ Student)
- **Dynamic Arithmetic Logic** (e.g., transaction fees)
- **In-Memory H2 Database** with initial seeded data
- **Swagger UI** for exploring endpoints
- **Audit Logging** via a `Payment` record

---

## 🔧 Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- Spring Data JPA
- H2 Database
- Swagger (SpringDoc OpenAPI 3)
- Lombok

---

## 📦 How to Build

1. **Clone the repository**
   ```bash
   git clone https://github.com/WisdomEssien/shared-payment-processing-system.git
   cd shared-payment-processing
   ```

2. **Build the application**
   ```bash
   mvn clean package
   ```

---

## ▶️ How to Run

### From command line:
```bash
java -jar target/shared-payment-processing-system-1.0.jar 
```

### Access:
- **API Endpoint**: `http://localhost:7788/api/v1/payments`
- **Swagger Docs**: `http://localhost:7788/swagger-ui/index.html`
- **H2 Console**: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:mydb`) `No password`

---

## ✅ How to Test the API

Use tools like **Postman** or **curl**:

### Request (POST `/api/v1/payments`)
```json
{
  "parentId": 1,
  "studentId": 1,
  "paymentAmount": 100.0
}
```

### With Basic Auth:
- **Username**: `admin`
- **Password**: `admin`

---

## 🔐 Security Design

- **Authentication**: In-memory user setup with Spring Security.
- **Authorization**: Only users with `ROLE_ADMIN` can access the `/api/v1/payments` endpoint.
- **Basic Auth** is used for simplicity.

---

## 🔄 Multi-Table Payment Logic

### Payment Routing:

1. Determine if the student is shared (`isStudentShared = parentIds.size() > 1`) or unique.
2. Apply **dynamic fee/discount** using:
   ```java
   adjustedAmount = paymentAmount * (1 + dynamicRate)
   ```

3. **Atomic Updates**:
    - If **shared**, both Parent A and B’s balances are updated equally.
    - If **unique**, only the initiating parent’s balance is updated.
    - The student’s balance is **always** updated.

4. **Transaction Safety**: All operations happen within a `@Transactional` block—ensuring rollback on failure.

---

## 🧼 Arithmetic Logic Explanation

The `adjustedAmount` includes a % fee over the base payment. It is configurable in the property file:

```java
adjustedAmount = amount * (1 + 0.05);
```

**Example:**
- Payment: `100.00`
- Adjusted: `100.00 * 1.05 = 105.00`

### Shared Payment:
- Parent A: +52.50
- Parent B: +52.50
- Student: +105.00

### Unique Payment:
- Parent A or B: +105.00
- Student: +105.00

---

## 🗃️ Initialized Data (on startup)

| ID | Parent Name | Type     |
|----|-------------|----------|
| 1  | Parent A    | Shared + Unique (Student 2) |
| 2  | Parent B    | Shared + Unique (Student 3) |

| ID | Student Name        | Type   |
|----|---------------------|--------|
| 1  | Student 1 (Shared)  | Shared |
| 2  | Student 2 (Unique A)| Unique |
| 3  | Student 3 (Unique B)| Unique |

---

## 📄 License

Hope this was helpful to get you started

[wisdom essien](https://github.com/WisdomEssien/shared-payment-processing-system.git) © 2025

