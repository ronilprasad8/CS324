# CS324: Distributed Computing — Week 11 Lab
# Java gRPC Bidirectional Streaming

This project is a complete implementation of the **Java gRPC Bidirectional Streaming Lab** for CS324. It demonstrates high-throughput, asynchronous, bidirectional streaming between a client and a server using Google Protocol Buffers (Protobuf) and gRPC.

---

## 1. Objectives & Lab Requirements

- **Define a streaming RPC in Protocol Buffers**: Declare a bidirectional streaming method in `number.proto`.
- **Generate Java gRPC Classes**: Use `protobuf-maven-plugin` and `protoc-gen-grpc-java` to compile proto messages and service stubs.
- **Implement `StreamObserver`**:
  - **Server**: Handle incoming requests asynchronously via `onNext()`, classify each integer as `ODD` or `EVEN`, immediately stream the response back, and summarize received statistics upon completion (`onCompleted()`).
  - **Client**: Generate **10,000 random integers every second for 10 seconds** (totaling **100,000 numbers**), stream each number immediately to the server, and asynchronously consume responses in real-time without blocking transmission.
- **Execution**: Run the server and client concurrently via NetBeans IDE or standalone Maven CLI.

---

## 2. Background: gRPC Streaming Patterns

gRPC supports four distinct communication models:

| RPC Type | Description | Protobuf Method Signature |
| :--- | :--- | :--- |
| **Unary RPC** | Traditional request-response (like standard HTTP or REST). | `rpc SayHello(HelloRequest) returns (HelloResponse);` |
| **Server Streaming** | Client sends one request; server responds with a stream of messages. | `rpc LotsOfReplies(HelloRequest) returns (stream HelloResponse);` |
| **Client Streaming** | Client writes a sequence of messages; server responds once when finished. | `rpc LotsOfGreetings(stream HelloRequest) returns (HelloResponse);` |
| **Bidirectional Streaming** *(Implemented)* | Both sides read and write sequences of messages independently in any order. | `rpc CheckNumbers(stream NumberRequest) returns (stream NumberResponse);` |

---

## 3. Architecture & Data Flow

```
+---------------------------------------------------------------------------------------+
|                                    NumberClient                                       |
|                                                                                       |
|   Loop (10 seconds):                                                                  |
|     10,000 random integers/sec generated                                             |
|                                                                                       |
|       requestObserver.onNext(NumberRequest{number})                                   |
|                │                                                                      |
|                │  gRPC Bidirectional Stream (HTTP/2 - Port 50051)                     |
|                │                                                                      |
|                ▼                                                                      |
|   +-------------------------------------------------------------------------------+   |
|   |                                NumberServer                                   |   |
|   |                                                                               |   |
|   |   onNext(NumberRequest request):                                              |   |
|   |     - Increment totalCount                                                    |   |
|   |     - Determine if number % 2 == 0  --> "EVEN" (evenCount++)                  |   |
|   |                                     --> "ODD"  (oddCount++)                   |   |
|   |     - Stream back immediately:                                                |   |
|   |         responseObserver.onNext(NumberResponse{number, result})               |   |
|   +-------------------------------------------------------------------------------+   |
|                │                                                                      |
|                │  Streaming Responses                                                 |
|                ▼                                                                      |
|   responseObserver.onNext(NumberResponse response):                                   |
|     - Increment responsesReceived                                                     |
|     - Log every 1,000th response (e.g., "Response 1000: 828064 -> EVEN")             |
|                                                                                       |
|   Completion:                                                                         |
|     - Client calls requestObserver.onCompleted()                                      |
|     - Server prints summary (total, odds, evens) & calls responseObserver.onCompleted()|
|     - Client receives onCompleted() & displays execution time and summary             |
+---------------------------------------------------------------------------------------+
```

---

## 4. Project Directory Structure

```
GrpcNumberStreaming/
├── pom.xml                                    # Maven configuration & gRPC dependencies
├── README.md                                  # Project documentation (this file)
└── src/
    └── main/
        ├── proto/
        │   └── number.proto                   # Service & message definitions
        └── java/
            └── com/
                └── example/
                    └── grpcstream/
                        ├── NumberServer.java  # gRPC Server implementation
                        └── NumberClient.java  # gRPC Client implementation
```

---

## 5. Technology Stack & Dependencies

