# Lab 2: Maven customer management application

This CS324 lab is a Java console application for adding, deleting, updating, and listing customers stored in a Microsoft Access database. It demonstrates separation into presentation, business, and data access layers, with Maven managing compilation and the JDBC dependency.

## Project structure

```text
Lab2/
|-- README.md
|-- pom.xml
|-- database/
|   `-- new_test.accdb                Access database used at runtime
|-- src/
|   |-- presentation_layer/
|   |   |-- Application_main.java    Application entry point
|   |   `-- UI.java                  Console menu and input handling
|   |-- business_layer/
|   |   `-- BLcustomer.java          Customer fields and business operations
|   `-- data_access_layer/
|       |-- Access_JDBC.java         JDBC connection management
|       `-- DAcustomer.java          SQL operations and result mapping
`-- target/                         Maven-generated build output
    |-- classes/                    Compiled package directories
    `-- maven-status/               Compiler bookkeeping
```

The POM explicitly sets `<sourceDirectory>src</sourceDirectory>`. Sources therefore live directly under `src/`, rather than Maven's conventional `src/main/java/`. An existing `Access_JDBC.class` also resides beside its source; use Maven's freshly compiled output in `target/classes` when running the application. `target/` can be regenerated with Maven.

The execution flow is `Application_main` → `UI` → `BLcustomer` → `DAcustomer` → `Access_JDBC`/database. `BLcustomer` holds the customer ID, first name, and last name and delegates persistence operations to `DAcustomer`. The data access layer maps query results back into `BLcustomer` objects.

## Maven configuration and dependencies

The coordinates declared in `pom.xml` are:

| Setting | Value |
| --- | --- |
| Group ID | `com.cs324` |
| Artifact ID | `labwk3` |
| Version | `1.0` |
| Packaging | `jar` |
| Source directory | `src` |
| Java source / target | `11` / `11` |
| Main class | `presentation_layer.Application_main` |

Although the directory is named `Lab2`, the Maven artifact is named `labwk3`.

### Declared application dependency

| Dependency | Version | Scope | Purpose |
| --- | --- | --- | --- |
| `io.github.spannm:ucanaccess` | `5.1.6` | `compile` | Supplies the UCanAccess JDBC driver for accessing the `.accdb` database. |

This is the only direct dependency in the POM. Maven resolves its transitive dependencies automatically. The Java driver class loaded by the source is `net.ucanaccess.jdbc.UcanaccessDriver`; its package name differs from the dependency's Maven group ID.

To inspect the resolved dependency tree from this directory:

```text
mvn dependency:tree
```

### Build plugins

| Plugin | Version | Configuration |
| --- | --- | --- |
| `org.apache.maven.plugins:maven-compiler-plugin` | `3.11.0` | Compiles Java with source and target level 11. |
| `org.codehaus.mojo:exec-maven-plugin` | `3.1.0` | Runs `presentation_layer.Application_main` using the project's runtime classpath. |

The POM does not configure an executable JAR manifest or bundle dependencies into the JAR. Use the Maven execution command below instead of `java -jar`.

## Prerequisites and setup

- A JDK 11 or newer, with `java` and `javac` available on `PATH`.
- Apache Maven installed and available as `mvn`. No Maven wrapper is supplied.
- `JAVA_HOME` pointing to the JDK installation when required by your Maven setup.
- Network access for Maven's first download of dependencies and plugins, or an already populated local Maven cache.
- The supplied `database/new_test.accdb` file and permission to read and write it for customer operations.

Verify the tools:

```text
java -version
javac -version
mvn -version
```

Check that Maven reports the intended JDK. From the repository root, enter the lab directory:

```text
cd Labs/Lab2
```

Run all following commands from `Labs/Lab2`. There is no `run.bat` or other launcher script in this lab.

## Database configuration

`src/data_access_layer/Access_JDBC.java` connects using this hard-coded JDBC URL:

```text
jdbc:ucanaccess://database//new_test.accdb
```

The path is relative to the process working directory. Keep the database at `Labs/Lab2/database/new_test.accdb` and launch from `Labs/Lab2`. Merely pointing Maven at the POM from another directory does not establish the correct working directory for this path. For an IDE launch, explicitly set the working directory to `Labs/Lab2`.

The SQL in `DAcustomer.java` expects a table named `customer` with these columns:

| Column | How the application uses it |
| --- | --- |
| `id` | Customer identifier; entered as text but inserted into SQL without quotes, so use a numeric ID. |
| `lname` | Last name, handled as a string. |
| `fname` | First name, handled as a string. |

These are the requirements visible in the source, not a full specification of the database's column types or constraints. No database creation or migration script is provided. The connection call supplies no username or password; the class's URL, connection-string, and credential setters are not used by `connect()`. To change the database location with the current implementation, edit the URL in `connect()` and recompile.

Add, update, and delete operations change the supplied database. Make a copy before experimenting if you need to preserve its initial records.

## Build and run

Compile and start the interactive application:

```text
mvn compile exec:java
```

To rebuild from clean Maven output and then run:

```text
mvn clean compile exec:java
```

To perform compilation and execution separately:

```text
mvn clean compile
mvn exec:java
```

Run the execution command after the build succeeds. `exec:java` uses the configured main class and includes Maven dependencies on the classpath; it does not itself compile source changes.

To create the ordinary JAR artifact:

```text
mvn clean package
```

This produces `target/labwk3-1.0.jar`. The JAR does not include the external database or its dependency JARs, and is not configured for direct `java -jar` execution. No automated test sources or test dependencies are supplied, so a successful package build does not verify database operations.

For IDE execution, import `pom.xml` as a Maven project, select a compatible JDK, allow dependency resolution, and run `presentation_layer.Application_main` with `Labs/Lab2` as the working directory.

## Using the console menu

```text
press 1 to add a customer
press 2 to delete a customer
press 3 to update a customer
press 4 to view all current customers
press 5 to exit the application
```

| Choice | Operation | Input |
| --- | --- | --- |
| `1` | Add a customer | ID, last name, then first name. |
| `2` | Delete a customer | ID of the customer to delete. |
| `3` | Update a customer | Existing ID, new last name, then new first name. |
| `4` | List customers | No additional input; results are ordered by ID. |
| `5` | Exit | No additional input. |

Enter each response followed by Enter. Listing customers prints their ID, first name, and last name, or `No customers found` when the returned list is empty.

For a basic manual check, start the program, choose `4` to exercise database access, then choose `5` to exit. To check writes, use a disposable database copy, add an unused numeric ID, list it, update it, list again, delete it, and confirm its removal.

## Current limitations and troubleshooting

- Menu input uses `Scanner.nextInt()`: enter an integer. Non-numeric input can terminate the application with an input mismatch exception. Integers outside `1`–`5` display an invalid-choice message.
- IDs and names use `Scanner.next()`, so enter single-token values without spaces.
- SQL is assembled by concatenating input. Names containing apostrophes can break queries, and input is not parameterized. Treat this as a teaching application, not a production-ready customer service.
- Add and delete catch and print database errors internally, so the UI can print a success message even when the operation failed. Update and delete also do not check the affected-row count. Verify changes by listing records and reading any error output.

| Symptom | What to check |
| --- | --- |
| `mvn` is not recognized | Install Maven, add its `bin` directory to `PATH`, and open a new terminal. |
| Java target or compiler errors | Check `mvn -version` and `JAVA_HOME`; Maven must use a JDK compatible with Java 11. |
| Maven cannot resolve dependencies or plugins | Check network access and Maven proxy/repository settings, then retry the build. |
| Driver class not found | Run through `mvn compile exec:java` or an IDE with resolved Maven dependencies. Plain `java -cp target/classes ...` omits the driver and its dependencies. |
| Database connection failure | Check the working directory, database file, permissions, and any file locks. Read the first `Error:` message; a failed connection may be followed by a null-connection exception. |
| Missing table or column | Confirm the database has the `customer` table and the `id`, `lname`, and `fname` columns expected by the SQL. |
| SQL error while adding or updating | Use a numeric ID and single-token names without apostrophes; check database constraints and whether an ID already exists. |
