import java.util.concurrent.atomic.AtomicBoolean;

class AtomicSeat {
    private final AtomicBoolean booked = new AtomicBoolean(false);

    boolean bookSeat() {
        return booked.compareAndSet(false, true);
    }
}
