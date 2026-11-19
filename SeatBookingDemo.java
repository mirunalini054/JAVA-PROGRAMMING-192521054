class UnsafeSeatBooking {
    private boolean booked = false;

    boolean bookSeat() {
        if (!booked) {
            try {
                Thread.sleep(10); // makes the race easier to observe
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
            booked = true;
            return true;
        }
        return false;
    }
}

class SafeSeatBooking {
    private boolean booked = false;

    public synchronized boolean bookSeat() {
        if (booked) {
            return false;
        }
        booked = true;
        return true;
    }
}

public class SeatBookingDemo {
    public static void main(String[] args) throws InterruptedException {
        SafeSeatBooking seat = new SafeSeatBooking();

        Runnable request = () -> System.out.println(
                Thread.currentThread().getName()
                + " booking result: " + seat.bookSeat());

        Thread a = new Thread(request, "Customer-A");
        Thread b = new Thread(request, "Customer-B");

        a.start();
        b.start();
        a.join();
        b.join();
    }
}
