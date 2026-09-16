# CS324 Week 8 Lab: gRPC Java Example

A simple demonstration of building a client-server remote procedure call (RPC) application using **gRPC** and **Protocol Buffers (protobuf)** in Java.

---

## 📌 Overview

This project implements a basic `Greeter` service:
- **Server (`HelloWorldServer`)**: Listens on port `50051`, accepts RPC calls with a name parameter, and returns a greeting message.
- **Client (`HelloWorldClient`)**: Establishes a plaintext gRPC channel to the server on `localhost:50051`, sends a `HelloRequest`, and prints the `HelloReply` response.
- **Proto Contract (`helloword.proto`)**: Defines the service interface and message schemas compiled into Java code via `protobuf-maven-plugin`.

---

## 🛠️ Prerequisites

- **Java JDK**: Version 21 (or compatible)
- **Apache Maven**: Version 3.6+
- **Git** (optional)

---

## 📂 Project Structure

```text
.
├── pom.xml                                    # Maven configuration & gRPC/Protobuf dependencies
└── src
    └── main
        ├── java
        │   └── com
        │       └── example
        │           └── grpc
        │               ├── HelloWorldClient.java  # gRPC Client implementation
        │               └── HelloWorldServer.java  # gRPC Server implementation
        └── proto
            └── helloword.proto                # Protocol Buffer service and message definition
```

---

## 📜 Protocol Buffer Definition (`helloword.proto`)

```protobuf
syntax = "proto3"; 
 
option java_package = "com.example.grpc"; 
option java_outer_classname = "HelloWorldProto"; 

service Greeter { 
    rpc SayHello (HelloRequest) returns (HelloReply); 
} 

message HelloRequest { 
    string name = 1; 
} 

message HelloReply { 
    string message = 1; 
}
```

---

## 🚀 Getting Started

### 1. Compile and Generate gRPC Stubs

Run the following command in the project root directory to compile the `.proto` file and build the project:

```bash
mvn clean compile
```

This compiles the Protobuf definition and generates the Java classes (`GreeterGrpc`, `HelloWorldProto`, etc.) in `target/generated-sources/protobuf/`.

---

### 2. Run the Server

Start the gRPC server so that it can listen for incoming client requests:

Using Maven:
```bash
mvn exec:java -Dexec.mainClass="com.example.grpc.HelloWorldServer"
```

*Expected output:*
```text
Server started, listening on 50051
```

---

### 3. Run the Client

In a separate terminal window/tab, run the client to send an RPC request to the server:

Using Maven:
```bash
mvn exec:java -Dexec.mainClass="com.example.grpc.HelloWorldClient"
```

*Expected output:*
```text
Greeting: Hello World
```

---

## ⚙️ Key Dependencies & Plugins

From [`pom.xml`](pom.xml):

| Component | Artifact | Version | Description |
| :--- | :--- | :--- | :--- |
| **gRPC Netty** | `io.grpc:grpc-netty-shaded` | `1.58.0` | Netty-based gRPC transport layer |
| **gRPC Protobuf** | `io.grpc:grpc-protobuf` | `1.58.0` | gRPC integration with Google Protobuf |
| **gRPC Stub** | `io.grpc:grpc-stub` | `1.58.0` | Client and server stubs for gRPC calls |
| **Protobuf Java** | `com.google.protobuf:protobuf-java` | `3.24.0` | Core Protocol Buffers runtime |
| **Maven Plugin** | `protobuf-maven-plugin` | `0.6.1` | Protoc compiler plugin for stub generation |

---

## 👤 Author

- **ronilprasad8**
