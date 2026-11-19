import parking.model.*;
import parking.service.*;
import java.time.*;
public class ParkingTest {
    private static int passed=0;
    static void check(String label, boolean condition) {
        if (!condition) throw new AssertionError("FAIL: "+label);
        System.out.println("PASS: "+label); passed++;
    }
    static boolean rejects(Runnable action) {
        try { action.run(); return false; }
        catch (IllegalArgumentException | IllegalStateException exception) { return true; }
    }
    public static void main(String[] args) {
        LocalDateTime t=LocalDateTime.of(2026,10,25,9,0);
        ParkingFacility p=new ParkingFacility(1);
        check("capacity of one floor",p.capacity()==10);
        Ticket a=p.enter(new TwoWheeler("tn01ab1234","Student"),t);
        check("allocate correct slot", a.getSlot().getType().equals("TWO_WHEELER"));
        check("occupancy after entry", p.occupancy()==1);
        check("prevent duplicate entry",rejects(()->p.enter(new TwoWheeler("TN01AB1234","X"),t)));
        check("one minute rounded to 1 hour",p.exit("tn01ab1234",t.plusMinutes(1))==1000);
        check("occupancy after exit",p.occupancy()==0);
        p.enter(new Car("CAR1","Staff"),t);
        check("car 61 minutes rounds to 2 hours",p.exit("CAR1",t.plusMinutes(61))==4000);
        p.enter(new ElectricVehicle("EV1","Visitor"),t);
        check("electric surcharge",p.exit("EV1",t.plusMinutes(60))==2500);
        p.addMonthlyPass("PASS1",t.toLocalDate().plusDays(30));
        p.enter(new Car("PASS1","Staff"),t);
        check("monthly pass zero fee",p.exit("PASS1",t.plusHours(2))==0);
        p.enter(new Car("TIME1","Staff"),t);
        check("reject exit before entry",rejects(()->p.exit("TIME1",t.minusMinutes(1))));
        check("invalid exit does not free slot",p.occupancy()==1);
        check("zero duration one hour min",p.exit("TIME1",t)==2000);
        for(int i=0;i<4;i++) p.enter(new TwoWheeler("FULL"+i,"Owner"),t);
        check("full compatible area rejects entry",rejects(()->p.enter(new TwoWheeler("EXTRA","Owner"),t)));
        check("value equality by plate",new Car("a1","a").equals(new Car("A1","b")));
        System.out.println("ALL " + passed + " TESTS PASSED");
    }
}
