# CS324 Lab 6: Java Multithreading and Concurrency

This directory contains Java lab projects for CS324 demonstrating core concepts of **Multithreading**, **Thread Synchronization**, **Deadlock Scenarios**, and **GUI Thread Safety (Java Swing)**.

---

## Project Structure

```text
Lab6/
├── Banking/     # Multi-threaded banking operations (transfers & interest)
├── Deadlock/    # Classic thread deadlock demonstration
└── Gallery/     # Swing GUI with concurrent worker threads & EDT safety
```

---

## 1. Banking System (`/Banking`)

### Overview
Simulates a multi-threaded banking environment where concurrent operations (interest application and fund transfers) are performed on bank accounts.

### Key Files
- **[Account.java](file:///c:/Users/ronil_47xp2lm/OneDrive%20-%20The%20University%20of%20the%20South%20Pacific/Resources/Semester%206/CS324/Week%207/Lab%20resources/Lab6/Banking/src/banking/Account.java)**: Defines bank account attributes (`accountHolder`, `accountType`, `balance`) and methods for `deposit()`, `withdraw()`, and `addinterest()`.
- **[Interest.java](file:///c:/Users/ronil_47xp2lm/OneDrive%20-%20The%20University%20of%20the%20South%20Pacific/Resources/Semester%206/CS324/Week%207/Lab%20resources/Lab6/Banking/src/banking/Interest.java)**: A `Thread` subclass that calculates and applies monthly interest to a designated account.
- **[Transfer.java](file:///c:/Users/ronil_47xp2lm/OneDrive%20-%20The%20University%20of%20the%20South%20Pacific/Resources/Semester%206/CS324/Week%207/Lab%20resources/Lab6/Banking/src/banking/Transfer.java)**: A `Thread` subclass executing money transfers from one account to another (`withdraw` followed by `deposit`).
- **[Banking.java](file:///c:/Users/ronil_47xp2lm/OneDrive%20-%20The%20University%20of%20the%20South%20Pacific/Resources/Semester%206/CS324/Week%207/Lab%20resources/Lab6/Banking/src/banking/Banking.java)**: Main class that initializes accounts, starts interest threads, joins them, and executes transfers.

### Key Concepts
- Extending `java.lang.Thread` to define concurrent worker tasks.
- Synchronization and sequencing using `thread.start()` and `thread.join()`.
- Handling shared state manipulation across threads.

---

## 2. Deadlock Demonstration (`/Deadlock`)

### Overview
Demonstrates a classic **deadlock** condition caused by inverse lock acquisition order between two competing threads.

### Key Files
- **[Main.java](file:///c:/Users/ronil_47xp2lm/OneDrive%20-%20The%20University%20of%20the%20South%20Pacific/Resources/Semester%206/CS324/Week%207/Lab%20resources/Lab6/Deadlock/src/myapplication/Main.java)**: Instantiates shared monitor objects (`myLock1`, `myLock2`) and launches `Thread1` and `Thread2`.
- **[Thread1.java](file:///c:/Users/ronil_47xp2lm/OneDrive%20-%20The%20University%20of%20the%20South%20Pacific/Resources/Semester%206/CS324/Week%207/Lab%20resources/Lab6/Deadlock/src/myapplication/Thread1.java)**: Synchronizes on `myLock2` first, then attempts to acquire `myLock1`.
- **[Thread2.java](file:///c:/Users/ronil_47xp2lm/OneDrive%20-%20The%20University%20of%20the%20South%20Pacific/Resources/Semester%206/CS324/Week%207/Lab%20resources/Lab6/Deadlock/src/myapplication/Thread2.java)**: Synchronizes on `myLock1` first, then attempts to acquire `myLock2`.

### Deadlock Mechanics
1. `Thread1` locks `myLock2`.
2. `Thread2` locks `myLock1`.
3. `Thread1` tries to acquire `myLock1` (held by `Thread2`) and blocks.
4. `Thread2` tries to acquire `myLock2` (held by `Thread1`) and blocks.
5. Both threads remain permanently suspended in a circular dependency deadlock.

---

## 3. Image Gallery Application (`/Gallery`)

### Overview
A Java Swing desktop application displaying image panels animated by individual worker threads. Illustrates safe multi-threaded GUI programming in Java.

### Key Files
- **[Gallery.java](file:///c:/Users/ronil_47xp2lm/OneDrive%20-%20The%20University%20of%20the%20South%20Pacific/Resources/Semester%206/CS324/Week%207/Lab%20resources/Lab6/Gallery/src/gallery/Gallery.java)**: Contains the full GUI application structure:
  - **`Gallery`**: Builds main `JFrame`, panels, and interactive controls ("More" / "Less" buttons) to dynamically add or interrupt animation threads.
  - **`ImagePanel`**: Custom `JPanel` that renders images via `paintComponent()`.
  - **`Tred`**: Worker `Thread` cycling images at 1-second intervals. Safely updates UI elements on the Event Dispatch Thread (EDT) using `SwingUtilities.invokeAndWait()` and cleans up resources when interrupted.

### Key Concepts
- **Swing UI Thread Safety**: Dispatching GUI updates onto the Event Dispatch Thread (EDT) via `SwingUtilities.invokeAndWait()`.
- Responsive GUI interaction paired with background worker thread control (`start()` and `interrupt()`).

---

## Compilation & Execution

### Requirements
- Java Development Kit (JDK 8 or higher)
- NetBeans IDE (optional, project files included)

### Running via Command Line

#### 1. Banking Application
```bash
cd Banking/src
javac banking/*.java
java banking.Banking
```

#### 2. Deadlock Application
```bash
cd Deadlock/src
javac myapplication/*.java
java myapplication.Main
```

#### 3. Gallery Application
```bash
cd Gallery/src
javac gallery/*.java
java gallery.Gallery
```
