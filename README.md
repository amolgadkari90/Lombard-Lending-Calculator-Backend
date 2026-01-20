Lombard Lending Calculator – Backend
Spring Boot REST API

Overview
This repository contains the backend service for the Lombard Lending Calculator application, developed using Java and Spring Boot.
The service calculates the Loan-to-Value (LTV) percentage based on portfolio value and loan amount, determines loan status as per business rules, and maintains calculation history.
The application is built following real project practices such as clean layering, proper validation, and centralized exception handling.

Business Logic
LTV Formula
LTV (%) = (Loan Amount / Portfolio Value) × 100

Loan Status
APPROVED – Loan is within acceptable LTV range
REJECTED – Loan exceeds allowed limits

All financial calculations are handled using BigDecimal to avoid precision issues.
Architecture
Controller → Service → Repository
↓
Validation & Business Rules

Key points:
REST-based and stateless APIs
DTO-based request and response handling
Common API response structure
Centralized exception handling

Technology Stack:
Java 17
Spring Boot
Spring Web
Spring Validation
Spring Data JPA
Maven
Lombok
PostgreSQL

API Endpoints:
Calculate Loan LTV
POST /api/loans/calculate

Request Body
{
"portfolioValue": 15243.66,
"loanAmount": 1000.00
}

Success Response
{
"success": true,
"data": {
"ltv": 6.56,
"status": "APPROVED",
"maxLoanAmount": 7621.83,
"marginCallThreshold": 9146.20,
"createdAt": "2026-01-20T15:12:47.5164767Z"
},
"message": "LTV is calculated and saved successfully.",
"path": "/api/loans/calculate",
"statusCode": 200,
"timeStamp": "2026-01-20T20:42:47.6467701+05:30"
}

Health Check
GET /api/loans/health

Response
Loan service is UP

Loan Calculation History
GET /api/loans/history

Response
{
"success": true,
"data": [
{
"ltv": 6.56,
"status": "APPROVED",
"maxLoanAmount": 7621.83,
"marginCallThreshold": 9146.20,
"createdAt": "2026-01-20T15:12:47.516477Z"
},
{
"ltv": 1312.02,
"status": "REJECTED",
"maxLoanAmount": 7621.83,
"marginCallThreshold": 9146.20,
"createdAt": "2026-01-20T08:06:08.452691Z"
}
]
}

Error Handling:
All errors are handled using centralized exception handling to provide consistent responses.

Malformed Request Example
{
"success": false,
"status": 400,
"errorCode": "INVALID_REQUEST_BODY",
"errorMessage": "Malformed JSON request",
"path": "/api/loans/calculate",
"timestamp": "2026-01-20T20:44:13"
}

Business Validation Error Example
{
"success": false,
"status": 400,
"errorCode": "INVALID_INPUT_EXCEPTION",
"errorMessage": "Loan amount must be less than Portfolio value",
"path": "/api/loans/calculate",
"timestamp": "2026-01-20T20:44:32"
}

Configuration
Server Port: 8081

Database configuration can be updated in application.yaml.
How to Run

Build
mvn clean install

Run
mvn spring-boot:run

Application will be available at
http://localhost:8081

Assumptions:
Single currency system
Authentication is not implemented
Portfolio value is assumed to be validated by upstream systems
Future Enhancements
Authentication and authorization

Audit logging:
Pagination for history API


Author
Amol Gadkari
Java Backend / Full Stack Developer
