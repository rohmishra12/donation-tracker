# Donation Tracker

A robust donation tracking system built with Spring Boot and Apache Kafka, designed to handle real-time donation events with high throughput and reliability.

## 🚀 Features

- Real-time donation event processing using Apache Kafka
- Circuit breaker implementation for fault tolerance
- Automatic retry mechanisms for failed operations
- High-performance event processing (2,000+ events/sec)
- RESTful API for donation management
- Real-time analytics and monitoring
- Scalable microservices architecture
- Comprehensive error handling and logging
- Docker support for containerized deployment
- Prometheus metrics integration
- Health checks and monitoring endpoints

## 🛠️ Technical Stack

- 🌟 Backend Framework: Spring Boot 3.2.3
- 🐘 Database: H2 Database (development) / PostgreSQL (production)
- 📡 Message Broker: Apache Kafka
- ⚡️ Performance: Resilience4j for circuit breaking
- 📦 Build Tool: Maven 3.6+
- 🌐 API: Spring Web
- 📊 Monitoring: Prometheus metrics
- 📝 Code Generation: Lombok
- 🔄 Event Processing: Spring Kafka
- 📚 ORM: Spring Data JPA

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- Java 17 or higher
- Maven 3.6 or higher
- Apache Kafka 3.x
- Docker (for containerized deployment)
- Git

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/donation-tracker.git
cd donation-tracker
```

### 2. Start Kafka (Using Docker)
```bash
docker-compose up -d
```

### 3. Build the Application
```bash
mvn clean install
```

### 4. Run the Application
```bash
mvn spring-boot:run
```
The application will start on port 8080.

## 📚 API Documentation

### Donation Management Endpoints

#### Create a New Donation
```http
POST /api/v1/donations
Content-Type: application/json

{
    "donorId": "string",
    "campaignId": "string",
    "amount": 100.00,
    "currency": "USD",
    "donationDate": "2024-01-01T12:00:00Z"
}
#### Get Donations by Campaign 
http
GET /api/v1/donations/campaign/{campaignId}
#### Get Donations by Donor
http
GET /api/v1/donations/donor/{donorId}
#### Get Donation by ID
http
GET /api/v1/donations/{id}
#### Update Donation Status
http
PATCH /api/v1/donations/{id}/status
Content-Type: application/json

{
    "status": "COMPLETED"
}
#### 🔧 Performance Optimization
The application is optimized for high performance through:

Kafka batch processing with optimized consumer settings
Circuit breaker implementation with fallback mechanisms
Async processing using CompletableFuture
Connection pooling and thread management
Batch database operations
Efficient JSON serialization/deserialization
Caching strategies for frequently accessed data
#### 📊 Monitoring & Metrics
The application exposes several monitoring endpoints:

/actuator/health - Application health check
/actuator/metrics - Application metrics
/actuator/prometheus - Prometheus metrics
/actuator/circuitbreakers - Circuit breaker status
/actuator/loggers - Logging configuration
/actuator/info - Application info
#### 🔒 Security
The application implements:

Role-based access control (RBAC)
JWT authentication
Input validation
SQL injection prevention
XSS protection
Rate limiting
CORS configuration
#### 🔄 Circuit Breaker Configuration
The application uses Resilience4j for circuit breaking with the following settings:

Sliding window size: 100
Minimum number of calls: 10
Failure rate threshold: 50%
Wait duration in open state: 5 seconds
Automatic transition from open to half-open state
Custom fallback mechanisms
#### 🤝 Contributing
Fork the repository
Create your feature branch (git checkout -b feature/AmazingFeature)
Commit your changes (git commit -m 'Add some AmazingFeature')
Push to the branch (git push origin feature/AmazingFeature)
Open a Pull Request
