CS324: Distributed Systems & Software Architecture LabsA collection of weekly laboratory implementations demonstrating core software architecture patterns, object-oriented principles, multithreading, database connectivity, and distributed systems technologies.📑 Table of ContentsRepository StructureLab Modules OverviewModule DetailsLab 1: OOP Polymorphism & InheritanceLab 2: Three-Tier Architecture & JDBCLab 3: Observer Design PatternLab 5: Distributed Client-Server ArchitectureLab 6: Concurrency, Deadlocks, & MultithreadingLab 7: Microservices with gRPC & Protocol BuffersPrerequisitesBuild and Execution Instructions📂 Repository StructureCS324/
└── Labs/
    ├── Lab1/              # Core OOP: Polymorphism & Dynamic Dispatch
    │   └── music/
    ├── Lab2/              # Three-Tier Architecture (UI, Business, Data Access)
    │   ├── database/
    │   └── src/
    ├── Lab3/              # Behavioral Patterns: Observer Pattern
    │   └── src/lab3/
    ├── Lab5/              # Distributed Banking API
    │   ├── Bank/
    │   └── Customer/
    ├── Lab6/              # Concurrency & Synchronization
    │   ├── Banking/
    │   ├── Deadlock/
    │   └── Gallery/
    └── Lab7/              # Remote Procedure Calls with gRPC & Protobuf
        └── src/
🔬 Lab Modules OverviewModuleCore TopicArchitectural PatternTechnologiesLab 1OOP FundamentalsPolymorphism & Dynamic DispatchJava SE, Batch ScriptingLab 2Enterprise Data LayeringThree-Tier Architecture (Presentation, Business, DAL)Java, Maven, JDBC, MS Access (.accdb)Lab 3Event-Driven UpdatesObserver Design PatternJava, Apache AntLab 5Distributed SystemsClient-Server Architecture via Shared InterfacesJava, Apache Ant, Remote ServicesLab 6MultithreadingConcurrency, Deadlock Inversion, Swing WorkersJava Threads, Swing, SynchronizationLab 7Modern RPCContract-First Microservice CommunicationJava, Maven, gRPC, Protocol Buffers (Proto3)🛠 Module DetailsLab 1: OOP Polymorphism & InheritanceExplores abstraction, inheritance hierarchies, and dynamic method dispatch using an orchestra/music model.Key Components:Instrument.java: Abstract base class defining playback contracts.Guitar.java, Flute.java: Concrete implementations overriding behavior.Note.java, Music.java: Main orchestrator and musical note representations.Lab 2: Three-Tier Architecture & JDBCIllustrates strict separation of concerns across enterprise layers to improve modularity and maintainability:Presentation Layer (presentation_layer/): Handles user interaction (UI.java, Application_main.java).Business Logic Layer (business_layer/): Enforces domain rules and data transformations (BLcustomer.java).Data Access Layer (data_access_layer/): Encapsulates direct database queries and connections (DAcustomer.java, Access_JDBC.java).Database (database/): Microsoft Access database file (new_test.accdb).Lab 3: Observer Design PatternDemonstrates the Gang of Four (GoF) Observer pattern to keep multiple dependent components updated upon a subject's state change:mySubject.java & number.java: Maintain state and notify registered listeners.myObserver.java: Observer interface.BinNumber.java & HexNumber.java: Concrete observers displaying real-time updates in binary and hexadecimal formats.Lab 5: Distributed Client-Server ArchitectureA multi-project distributed service decoupling banking logic across remote endpoints:Shared API (api/): Contains Api.java and data model Data.java defining the communication contract.Bank/: Server application implementing services (ApiImpl.java, Bank.java).Customer/: Client application consuming the bank service interface (Customer.java).Lab 6: Concurrency, Deadlocks, & MultithreadingExamines concurrent access, resource contention, and asynchronous GUI tasks across three sub-projects:Banking/: Thread synchronization and critical section management during concurrent account operations (Account.java, Transfer.java, Interest.java).Deadlock/: Simulates circular wait conditions and lock inversion between competing threads (Thread1.java, Thread2.java).Gallery/: Image viewer built with Java Swing demonstrating background image loading without freezing the GUI event dispatch thread.Lab 7: Microservices with gRPC & Protocol BuffersImplements modern remote procedure calls using strongly typed, contract-first service definitions:Schema (helloword.proto): Protocol Buffers interface definition file.HelloWorldServer.java: RPC service implementation.HelloWorldClient.java: gRPC client executing remote calls.⚙️ PrerequisitesJava Development Kit (JDK): Version 8 or 11+Build Tools:Apache Maven (3.6+)Apache Ant (or an IDE with Ant integration like NetBeans or IntelliJ IDEA)Database Drivers: UCanAccess / JDBC drivers (configured via Maven for Lab 2)🚀 Build and Execution Instructions1. Maven ProjectsLab 2: Three-Tier Customer Systemcd Labs/Lab2
mvn clean compile
mvn exec:java -Dexec.mainClass="presentation_layer.Application_main"
Lab 7: gRPC Servicecd Labs/Lab7
# Compile proto files and generate Java classes
mvn clean compile

# Terminal 1: Start the gRPC Server
mvn exec:java -Dexec.mainClass="com.example.grpc.HelloWorldServer"

# Terminal 2: Run the Client
mvn exec:java -Dexec.mainClass="com.example.grpc.HelloWorldClient"
2. Ant Projects (NetBeans / Command Line)Lab 3: Observer Patterncd Labs/Lab3
ant compile
ant run
Lab 5: Distributed Banking# Compile and run Bank Server
cd Labs/Lab5/Bank
ant compile
ant run

# In a separate terminal, compile and run Customer Client
cd Labs/Lab5/Customer
ant compile
ant run
Lab 6: Concurrency Submodules# Deadlock Simulation
cd Labs/Lab6/Deadlock
ant compile
ant run

# Multi-threaded Gallery
cd Labs/Lab6/Gallery
ant compile
ant run
3. Scripted ExecutionLab 1: OOP Musiccd Labs/Lab1
# On Windows:
run.bat

# Or manual compilation:
javac music/*.java
java music.Music
