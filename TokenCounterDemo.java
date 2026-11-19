class TokenCounter {
    private final int capacity = 5;
    private int count = 0;

    public synchronized void produce() throws InterruptedException {
        // TODO 1 completed: wait while the buffer is full.
        while (count == capacity) {
            wait();
        }
        count++;
        System.out.println("Produced. Count = " + count);
        notifyAll();
    }

    public synchronized void consume() throws InterruptedException {
        // TODO 2 completed: wait while the buffer is empty.
        while (count == 0) {
            wait();
        }
        count--;
        System.out.println("Consumed. Count = " + count);
        notifyAll();
    }
}

public class TokenCounterDemo {
    public static void main(String[] args) {
        TokenCounter counter = new TokenCounter();

        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    counter.produce();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    counter.consume();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }
}
