# Assignment 5 – Java CO5

## Requirements
- JDK 17 or later (JDK 11+ should also work for the source files).
- VS Code with Extension Pack for Java, or terminal.

## Compile
Open this folder in VS Code. In the terminal:

```powershell
cd src
javac PassStore.java
java PassStore
javac LineCounter.java
java LineCounter
javac PassStoreBenchmark.java
java PassStoreBenchmark
```

The programs create temporary test data or files in the current working directory and print results. Run them locally and capture screenshots for your report.

## Files
- `PassStore.java`: add, find, list-by-route; malformed lines are skipped.
- `LineCounter.java`: compares byte-at-a-time and buffered line counting for 1 MB, 10 MB and 50 MB files.
- `PassStoreBenchmark.java`: measures sequential ID lookup as the number of records grows.

Replace the cover-page placeholders in the Word report. Fill timing tables using your own machine's output; no timings are fabricated.
