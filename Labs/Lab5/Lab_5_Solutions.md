# CS324 - Distributed Systems
## Lab 5: Remote Method Invocation (RMI) in Java — Complete Solutions & Report

---

## Exercise 1

### Question 1: What does the file `Api.java` contain? Why do both projects contain a file `Api.java`?
- **Contents:** `Api.java` contains the **Remote Interface** (`api.Api`), which extends `java.rmi.Remote`. It declares the methods that can be invoked remotely by clients (e.g., `setBalance`, `deposit`, `withdraw`, `addInterest`), each declaring `throws RemoteException`.
- **Why both projects contain it:** In Java RMI, the interface defines the **contract** between the client and server:
  - The **Server (`Bank`)** needs `Api.java` to implement the methods in `ApiImpl` (`public class ApiImpl extends UnicastRemoteObject implements Api`).
  - The **Client (`Customer`)** needs `Api.java` at compile time and runtime to know what remote methods are available and to cast the retrieved remote stub reference (`(Api) registry.lookup(...)`). This allows the client to invoke remote methods without needing access to the server's concrete implementation class (`ApiImpl`).

---

### Question 2: Why does only one of the projects contain a file `ApiImpl.java`?
- `ApiImpl.java` is the **concrete implementation** of the `Api` interface.
- In distributed client-server architecture, the server is responsible for hosting business logic, holding persistent state (such as the bank account balance), and performing actual operations.
- The client acts purely as a consumer that invokes methods remotely via proxy/stub references. It does not need, and for encapsulation and security reasons should not have, the server-side implementation code.

---


### Question 3: Describe what happens when the client calls method `setBalance(1000)`?
1. **Client Method Invocation:** The client calls `remoteApi.setBalance(...)` on the local stub/proxy instance.
2. **Marshaling (Serialization):** The client-side RMI runtime marshals (serializes) the method signature and arguments into a network byte stream.
3. **Network Transmission:** The RMI runtime transmits the serialized request across TCP/IP socket connection (port 1099) to the remote server host.
4. **Unmarshaling on Server:** The server-side RMI runtime receives the request, deserializes the arguments, and identifies the target remote object (`ApiImpl`).
5. **Method Execution:** The server invokes `ApiImpl.setBalance(1000)` on the actual remote object, updating the server's internal state (`account.setValue(1000)`).
6. **Return Marshaling:** The server RMI runtime marshals the return value (or exception, if any) into bytes and sends it back across the socket.
7. **Client Receives Result:** The client unmarshals the return value and delivers it to the calling client thread as the normal method return value.

---

### Question 4: In which line does the server register its service, in which line does the client connect to the registry?
- **Server Registration (`Bank.java`):**
  - Line 21 creates the RMI registry: `Registry registry = LocateRegistry.createRegistry(1099);`
  - **Line 24 registers/binds the service:**
    ```java
    registry.rebind(Api.class.getSimpleName(), new ApiImpl());
    ```
- **Client Connection (`Customer.java`):**
  - **Line 18 connects to / locates the RMI registry:**
    ```java
    registry = LocateRegistry.getRegistry(HOST, PORT);
    ```
  - **Line 19 looks up the service by name:**
    ```java
    Api remoteApi = (Api) registry.lookup(Api.class.getSimpleName());
    ```

---

### Question 5: Run the client several times. What is the output?
- **Client Output:**
  ```
  New balance = 1000
  ```
- **Explanation:** Every time the client is executed, it calls `remoteApi.setBalance(1000)`. Because `setBalance` explicitly sets/overwrites the stored balance to `1000`, the return value is always `1000` regardless of how many times the client is run. On the server side, `new balance: 1000` is printed on each client execution.

---

### Question 6: Open the task manager in windows, and locate the process running the server. Why is the server running continuously?
- When the server calls `LocateRegistry.createRegistry(1099)` and exports `ApiImpl` (which extends `UnicastRemoteObject`), the RMI runtime spawns active **non-daemon background threads** (such as TCP listener threads and connection acceptance threads).
- In Java, the Java Virtual Machine (JVM) stays alive as long as at least one non-daemon thread continues running. Because these listener threads continuously listen for incoming remote client requests, the server process does not terminate automatically.

---

