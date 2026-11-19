package parking.model;
public class TwoWheeler extends Vehicle {
    public TwoWheeler(String plate, String owner) { super(plate, owner); }
    @Override public int hourlyRatePaise() { return 1000; }
    @Override public String category() { return "TWO_WHEELER"; }
}
