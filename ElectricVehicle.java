package parking.model;
public class ElectricVehicle extends Car implements Chargeable {
    public ElectricVehicle(String plate, String owner) { super(plate, owner); }
    @Override public String category() { return "ELECTRIC"; }
    @Override public int chargingSurchargePaise() { return 500; }
    @Override public long calculateFee(long minutes) {
        return Math.addExact(super.calculateFee(minutes), chargingSurchargePaise());
    }
}
