# CS324: Distributed Systems & Software Architecture Labs

A collection of Java-based laboratory exercises covering object-oriented design, enterprise application architecture, concurrency, distributed systems, messaging middleware, and RPC-based microservices.

---

## Table of Contents

- [Repository Structure](#repository-structure)
- [Lab Modules Overview](#lab-modules-overview)
- [Module Details](#module-details)
  - [Lab 1: OOP Polymorphism & Inheritance](#lab-1-oop-polymorphism--inheritance)
  - [Lab 2: Three-Tier Architecture & JDBC](#lab-2-three-tier-architecture--jdbc)
  - [Lab 3: Observer Design Pattern](#lab-3-observer-design-pattern)
  - [Lab 5: Distributed Client-Server Architecture](#lab-5-distributed-client-server-architecture)
  - [Lab 6: Concurrency, Deadlocks, & Multithreading](#lab-6-concurrency-deadlocks--multithreading)
  - [Lab 7: Microservices with gRPC & Protocol Buffers](#lab-7-microservices-with-grpc--protocol-buffers)
  - [Lab 8: Messaging with ZeroMQ](#lab-8-messaging-with-zeromq)
  - [Lab 10: gRPC Streaming](#lab-10-grpc-streaming)
- [Prerequisites](#prerequisites)
- [Build and Execution Instructions](#build-and-execution-instructions)

---

## Repository Structure

```text
CS324/
├── README.md
└── Labs/
    ├── Lab1/                  # OOP fundamentals and polymorphism
    │   ├── music/
    │   ├── run.bat
    │   └── README.md
    ├── Lab2/                  # Three-tier architecture + JDBC + Access DB
    │   ├── database/
    │   ├── src/
    │   ├── pom.xml
    │   └── README.md
    ├── Lab3/                  # Observer pattern demo
    │   ├── src/
    │   ├── build.xml
    │   └── README.md
    ├── Lab5/                  # Remote banking client/server
    │   ├── Bank/
    │   ├── Customer/
    │   ├── README.md
    │   └── Lab_5_Solutions.md
    ├── Lab6/                  # Concurrency, deadlock, and Swing gallery
    │   ├── Banking/
    │   ├── Deadlock/
    │   ├── Gallery/
    │   └── README.md
    ├── Lab7/                  # gRPC + protobuf service
    │   ├── src/
    │   ├── pom.xml
    │   └── README.md
    ├── Lab8/                  # ZeroMQ communication with Java
    │   ├── src/
    │   ├── pom.xml
    │   └── README.md
    └── Lab10/                 # gRPC streaming demo
        ├── src/
        ├── pom.xml
        └── README.md
```

---

## Lab Modules Overview

| Module | Core Topic | Architectural Pattern | Technologies |
| :--- | :--- | :--- | :--- |
| **Lab 1** | OOP Fundamentals | Polymorphism & Dynamic Dispatch | Java SE, Batch Scripting |
| **Lab 2** | Enterprise Data Layering | Three-Tier Architecture (Presentation, Business, DAL) | Java, Maven, JDBC, MS Access (`.accdb`) |
| **Lab 3** | Event-Driven Updates | Observer Design Pattern | Java, Apache Ant |
| **Lab 5** | Distributed Systems | Client-Server Architecture via Remote Interfaces | Java, Apache Ant, Remote Services |
| **Lab 6** | Multithreading | Concurrency, Locking, Deadlock Analysis | Java Threads, Swing, Synchronization |
| **Lab 7** | Modern RPC | Contract-First Microservice Communication | Java, Maven, gRPC, Protocol Buffers |
| **Lab 8** | Messaging Middleware | Message Passing & Decoupled Communication | Java, Maven, ZeroMQ (JeroMQ) |
| **Lab 10** | Streaming RPC | Asynchronous Streaming Communication | Java, Maven, gRPC, Proto3 |

---

## Module Details

### Lab 1: OOP Polymorphism & Inheritance
Explores abstraction, inheritance hierarchies, and dynamic dispatch using an orchestral music model.
- `Instrument.java`: abstract base class defining playback behavior.
- `Guitar.java` and `Flute.java`: concrete subclasses overriding methods.
- `Music.java` and `Note.java`: orchestration and note representation.

### Lab 2: Three-Tier Architecture & JDBC
Demonstrates separation of concerns in a database-backed Java system.
- Presentation layer: `presentation_layer/UI.java`, `Application_main.java`
- Business logic layer: `business_layer/BLcustomer.java`
- Data access layer: `data_access_layer/DAcustomer.java`, `Access_JDBC.java`
- Database: `database/new_test.accdb`

### Lab 3: Observer Design Pattern
Shows how the Observer pattern keeps dependent components synchronized with a subject state change.
- `mySubject.java` and `number.java`: subject and state holder
- `myObserver.java`: observer abstraction
- `BinNumber.java` and `HexNumber.java`: concrete observers

### Lab 5: Distributed Client-Server Architecture
A Java RMI-style banking application split across the server and client projects.
- `Bank/`: server implementation and API contracts
- `Customer/`: client application consuming the shared interface
- `api/`: common service contracts and models

### Lab 6: Concurrency, Deadlocks, & Multithreading
Focuses on synchronization, locking, and responsiveness in GUI-driven multithreaded applications.
- `Banking/`: concurrent account processing and interest transfer logic
- `Deadlock/`: circular wait simulation
- `Gallery/`: Swing-based image viewer with background loading behavior

### Lab 7: Microservices with gRPC & Protocol Buffers
Implements an RPC-based service using a proto contract and generated Java stubs.
- `helloword.proto`: service definition
- `HelloWorldServer.java`: service implementation
- `HelloWorldClient.java`: client request executor

### Lab 8: Messaging with ZeroMQ
Introduces lightweight distributed messaging using ZeroMQ for decoupled communication between clients and servers.
- `ZmqServer.java`: ZeroMQ server endpoint
- `ZmqClient.java`: message-producing client
- Built using Maven and Java packages under `com.example`

### Lab 10: gRPC Streaming
Implements streaming communication using gRPC with Java and Proto3.
- `number.proto`: streaming RPC contract
- `NumberServer.java`: server-side streaming implementation
- `NumberClient.java`: client-side consumer

---

## Prerequisites

- Java Development Kit (JDK): 8 or newer
- Build tools:
  - Maven 3.6+
  - Apache Ant
- Optional IDE support: NetBeans, IntelliJ IDEA, or VS Code with Java extensions
- Database support:
  - Microsoft Access driver / UCanAccess for Lab 2
- Messaging/RPC dependencies:
  - JeroMQ / ZeroMQ bindings for Lab 8
  - gRPC and protobuf support for Lab 7 and Lab 10

---

## Build and Execution Instructions

### 1. Maven Projects

#### Lab 2: Three-Tier Customer System
```bash
cd Labs/Lab2
mvn clean compile
mvn exec:java -Dexec.mainClass="presentation_layer.Application_main"
```

#### Lab 7: gRPC Service
```bash
cd Labs/Lab7
mvn clean compile

# Terminal 1: start the gRPC server
mvn exec:java -Dexec.mainClass="com.example.grpc.HelloWorldServer"

# Terminal 2: run the gRPC client
mvn exec:java -Dexec.mainClass="com.example.grpc.HelloWorldClient"
```

#### Lab 8: ZeroMQ Messaging
```bash
cd Labs/Lab8
mvn clean compile

# Terminal 1: start the server
mvn exec:java -Dexec.mainClass="com.example.ZmqServer"

# Terminal 2: run the client
mvn exec:java -Dexec.mainClass="com.example.ZmqClient"
```

#### Lab 10: gRPC Streaming
```bash
cd Labs/Lab10
mvn clean compile

# Terminal 1: start the streaming server
mvn exec:java -Dexec.mainClass="com.example.grpcstream.NumberServer"

# Terminal 2: run the streaming client
mvn exec:java -Dexec.mainClass="com.example.grpcstream.NumberClient"
```

---

### 2. Ant Projects (NetBeans / Command Line)

#### Lab 3: Observer Pattern
```bash
cd Labs/Lab3
ant compile
ant run
```

#### Lab 5: Distributed Banking
```bash
# Compile and run server
cd Labs/Lab5/Bank
ant compile
ant run

# In a separate terminal, compile and run client
cd Labs/Lab5/Customer
ant compile
ant run
```

#### Lab 6: Concurrency Submodules
```bash
# Deadlock simulation
cd Labs/Lab6/Deadlock
ant compile
ant run

# Swing gallery
cd Labs/Lab6/Gallery
ant compile
ant run
```

---

### 3. Scripted Execution

#### Lab 1: OOP Music
```bash
cd Labs/Lab1

# Windows
run.bat

# Or manually
javac music/*.java
java music.Music
```

---

This repository is intended as a practical collection of Java-based software architecture and distributed systems labs, emphasizing design patterns, synchronization, middleware, and service-oriented communication.
