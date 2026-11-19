import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SafeSalesReport {
    public static void main(String[] args) {
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter("daily-sales-safe.txt"))) {
            writer.write("Daily Sales Report");
            writer.newLine();
            writer.write("Total sales: Rs. 12500");
            System.out.println("Report generated safely.");
        } catch (IOException e) {
            System.out.println("Report error: " + e.getMessage());
            for (Throwable suppressed : e.getSuppressed()) {
                System.out.println("Suppressed close error: "
                        + suppressed.getMessage());
            }
        }
    }
}
