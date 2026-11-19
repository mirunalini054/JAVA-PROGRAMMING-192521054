package parking.app;
import parking.model.*;
import parking.service.*;
import java.time.*;
import java.util.*;
public class Main {
    public static void main(String[] args) {
        ParkingFacility facility = new ParkingFacility(2);
        Scanner in = new Scanner(System.in);
        System.out.println("CAMPUS SMART PARKING | 2 floors | Fees: bike Rs10/h, car Rs20/h, EV Rs20/h + Rs5 session");
        while (true) {
            System.out.print("\n1.Enter  2.Exit  3.Status  4.Monthly pass  0.Quit: ");
            String op = in.nextLine().trim();
            try {
                if (op.equals("0")) break;
                if (op.equals("1")) {
                    System.out.print("Type (bike/car/ev): "); String type = in.nextLine().trim().toLowerCase();
                    System.out.print("Plate: "); String plate = in.nextLine();
                    System.out.print("Owner: "); String owner = in.nextLine();
                    Vehicle v;
                    switch (type) {
                        case "bike": v = new TwoWheeler(plate,owner); break;
                        case "car": v = new Car(plate,owner); break;
                        case "ev": v = new ElectricVehicle(plate,owner); break;
                        default: throw new IllegalArgumentException("Unknown vehicle type");
                    }
                    Ticket t = facility.enter(v, LocalDateTime.now());
                    System.out.println("Ticket #"+t.getId()+" Slot="+t.getSlot());
                } else if (op.equals("2")) {
                    System.out.print("Plate: "); String plate=in.nextLine();
                    long fee = facility.exit(plate, LocalDateTime.now());
                    System.out.println("Fee: "+ParkingFacility.rupees(fee));
                } else if (op.equals("3")) {
                    System.out.println("Occupied " + facility.occupancy() + "/" + facility.capacity() + ", total entries=" + ParkingFacility.getTotalEntries());
                } else if (op.equals("4")) {
                    System.out.print("Plate: "); String plate=in.nextLine();
                    System.out.print("Valid-through date (YYYY-MM-DD): "); LocalDate until=LocalDate.parse(in.nextLine());
                    facility.addMonthlyPass(plate,until);
                    System.out.println("Pass added; fees waived on exit through " + until);
                } else System.out.println("Invalid selection");
            } catch (Exception e) { System.out.println("Error: "+e.getMessage()); }
        }
        in.close();
    }
}
