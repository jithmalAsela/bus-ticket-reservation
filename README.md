# Bus Ticket Reservation System

A simple bus reservation REST API built with **Java 17**, **Servlets**, and **Gson**.  
The system manages a bus with configurable seats, stops, and prices.

It provides endpoints to:
- Check seat availability
- Reserve tickets

All data is stored **in-memory**, and configuration is externalized in `config.properties`.

---

## Features

- **Check Availability**
  - Returns number of available seats and total price for a given route.

- **Reserve Tickets**
  - Books seats and returns a ticket number with assigned seat numbers.

- **Segment-Based Seat Management**
  - Handles overlapping journeys correctly.
  - Example:
    - A seat booked from `A → C` is **not available** for `A → B` or `B → C`
    - But **can be reused** for `C → D`

- **Thread-Safe**
  - Uses synchronized methods to handle concurrent requests.

- **Configurable**
  - Seats, stops, and prices defined in `config.properties`.

- **Standalone Client JAR**
  - Command-line client to interact with the API.

---

## Technologies

- Java 17  
- Maven 3.8+  
- Servlet API 4.0  
- Gson 2.10.1  
- JUnit 4.13.2 (Unit Testing)  
- Apache Tomcat 9/10 (Servlet Container)  

---

## Project Structure

bus-reservation/
├── pom.xml
├── src/
│ ├── main/
│ │ ├── java/com/lk/busreservation/
│ │ │ ├── config/BusConfig.java
│ │ │ ├── model/
│ │ │ │ ├── AvailabilityRequest.java
│ │ │ │ ├── AvailabilityResponse.java
│ │ │ │ ├── Reservation.java
│ │ │ │ ├── ReservationRequest.java
│ │ │ │ └── ReservationResponse.java
│ │ │ ├── service/BusService.java
│ │ │ └── servlet/
│ │ │ ├── CheckAvailabilityServlet.java
│ │ │ └── ReserveTicketsServlet.java
│ │ └── resources/config.properties
│ └── test/java/com/lk/busreservation/service/BusServiceTest.java
└── README.md




## Configuration (`config.properties`)

Place this file in:

src/main/resources/


### Example:

properties
totalSeats = 40
seatsPerRow = 4
stops = A, B, C, D

price.A-B = 50
price.A-C = 100
price.A-D = 150
price.B-C = 50
price.B-D = 100
price.C-D = 50
Explanation:
totalSeats → Total number of seats (must be divisible by seatsPerRow)

seatsPerRow → Seats per row (e.g., 4 → 1A, 1B, 1C, 1D)

stops → Ordered list of stops

price.X-Y → Ticket price from X to Y
(Reverse direction handled automatically)

---

Building the WAR
Prerequisites
Java 17

Maven

Steps
mvn clean package
Output:

target/bus-reservation.war

---

Deploying to Tomcat
Copy the WAR file into:

<TOMCAT_HOME>/webapps/
Start Tomcat:

Linux/macOS

bin/startup.sh
Windows

bin\startup.bat
Access the application:

http://localhost:8080/bus-reservation

---

REST API Endpoints
All endpoints:

Method: POST

Header: Content-Type: application/json

1 Check Availability
URL

/api/checkAvailability
Request

{
  "passengerCount": 2,
  "origin": "A",
  "destination": "D"
}
Success Response (200)

{
  "availableSeats": 40,
  "totalPrice": 300.0
}
Error Response (400)

{
  "error": "Invalid route: A → A"
}

2 Reserve Tickets
URL

/api/reserveTickets
Request

{
  "passengerCount": 2,
  "origin": "A",
  "destination": "D",
  "priceConfirmation": 300.0
}
Success Response (200)

{
  "ticketNumber": "TICKET-1",
  "assignedSeats": ["1A", "1B"],
  "origin": "A",
  "destination": "D",
  "totalPrice": 300.0
}
Error Response (400)

{
  "error": "Not enough seats available."
}

---

Running Tests
mvn test
