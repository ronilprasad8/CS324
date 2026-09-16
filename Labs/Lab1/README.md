
# Lab 1: Java inheritance and polymorphism

This lab demonstrates abstract classes, inheritance, method overriding, and runtime polymorphism using musical instruments. It is a console application: instruments print the notes they play; they do not produce audio.

## Project structure

```text
Lab1/
|-- README.md
|-- run.bat                 Windows compile-and-run script
`-- music/
    |-- Instrument.java    Abstract base class declaring play(Note)
    |-- Flute.java         Instrument implementation for a flute
    |-- Guitar.java        Instrument implementation for a guitar
    |-- Note.java          Named note objects and their text representation
    `-- Music.java         main entry point and shared tune(Instrument) method
```

The directory also contains compiled `.class` files. Recompile the `.java` sources before running so that execution reflects the current code.

`Music.main()` creates a `Flute` and a `Guitar`, then passes each to `tune(Instrument)`. That method plays `Note.MIDDLE_C` and `Note.C_SHARP`. Java selects the appropriate overridden `play()` implementation at runtime. `Note` also defines `B_FLAT`, which this program does not use. These constants are instances of a class with a private constructor, not a Java enum.

## Prerequisites and setup

- Install a Java Development Kit (JDK), which supplies both `javac` and `java`. A JDK 11 or newer can compile this source; the lab does not declare a specific Java version.
- Make the JDK's `bin` directory available on `PATH`.
- Use Windows to run `run.bat`, or compile and run manually on another operating system.

No Maven, third-party libraries, database, or environment file is required.

Check the tools in a terminal:

```text
java -version
javac -version
```

From the repository root, change to the lab directory:

```text
cd Labs/Lab1
```

All commands below assume this is the current working directory.

## Run with the Windows script

In PowerShell:

```powershell
.\run.bat
```

In Command Prompt:

```bat
run.bat
```

`run.bat` is the only script supplied with this lab. It performs these steps:

1. Runs `javac music\*.java`, writing compiled classes beside the source files.
2. Runs `java music.Music`.
3. Runs `pause`, keeping the console open until a key is pressed.

The script uses relative paths and does not change to its own directory. Run it from `Labs/Lab1`; invoking it by path from the repository root will not resolve `music\*.java` correctly. It also does not stop after a compilation error, so check for compiler errors before trusting output from existing class files.

## Compile and run manually

In Windows PowerShell or Command Prompt:

```text
javac music\*.java
java -cp . music.Music
```

In a macOS or Linux shell:

```sh
javac music/*.java
java -cp . music.Music
```

Run the second command only after compilation succeeds. The classpath is the lab directory, which contains the `music` package directory. Manual execution does not pause at the end.

## Expected output

```text
Flute is playing Middle C
Flute is playing C Sharp
Guitar is playing Middle C
Guitar is playing C Sharp
End of program.
```

There is no application input. When using the batch script, press a key at the final pause prompt to close it.

## Troubleshooting

| Symptom | What to check |
| --- | --- |
| `javac` or `java` is not recognized | Install a full JDK, add its `bin` directory to `PATH`, and open a new terminal. |
| The compiler cannot find `music\*.java` | Change to `Labs/Lab1` before running the script or compiler. |
| `Could not find or load main class music.Music` | Compile successfully, remain in `Labs/Lab1`, and use `java -cp . music.Music`. |
| `UnsupportedClassVersionError` | Use a runtime compatible with the JDK that compiled the classes, or recompile using the intended JDK. |
| Output does not match recent changes | Resolve compilation errors and rebuild; the batch script can execute old classes after a failed compilation. |

No automated test suite is supplied. A successful compilation followed by the output above provides a basic check of the demonstration.