- **Java Development Kit (JDK)**: Java 17+ (Tested and verified with JDK 24)
- **Build System**: Apache Maven 3.9+
- **gRPC Version**: `1.84.0`
  - `grpc-netty-shaded` (relocated Netty transport layer)
  - `grpc-protobuf` (protobuf serialization and integration)
  - `grpc-stub` (client stubs and `StreamObserver` abstractions)
- **Protocol Buffers**: `3.25.8`
- **Maven Plugins**:
  - `kr.motd.maven:os-maven-plugin:1.7.1` (detects OS classifier for platform-specific protoc binary)
  - `org.xolstice.maven.plugins:protobuf-maven-plugin:0.6.1` (invokes `protoc` and `protoc-gen-grpc-java`)

---

## 6. How to Build & Run

### Method A: NetBeans IDE (As specified in the lab)

1. Open **Apache NetBeans**.
2. Select **File > Open Project...** and choose this `GrpcNumberStreaming` directory.
3. Right-click the `GrpcNumberStreaming` project node and choose **Clean and Build**.
   - Maven compiles `number.proto` and generates `NumberRequest`, `NumberResponse`, and `NumberServiceGrpc`.
4. **Start the Server**:
   - Under **Source Packages**, expand `com.example.grpcstream`.
   - Open `NumberServer.java`.
   - Right-click inside the editor and select **Run File** (or press `Shift + F6`).
   - Leave the server running. You will see:
     ```text
     Number Server started on port 50051...
     ```
5. **Start the Client**:
   - Open `NumberClient.java`.
   - Right-click inside the editor and select **Run File** (or press `Shift + F6`).
   - Watch the Output window for 10 seconds of streaming activity.

---

### Method B: Command Line (PowerShell / Terminal)

Open a terminal in this directory:

```powershell
# 1. Clean and compile
mvn clean compile

# 2. Run the server (Terminal 1)
mvn exec:java "-Dexec.mainClass=com.example.grpcstream.NumberServer"

# 3. Run the client (Terminal 2)
mvn exec:java "-Dexec.mainClass=com.example.grpcstream.NumberClient"
```

---

## 7. Expected Output

### Server Output
```text
Number Server started on port 50051...
Client finished sending numbers.
Total numbers received: 100000
Odd numbers: 49964
Even numbers: 50036
```

### Client Output
```text
Second 1 completed.
Second 2 completed.
Second 3 completed.
Response 1000: 828064 -> EVEN
Second 4 completed.
Response 2000: 697709 -> ODD
...
Response 100000: 888227 -> ODD
Server completed the response stream.

----- CLIENT SUMMARY -----
Total numbers sent: 100000
Total responses received: 100000
Execution time: 24.xx seconds
```

---

## 8. Implementation Details

- **Protocol Buffer Definition (`number.proto`)**:
  ```protobuf
  syntax = "proto3";

  option java_package = "com.example.grpcstream";
  option java_multiple_files = true;

  service NumberService {
    rpc CheckNumbers(stream NumberRequest) returns (stream NumberResponse);
  }

  message NumberRequest {
    int32 number = 1;
  }

  message NumberResponse {
    int32 number = 1;
    string result = 2;
  }
  ```

- **Thread Safety**:
  - `AtomicInteger` counters (`totalCount`, `oddCount`, `evenCount`, and `responsesReceived`) ensure safe atomic updates under multi-threaded streaming execution.
- **Asynchronous Flow**:
  - A `CountDownLatch(1)` in `NumberClient` awaits server stream completion before shutting down the `ManagedChannel`.
  - Rate pacing (`Thread.sleep()`) regulates transmission to 10,000 numbers per second over 10 cycles.

---

## 9. Troubleshooting & Tips

- **Windows / OneDrive File Locking**:
  If building in a folder synchronized with Microsoft OneDrive or protected by real-time antivirus, you may occasionally see `Unable to clean up temporary proto file directory`.
  - Simply run `mvn clean compile` again, or
  - Configure the temporary extraction directory outside synced folders by adding `<temporaryProtoFileDirectory>${java.io.tmpdir}/protoc-temp</temporaryProtoFileDirectory>` inside the `<configuration>` block of `protobuf-maven-plugin` in `pom.xml`.
- **Port Conflict (Port 50051 in use)**:
  If a previous server instance is still running in the background, terminate it or stop the process listening on port 50051 before restarting.
