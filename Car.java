package parking.model;
public class Car extends Vehicle {
    public Car(String plate, String owner) { super(plate, owner); }
    @Override public int hourlyRatePaise() { return 2000; }
    @Override public String category() { return "CAR"; }
}
