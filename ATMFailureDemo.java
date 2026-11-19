import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

class InsufficientBalanceException extends Exception {
    InsufficientBalanceException(String message) {
        super(message);
    }
}

public class ATMFailureDemo {
    static void withdraw(double balance, double amount)
            throws InsufficientBalanceException {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Withdrawal amount must be positive.");
        }
        if (amount > balance) {
            throw new InsufficientBalanceException(
                    "Insufficient account balance.");
        }
        System.out.println("Withdrawal successful: Rs. " + amount);
        System.out.println("Remaining balance: Rs. " + (balance - amount));
    }

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            System.out.print("Enter withdrawal amount: ");
            int amount = input.nextInt();

            File cardReader = new File("card-reader.txt");
            if (!cardReader.exists()) {
                throw new FileNotFoundException(
                        "Card-reader configuration file is missing.");
            }
            withdraw(5000.0, amount);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input: enter a whole number.");
        } catch (IllegalArgumentException | InsufficientBalanceException e) {
            System.out.println("Transaction rejected: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("ATM device error: " + e.getMessage());
        } finally {
            System.out.println("ATM session completed.");
        }
    }
}
