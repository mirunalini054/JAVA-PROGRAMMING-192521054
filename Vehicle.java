package parking.model;
import java.util.Objects;
public abstract class Vehicle {
    private final String plate;
    private final String owner;
    public Vehicle(String plate, String owner) {
        if (plate == null || plate.isBlank() || owner == null || owner.isBlank())
            throw new IllegalArgumentException("Plate and owner must not be blank");
        this.plate = plate.trim().toUpperCase(); this.owner = owner.trim();
    }
    public Vehicle(Vehicle other) { this(other.plate, other.owner); }
    public String getPlate() { return plate; }
    public String getOwner() { return owner; }
    public abstract int hourlyRatePaise();
    public abstract String category();
    public long calculateFee(long minutes) {
        if (minutes < 0) throw new IllegalArgumentException("Negative duration");
        long hours = Math.max(1L, (minutes + 59) / 60);
        return Math.multiplyExact(hours, hourlyRatePaise());
    }
    @Override public boolean equals(Object obj) {
        return this == obj || (obj instanceof Vehicle && plate.equals(((Vehicle)obj).plate));
    }
    @Override public int hashCode() { return Objects.hash(plate); }
    @Override public String toString() { return category() + "[" + plate + ", owner=" + owner + "]"; }
}
