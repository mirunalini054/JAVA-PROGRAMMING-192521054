import java.util.Scanner;

public class ExceptionFreeValidation {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            System.out.print("Enter withdrawal amount: ");

            if (!input.hasNextInt()) {
                System.out.println("ERROR_INVALID_INPUT");
                return;
            }

            int amount = input.nextInt();
            if (amount <= 0) {
                System.out.println("ERROR_INVALID_AMOUNT");
            } else if (amount > 5000) {
                System.out.println("ERROR_INSUFFICIENT_FUNDS");
            } else {
                System.out.println("SUCCESS");
            }
        }
    }
}
