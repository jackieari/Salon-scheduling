# Luxe Salon — Appointment Booking System

A full-stack salon appointment booking web application built with Spring Boot. Customers can browse available time slots and book appointments online; salon owners manage availability and view bookings through a PIN-protected dashboard.

---

## Features

### Customer
- Browse available appointment slots by stylist, date, and service
- Book appointments with name, email, and phone
- View booking confirmation and sync to an external calendar
- Cancel existing appointments

### Provider (Admin)
- PIN-protected dashboard
- View all upcoming booked appointments
- Add and delete available time slots
- Cancel customer bookings

### System
- Health check endpoint with real-time booking metrics
- Mock calendar service for integration testing
- Structured logging and in-memory observability metrics

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0.1 |
| Templating | Thymeleaf |
| Database | SQLite (embedded) |
| Build | Maven |
| Frontend | HTML5 + CSS3 |

---

## Project Structure

```
src/main/java/edu/sjsu/cmpe172/starterdemo/
├── controller/
│   ├── HomeController.java               # Landing page
│   ├── SalonController.java              # Customer booking flow
│   ├── ProviderController.java           # Admin dashboard
│   ├── CalendarIntegrationController.java
│   ├── MockCalendarServiceController.java
│   └── HealthController.java
├── model/
│   ├── Appointment.java
│   ├── TimeSlot.java
│   ├── CalendarEventRequest.java
│   └── CalendarEventResponse.java
├── service/
│   ├── AppointmentService.java
│   └── CalendarServiceClient.java
├── repository/
│   ├── AppointmentRepository.java
│   └── SlotRepository.java
└── monitoring/
    └── BookingMetrics.java

src/main/resources/
├── templates/         # Thymeleaf HTML pages
├── static/style.css   # Stylesheet
├── schema.sql         # Database schema
├── data.sql           # Seed data
└── application.properties
```

---

## Getting Started

### Prerequisites

- Java 17 or higher
- No additional installations required — Maven wrapper and SQLite are bundled

### Run the Application

**macOS / Linux:**
```bash
./mvnw spring-boot:run
```

**Windows:**
```cmd
mvnw.cmd spring-boot:run
```

The application starts at **http://localhost:8081**

### Build a JAR

```bash
./mvnw clean package
java -jar target/starter-demo-0.0.1-SNAPSHOT.jar
```

---

## Configuration

All configuration lives in `src/main/resources/application.properties`:

```properties
server.port=8081
spring.datasource.url=jdbc:sqlite:salon.db
```

The database file (`salon.db`) is created automatically in the working directory on first run. Schema and seed data are applied via `schema.sql` and `data.sql`.

No environment variables are required.

---

## Default Credentials

| Role | Access |
|---|---|
| Provider PIN | `1234` |

---

## API Reference

### Customer Pages

| Method | Path | Description |
|---|---|---|
| GET | `/` | Home / landing page |
| GET | `/slots` | View available appointments |
| GET | `/book/{slotId}` | Booking form |
| POST | `/book` | Submit a booking |
| GET | `/confirmation/{id}` | Booking confirmation |
| POST | `/cancel/{id}` | Cancel appointment |

### Provider Pages

| Method | Path | Description |
|---|---|---|
| GET | `/provider/login` | Login form |
| POST | `/provider/login` | Submit PIN |
| GET | `/provider` | Admin dashboard |
| POST | `/provider/slots` | Add a new slot |
| POST | `/provider/slots/{slotId}/delete` | Delete a slot |
| POST | `/provider/cancel/{appointmentId}` | Cancel a booking |
| GET | `/provider/logout` | Log out |

### REST Endpoints

| Method | Path | Description |
|---|---|---|
| POST | `/api/calendar/sync/{appointmentId}` | Sync appointment to external calendar |
| GET | `/mock-calendar/availability/{stylistName}` | Get stylist availability (mock) |
| POST | `/mock-calendar/register` | Register event (mock) |
| GET | `/health` | Health check + booking metrics |

---

## Database Schema

```sql
CREATE TABLE time_slots (
    slot_id      INTEGER PRIMARY KEY AUTOINCREMENT,
    stylist_name TEXT NOT NULL,
    slot_date    TEXT NOT NULL,
    start_time   TEXT NOT NULL,
    service      TEXT NOT NULL,
    is_blocked   INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE appointments (
    appointment_id INTEGER PRIMARY KEY AUTOINCREMENT,
    customer_name  TEXT NOT NULL,
    customer_email TEXT NOT NULL,
    customer_phone TEXT,
    slot_id        INTEGER NOT NULL,
    stylist_name   TEXT NOT NULL,
    slot_date      TEXT NOT NULL,
    start_time     TEXT NOT NULL,
    service        TEXT NOT NULL
);
```

Sample slots for stylists **Maria Lopez**, **James Kim**, and **Sofia Reyes** are seeded automatically on startup.

---

## Running Tests

```bash
./mvnw test
```

---

## Notes

- SQLite limits the connection pool to 1 (`hikari.maximum-pool-size=1`). For a production deployment, swap the datasource for PostgreSQL or MySQL.
- The provider PIN is hardcoded in `ProviderController.java`. Replace with a proper credential store before any real-world use.
- Bookings are wrapped in `@Transactional` with a `synchronized` guard to prevent double-booking under concurrent requests.