### Question 7: Where is the process for the client?
- The client process is no longer visible in Task Manager because it has already **terminated**.
- Once the client's `main()` method finishes executing all its statements (looking up the registry, invoking the method, and printing the balance), there are no non-daemon threads left running in the client JVM. Thus, the client process exits immediately upon completion.

---

## Exercise 2

### Code Changes
Added three new remote methods:
1. `deposit(int amount)` / add an integer amount to balance
2. `withdraw(int amount)` / withdraw an integer amount from balance
3. `addInterest(int rate)` / add interest given an interest rate

#### `Api.java` (in both `Bank` and `Customer`):
```java
package api;

import java.rmi.*;

public interface Api extends Remote {
    public int setBalance(int value) throws RemoteException;
    public int deposit(int amount) throws RemoteException;
    public int withdraw(int amount) throws RemoteException;
    public double addInterest(int rate) throws RemoteException;
}
```

#### `ApiImpl.java` (in `Bank`):
```java
@Override
public synchronized int deposit(int amount) throws RemoteException {
    account.setValue(account.getValue() + amount);
    System.out.println("deposited: " + amount + ", new balance: " + account.getValue());
    return account.getValue();
}

@Override
public synchronized int withdraw(int amount) throws RemoteException {
    account.setValue(account.getValue() - amount);
    System.out.println("withdrawn: " + amount + ", new balance: " + account.getValue());
    return account.getValue();
}

@Override
public synchronized double addInterest(int rate) throws RemoteException {
    double current = account.getValue();
    double newBalance = current + (current * rate / 100.0);
    account.setValue((int) Math.round(newBalance));
    System.out.println("added interest rate " + rate + "%, new balance: " + newBalance);
    return newBalance;
}
```

#### `Customer.java`:
```java
Data depositResult = remoteApi.deposit(500);
Data withdrawResult = remoteApi.withdraw(200);
Data interestResult = remoteApi.addInterest(10);
```

### Question 8: Run the client several times. What is the output?
- **Run 1:**
  ```
  Deposit 500: New balance = 500
  Withdraw 200: New balance = 300
  Add 10% Interest: New balance = 330.0
  ```
- **Run 2:**
  ```
  Deposit 500: New balance = 830
  Withdraw 200: New balance = 630
  Add 10% Interest: New balance = 693.0
  ```
- **Run 3:**
  ```
  Deposit 500: New balance = 1193
  Withdraw 200: New balance = 993
  Add 10% Interest: New balance = 1092.3
  ```
- **Explanation:** Unlike `setBalance(1000)` which resets the balance on each run, the three new methods modify the existing balance accumulatively. Because the server object (`ApiImpl`) remains active in memory on the server process between client invocations, its state is **preserved across multiple client runs**.

---

## Exercise 3

### Added `Data.java`
Added to both `Bank/src/api/Data.java` and `Customer/src/api/Data.java`:
```java
package api;

import java.io.*;

public class Data implements Serializable {
    private static final long serialVersionUID = 1L;
    private int value;

    public Data(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
```

### Updated `Api.java` (in both `Bank` and `Customer`):
```java
package api;

import java.rmi.*;

public interface Api extends Remote {
    public Data setBalance(Data value) throws RemoteException;
    public Data deposit(Data amount) throws RemoteException;
    public Data withdraw(Data amount) throws RemoteException;
    public Data addInterest(Data rate) throws RemoteException;
}
```

### Updated `ApiImpl.java` (in `Bank`):
```java
package bank;

import java.rmi.*;
import java.rmi.server.*;
import api.*;

public class ApiImpl extends UnicastRemoteObject implements Api {
    private static final long serialVersionUID = 1L;
    private Data account = new Data(0);

    public ApiImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized Data setBalance(Data value) throws RemoteException {
        account.setValue(value.getValue());
        System.out.println("new balance: " + account.getValue());
        return new Data(account.getValue());
    }

    @Override
    public synchronized Data deposit(Data amount) throws RemoteException {
        account.setValue(account.getValue() + amount.getValue());
        System.out.println("deposited: " + amount.getValue() + ", new balance: " + account.getValue());
        return new Data(account.getValue());
    }

    @Override
    public synchronized Data withdraw(Data amount) throws RemoteException {
        account.setValue(account.getValue() - amount.getValue());
        System.out.println("withdrawn: " + amount.getValue() + ", new balance: " + account.getValue());
        return new Data(account.getValue());
    }

    @Override
    public synchronized Data addInterest(Data rate) throws RemoteException {
        double current = account.getValue();
        double newBalance = current + (current * rate.getValue() / 100.0);
        account.setValue((int) Math.round(newBalance));
        System.out.println("added interest rate " + rate.getValue() + "%, new balance: " + account.getValue());
        return new Data(account.getValue());
    }
}
```

