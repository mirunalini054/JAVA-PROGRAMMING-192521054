class ScannerStation implements Runnable {
    private final int stationId;

    ScannerStation(int stationId) {
        this.stationId = stationId;
    }

    @Override
    public void run() {
        try {
            System.out.println("Station " + stationId + " started scanning.");
            Thread.sleep(500);
            System.out.println("Station " + stationId + " completed scanning.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Station " + stationId + " was interrupted.");
        }
    }
}

public class ScannerStationsDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread[] stations = new Thread[4];

        for (int i = 0; i < stations.length; i++) {
            stations[i] = new Thread(
                    new ScannerStation(i + 1), "Scanner-" + (i + 1));
            stations[i].setPriority(Thread.NORM_PRIORITY);
        }

        for (Thread station : stations) {
            System.out.println(station.getName() + " state: "
                    + station.getState());
            station.start();
        }

        for (Thread station : stations) {
            station.join();
            System.out.println(station.getName()
                    + " alive after join: " + station.isAlive());
        }

        System.out.println("All four stations completed.");
    }
}
