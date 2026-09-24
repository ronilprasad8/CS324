# ZeroMQ Hello World (Java & Maven)

A simple Request-Reply (REQ-REP) client/server demonstration using **ZeroMQ** in Java with **JeroMQ** (a pure Java implementation of ZeroMQ) and **Apache Maven**.

---

## Table of Contents

- [Overview](#overview)
- [Architecture & Messaging Pattern](#architecture--messaging-pattern)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Building the Project](#building-the-project)
- [Running the Application](#running-the-application)
  - [Option 1: Using Maven Exec Plugin (CLI)](#option-1-using-maven-exec-plugin-cli)
  - [Option 2: Running via IDE (NetBeans / VS Code / IntelliJ)](#option-2-running-via-ide-netbeans--vs-code--intellij)
  - [Option 3: Running with Java CLI](#option-3-running-with-java-cli)
- [Expected Output](#expected-output)
- [Key Components Explained](#key-components-explained)
- [Troubleshooting](#troubleshooting)

---

## Overview

This project demonstrates a classic client-server messaging workflow using ZeroMQ over TCP. Unlike traditional socket programming, ZeroMQ abstracts low-level socket management, framing, and reconnections while delivering high throughput and low latency.

Because this project uses **JeroMQ** (`org.zeromq:jeromq:0.6.0`), no native ZeroMQ C++ libraries or OS-specific binaries (`libzmq.so` or `libzmq.dll`) are required.

---

## Architecture & Messaging Pattern

The application implements the **Request-Reply pattern**:

```
+------------------+                    +------------------+
|    ZmqClient     |  "Hello, Server!"  |    ZmqServer     |
|   (Socket: REQ)  | -----------------> |   (Socket: REP)  |
|                  |                    |  tcp://*:5555    |
|                  |  "Hello, Client!"  |                  |
|                  | <----------------- |                  |
+------------------+                    +------------------+
```

1. **Server (`ZmqServer`)**:
   - Binds to `tcp://*:5555` using a `REP` (Reply) socket.
   - Waits in an infinite loop for incoming requests.
   - For each request received, processes it and sends back a response (`"Hello, Client!"`).

2. **Client (`ZmqClient`)**:
   - Connects to `tcp://localhost:5555` using a `REQ` (Request) socket.
   - Sends a request string (`"Hello, Server!"`).
   - Blocks waiting for the server's reply, prints the reply, and cleanly closes the connection.

---

## Project Structure

```text
Lab Resources/
├── pom.xml                                      # Maven build file with JeroMQ & Exec plugins
├── README.md                                    # Project documentation
├── ZeroMQ using Java with Maven in NetBeans Lab.pdf # Lab guide reference
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── example/
│                   ├── ZmqServer.java           # Server implementation (REP socket)
│                   └── ZmqClient.java           # Client implementation (REQ socket)
└── target/                                      # Compiled class files and output jar
```

---

## Prerequisites

- **Java Development Kit (JDK)**: Version 17 or later
  ```bash
  java -version
  ```
- **Apache Maven**: Version 3.6 or later
  ```bash
  mvn -version
  ```
- *(Optional)* An IDE such as **Apache NetBeans**, **VS Code** (with Extension Pack for Java), or **IntelliJ IDEA**.

---

## Building the Project

Open a terminal in the root directory of the project and compile the source code:

```bash
mvn clean compile
```

To package into a JAR:

```bash
mvn clean package
```

---

## Running the Application

> **Note:** Always start the **Server** first so that it is bound and listening before the **Client** connects.

### Option 1: Using Maven Exec Plugin (CLI) - Recommended

Open two separate terminal windows in the project root:

#### Terminal 1: Start Server
```bash
mvn exec:java@server
```
*Or using the property directly (note quotes for PowerShell compatibility):*
- **PowerShell:**
  ```powershell
  mvn exec:java "-Dexec.mainClass=com.example.ZmqServer"
  ```
- **Bash / Command Prompt:**
  ```bash
  mvn exec:java -Dexec.mainClass="com.example.ZmqServer"
  ```

#### Terminal 2: Run Client
```bash
mvn exec:java@client
```
*Or using the property directly:*
- **PowerShell:**
  ```powershell
  mvn exec:java "-Dexec.mainClass=com.example.ZmqClient"
  ```
- **Bash / Command Prompt:**
  ```bash
  mvn exec:java -Dexec.mainClass="com.example.ZmqClient"
  ```

---

### Option 2: Running via IDE (NetBeans / VS Code / IntelliJ)

1. **VS Code**:
   - Open `ZmqServer.java` and click the **Run** button (or CodeLens above `main`).
   - Open `ZmqClient.java` and click the **Run** button.

2. **NetBeans**:
   - Open the project (`File` -> `Open Project...`).
   - Right-click `ZmqServer.java` -> **Run File** (Shift + F6).
   - Right-click `ZmqClient.java` -> **Run File** (Shift + F6).

3. **IntelliJ IDEA**:
   - Right-click `ZmqServer.java` -> **Run 'ZmqServer.main()'**.
   - Right-click `ZmqClient.java` -> **Run 'ZmqClient.main()'**.

---

### Option 3: Running with Java CLI

First, compile and copy the project dependencies:

```bash
mvn compile dependency:copy-dependencies
```

Then run in two terminals:

```bash
# Terminal 1 - Server (Windows PowerShell / CMD)
java -cp "target/classes;target/dependency/*" com.example.ZmqServer
```
*(On Linux/macOS, use `:` as the classpath separator instead of `;`: `java -cp "target/classes:target/dependency/*" com.example.ZmqServer`)*

```bash
# Terminal 2 - Client (Windows PowerShell / CMD)
java -cp "target/classes;target/dependency/*" com.example.ZmqClient
```

---

## Expected Output

### Server Terminal
```text
ZeroMQ Server started, waiting for requests...
Received request: Hello, Server!
Sent reply: Hello, Client!
```

### Client Terminal
```text
ZeroMQ Client started, sending request...
Sent request: Hello, Server!
Received reply: Hello, Client!
```

---

## Key Components Explained

| Class / Component | Description |
| :--- | :--- |
| `ZMQ.Context` | Manages background I/O threads and memory for sockets. Created using `ZMQ.context(1)`. |
| `ZMQ.REP` | Reply socket used by the server to receive requests and send responses in lockstep. |
| `ZMQ.REQ` | Request socket used by the client to send requests and await responses in lockstep. |
| `responder.bind(...)` | Binds server to protocol and port (`tcp://*:5555`). |
| `requester.connect(...)` | Connects client to remote endpoint (`tcp://localhost:5555`). |
| `jeromq (0.6.0)` | Pure Java 0MQ implementation defined in `pom.xml`. No native C++ libraries needed. |

---

## Troubleshooting

- **`Address already in use` (BindException)**:
  - Another process or an existing instance of `ZmqServer` is already listening on port `5555`.
  - Terminate any running server instances or free port `5555` before restarting.
- **Client hangs with no response**:
  - Verify that the server is actively running in another window.
  - Check firewall settings if testing across different network interfaces.
- **Strict REQ-REP Order**:
  - In ZeroMQ REQ-REP pattern, request and reply must strictly alternate: a `REQ` socket must `send()` then `recv()`, while a `REP` socket must `recv()` then `send()`.
