package parking.model;
import java.time.LocalDateTime;
public class Ticket {
    private static int issued = 0;
    private final int id;
    private final Vehicle vehicle;
    private final Slot slot;
    private final LocalDateTime entry;
    public Ticket(Vehicle vehicle, Slot slot, LocalDateTime entry) {
        this.id = ++issued; this.vehicle = vehicle; this.slot = slot; this.entry = entry;
    }
    public int getId() { return id; }
    public Vehicle getVehicle() { return vehicle; }
    public Slot getSlot() { return slot; }
    public LocalDateTime getEntry() { return entry; }
    public static int getIssued() { return issued; }
}
