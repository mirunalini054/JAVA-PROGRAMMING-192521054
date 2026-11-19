package parking.model;
import java.time.LocalDate;
public class Pass {
    private final String plate;
    private final LocalDate validThrough;
    public Pass(String plate, LocalDate validThrough) {
        if (plate == null || plate.isBlank() || validThrough == null)
            throw new IllegalArgumentException("Invalid pass");
        this.plate = plate.trim().toUpperCase(); this.validThrough = validThrough;
    }
    public boolean validFor(String requestedPlate, LocalDate date) {
        return plate.equals(requestedPlate.trim().toUpperCase()) && !date.isAfter(validThrough);
    }
}
