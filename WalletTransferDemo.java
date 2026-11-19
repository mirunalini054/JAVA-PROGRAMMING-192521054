class Wallet {
    final int id;
    int balance;

    Wallet(int id, int balance) {
        this.id = id;
        this.balance = balance;
    }
}

public class WalletTransferDemo {
    static void transfer(Wallet from, Wallet to, int amount) {
        Wallet first = from.id < to.id ? from : to;
        Wallet second = from.id < to.id ? to : from;

        // Consistent ascending ID order prevents circular lock ordering.
        synchronized (first) {
            synchronized (second) {
                if (amount <= 0 || from.balance < amount) {
                    System.out.println("Transfer rejected.");
                    return;
                }
                from.balance -= amount;
                to.balance += amount;
                System.out.println("Transfer completed.");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Wallet w1 = new Wallet(1, 1000);
        Wallet w2 = new Wallet(2, 1000);

        Thread t1 = new Thread(() -> transfer(w1, w2, 100));
        Thread t2 = new Thread(() -> transfer(w2, w1, 150));

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Wallet 1 balance: " + w1.balance);
        System.out.println("Wallet 2 balance: " + w2.balance);
    }
}
