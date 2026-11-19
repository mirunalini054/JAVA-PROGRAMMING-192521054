import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ManualSalesReport {
    public static void main(String[] args) {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter("daily-sales-manual.txt"));
            writer.write("Daily Sales Report");
            writer.newLine();
            writer.write("Total sales: Rs. 12500");
            System.out.println("Report generated using manual close.");
        } catch (IOException e) {
            System.out.println("Report error: " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException closeError) {
                    System.out.println("Close error: "
                            + closeError.getMessage());
                }
            }
        }
    }
}
