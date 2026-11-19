import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScannerPoolDemo {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(4);

        try {
            for (int i = 1; i <= 4; i++) {
                final int id = i;
                pool.submit(() -> System.out.println(
                        "Station " + id + " scanned a script."));
            }
        } finally {
            pool.shutdown();
        }
    }
}
