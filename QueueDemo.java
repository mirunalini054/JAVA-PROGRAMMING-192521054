import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class QueueDemo {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);
        queue.offer(10);
        queue.offer(20);
        System.out.println("Consumed: " + queue.poll());
    }
}
