import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class LineCounter {
    private static long countUnbuffered(Path path) throws IOException {
        long lines = 0;
        try (InputStream in = Files.newInputStream(path)) {
            int b;
            while ((b = in.read()) != -1) {
                if (b == '\n') lines++;
            }
        }
        return lines;
    }

    private static long countBuffered(Path path) throws IOException {
        long lines = 0;
        try (InputStream in = new BufferedInputStream(Files.newInputStream(path))) {
            byte[] buffer = new byte[64 * 1024];
            int n;
            while ((n = in.read(buffer)) != -1) {
                for (int i = 0; i < n; i++) if (buffer[i] == '\n') lines++;
            }
        }
        return lines;
    }

    private static void createFile(Path path, int sizeMiB) throws IOException {
        byte[] line = "attendance-record,student,2026-11-19\n".getBytes();
        long target = (long) sizeMiB * 1024 * 1024;
        try (var out = Files.newOutputStream(path)) {
            long written = 0;
            while (written < target) {
                int amount = (int)Math.min(line.length, target - written);
                out.write(line, 0, amount);
                written += amount;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        for (int size : new int[]{1, 10, 50}) {
            Path file = Files.createTempFile("attendance-" + size, ".log");
            createFile(file, size);
            // Warm up the buffered path once.
            countBuffered(file);
            long t1 = System.nanoTime();
            long a = countUnbuffered(file);
            long unbufferedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - t1);
            long t2 = System.nanoTime();
            long b = countBuffered(file);
            long bufferedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - t2);
            System.out.printf("%d MB | lines=%d | unbuffered=%d ms | buffered=%d ms%n",
                    size, a, unbufferedMs, bufferedMs);
            if (a != b) throw new IllegalStateException("Counts differ");
            Files.deleteIfExists(file);
        }
    }
}
