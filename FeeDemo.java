package parking.app;
import parking.model.*;
public class FeeDemo {
    static void show(Vehicle v) { System.out.println(v.category() + " = " + v.calculateFee(60) + " paise"); }
    static void show(Vehicle v, boolean detailed) { show(v); if (detailed) System.out.println(v.toString()); }
    public static void main(String[] args) {
        Vehicle v = new ElectricVehicle("EV123", "Visitor");
        show(v); show(v, true); // overloading
        if (v instanceof Chargeable) System.out.println("EV surcharge = " + ((Chargeable)v).chargingSurchargePaise());
    }
}
