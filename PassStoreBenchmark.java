import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class PassStoreBenchmark {
    public static void main(String[] args) throws Exception {
        int[] sizes = {1_000, 10_000, 50_000, 100_000};
        for (int n : sizes) {
            Path file = Files.createTempFile("pass-benchmark", ".csv");
            StringBuilder batch = new StringBuilder();
            for (int i = 0; i < n; i++) {
                batch.append("P").append(i).append(",REG").append(i)
                     .append(",Student").append(i).append(",5,1000.0\n");
                if (batch.length() >= 1_000_000) {
                    Files.writeString(file, batch.toString(), java.nio.file.StandardOpenOption.APPEND);
                    batch.setLength(0);
                }
            }
            if (batch.length() > 0)
                Files.writeString(file, batch.toString(), java.nio.file.StandardOpenOption.APPEND);

            PassStore store = new PassStore(file);
            String target = "P" + (n - 1);
            long start = System.nanoTime();
            for (int q = 0; q < 100; q++) store.find(target);
            long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            System.out.printf("%d records | 100 searches | total=%d ms | avg=%.3f ms%n",
                    n, elapsed, elapsed / 100.0);
            Files.deleteIfExists(file);
        }
    }
}