### Updated `Customer.java`:
```java
package customer;

import java.rmi.registry.*;
import api.*;

public class Customer {
    private static final String HOST = "localhost";
    private static final int PORT = 1099;
    private static Registry registry;   

    public static void main(String[] args) throws Exception {
        registry = LocateRegistry.getRegistry(HOST, PORT);
        Api remoteApi = (Api) registry.lookup(Api.class.getSimpleName());
        
        Data depositResult = remoteApi.deposit(new Data(500));
        System.out.println("Deposit 500: New balance = " + depositResult.getValue());

        Data withdrawResult = remoteApi.withdraw(new Data(200));
        System.out.println("Withdraw 200: New balance = " + withdrawResult.getValue());

        Data interestResult = remoteApi.addInterest(new Data(10));
        System.out.println("Add 10% Interest: New balance = " + interestResult.getValue());
    }
}
```

### Question 9: Build and restart the server. Run the client several times. What is the output?
- **Server Console:**
  ```
  system is ready
  deposited: 500, new balance: 500
  withdrawn: 200, new balance: 300
  added interest rate 10%, new balance: 330
  deposited: 500, new balance: 830
  withdrawn: 200, new balance: 630
  added interest rate 10%, new balance: 693
  deposited: 500, new balance: 1193
  withdrawn: 200, new balance: 993
  added interest rate 10%, new balance: 1092
  ```

- **Client Console Output Across Runs:**
  - **Run 1:**
    ```
    Deposit 500: New balance = 500
    Withdraw 200: New balance = 300
    Add 10% Interest: New balance = 330
    ```
  - **Run 2:**
    ```
    Deposit 500: New balance = 830
    Withdraw 200: New balance = 630
    Add 10% Interest: New balance = 693
    ```
  - **Run 3:**
    ```
    Deposit 500: New balance = 1193
    Withdraw 200: New balance = 993
    Add 10% Interest: New balance = 1092
    ```

---

### Question 10: `class Data implements Serializable`. What does it mean to serialize an object, and why is this necessary in the context of RMIs?
1. **Meaning of Serialization:**
   - **Serialization** is the process of converting the state of an in-memory Java object into a byte stream (a sequence of bytes).
   - The corresponding reverse operation, **deserialization**, reconstructs the object in memory with its state from that byte stream.
2. **Why it is necessary in RMI:**
   - In a distributed system, the client and server run in **separate JVM processes** (often on physically distinct machines across a network) with completely separate memory address spaces.
   - Objects cannot be passed across a network by reference or memory pointer.
   - For an object (like `Data`) to be passed as a method argument or returned from a remote invocation (**pass-by-value**), it must be serialized into bytes on the sender's side, transmitted over network sockets, and deserialized back into a copy of the object on the receiver's side.
   - Without implementing `java.io.Serializable` (or `java.io.Externalizable`), the JVM throws a `java.io.NotSerializableException` when attempting to pass the object remotely.

---

## How to Compile and Run

### Option 1: In Command Prompt (`cmd.exe`)
```cmd
:: 1. Compile Bank
javac -d Bank/build/classes Bank\src\api\*.java Bank\src\bank\*.java

:: 2. Compile Customer
javac -d Customer/build/classes Customer\src\api\*.java Customer\src\customer\*.java

:: 3. Start Server (Terminal 1)
java -cp Bank/build/classes bank.Bank

:: 4. Run Client (Terminal 2)
java -cp Customer/build/classes customer.Customer
```

### Option 2: In PowerShell
```powershell
# 1. Compile Bank
javac -d Bank/build/classes (Get-ChildItem -Path Bank/src -Filter *.java -Recurse | ForEach-Object { $_.FullName })

# 2. Compile Customer
javac -d Customer/build/classes (Get-ChildItem -Path Customer/src -Filter *.java -Recurse | ForEach-Object { $_.FullName })

# 3. Start Server (Terminal 1)
java -cp Bank/build/classes bank.Bank

# 4. Run Client (Terminal 2)
java -cp Customer/build/classes customer.Customer
```

