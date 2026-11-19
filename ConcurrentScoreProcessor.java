import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Stream;

class ScoreResult {
    final String fileName;
    final Integer score;
    final String error;

    ScoreResult(String fileName, Integer score, String error) {
        this.fileName = fileName;
        this.score = score;
        this.error = error;
    }

    boolean isSuccess() {
        return score != null;
    }
}

class ScoreTask implements Callable<ScoreResult> {
    private final Path file;

    ScoreTask(Path file) {
        this.file = file;
    }

    @Override
    public ScoreResult call() {
        try {
            String value = Files.readString(file).trim();
            int score = Integer.parseInt(value);

            if (score < 0 || score > 100) {
                return new ScoreResult(file.getFileName().toString(),
                        null, "Score outside 0-100");
            }
            return new ScoreResult(file.getFileName().toString(), score, null);
        } catch (IOException | NumberFormatException e) {
            return new ScoreResult(file.getFileName().toString(),
                    null, e.getMessage());
        }
    }
}

public class ConcurrentScoreProcessor {
    public static void main(String[] args) throws Exception {
        Path inputDir = Path.of("score-input");
        int poolSize = args.length > 0 ? Integer.parseInt(args[0]) : 4;

        if (poolSize < 1) {
            throw new IllegalArgumentException("Pool size must be at least 1.");
        }

        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        List<Future<ScoreResult>> futures = new ArrayList<>();
        LongAdder success = new LongAdder();
        LongAdder failure = new LongAdder();
        LongAdder total = new LongAdder();

        long start = System.nanoTime();

        try (Stream<Path> paths = Files.list(inputDir)) {
            paths.filter(Files::isRegularFile)
                    .forEach(path ->
                            futures.add(executor.submit(new ScoreTask(path))));
        } catch (IOException e) {
            executor.shutdownNow();
            throw e;
        }

        executor.shutdown();

        try {
            for (Future<ScoreResult> future : futures) {
                ScoreResult result = future.get();
                if (result.isSuccess()) {
                    success.increment();
                    total.add(result.score);
                } else {
                    failure.increment();
                    System.out.println("Failed: " + result.fileName
                            + " - " + result.error);
                }
            }
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } finally {
            if (!executor.isTerminated()) {
                executor.shutdownNow();
            }
        }

        long processed = success.sum() + failure.sum();
        double average = success.sum() == 0 ? 0.0
                : (double) total.sum() / success.sum();
        double elapsedMs = (System.nanoTime() - start) / 1_000_000.0;

        System.out.println("Processed: " + processed);
        System.out.println("Successful: " + success.sum());
        System.out.println("Failed: " + failure.sum());
        System.out.println("Average score: " + average);
        System.out.println("Pool size: " + poolSize);
        System.out.println("Elapsed ms: " + elapsedMs);
    }
}
