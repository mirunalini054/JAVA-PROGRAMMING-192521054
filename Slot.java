package parking.model;
public class Slot {
    private final int floor;
    private final int number;
    private final String type;
    private String occupiedPlate;
    public Slot(int floor, int number, String type) {
        if (floor < 1 || number < 1 || (!type.equals("TWO_WHEELER") && !type.equals("CAR") && !type.equals("ELECTRIC")))
            throw new IllegalArgumentException("Invalid slot");
        this.floor = floor; this.number = number; this.type = type;
    }
    public int getFloor() { return floor; }
    public int getNumber() { return number; }
    public String getType() { return type; }
    public boolean isAvailable() { return occupiedPlate == null; }
    public String getOccupiedPlate() { return occupiedPlate; }
    public void occupy(String plate) {
        if (!isAvailable()) throw new IllegalStateException("Slot occupied");
        occupiedPlate = plate;
    }
    public void release() { occupiedPlate = null; }
    @Override public String toString() { return "F" + floor + "-S" + number + "(" + type + ")"; }
}
