import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class PassStore {
    private final Path file;

    public PassStore(Path file) {
        this.file = file;
    }

    // Text record: passId,regNo,name,route,amount (CSV-like; quote fields containing commas).
    private String encode(String... fields) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) out.append(',');
            String value = fields[i].replace("\"", "\"\"");
            if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
                out.append('"').append(value).append('"');
            } else {
                out.append(value);
            }
        }
        return out.toString();
    }

    // Small CSV parser supporting quoted commas and doubled quotes.
    private String[] parse(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    field.append('"'); i++;
                } else {
                    quoted = !quoted;
                }
            } else if (ch == ',' && !quoted) {
                fields.add(field.toString());
                field.setLength(0);
            } else {
                field.append(ch);
            }
        }
        if (quoted) return null;
        fields.add(field.toString());
        return fields.toArray(new String[0]);
    }

    public void add(String passId, String regNo, String name, int route, double amount)
            throws IOException {
        if (passId == null || passId.isBlank() || regNo == null || regNo.isBlank()
                || name == null || name.isBlank() || route < 0 || amount < 0) {
            throw new IllegalArgumentException("Invalid bus-pass data");
        }
        Path parent = file.toAbsolutePath().getParent();
        if (parent != null) Files.createDirectories(parent);
        try (BufferedWriter w = Files.newBufferedWriter(file, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            w.write(encode(passId, regNo, name, Integer.toString(route),
                    Double.toString(amount)));
            w.newLine();
        }
    }

    /** Returns five fields for the given ID, or null if not found. */
    public String[] find(String passId) throws IOException {
        if (!Files.exists(file)) return null;
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] fields = parse(line);
                if (fields == null || fields.length != 5) continue;
                try {
                    Integer.parseInt(fields[3]);
                    double amount = Double.parseDouble(fields[4]);
                    if (!Double.isFinite(amount) || amount < 0) continue;
                } catch (NumberFormatException ex) {
                    continue;
                }
                if (fields[0].equals(passId)) return fields;
            }
        }
        return null;
    }

    public List<String[]> listByRoute(int route) throws IOException {
        List<String[]> results = new ArrayList<>();
        if (!Files.exists(file)) return results;
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] fields = parse(line);
                if (fields == null || fields.length != 5) continue;
                try {
                    int storedRoute = Integer.parseInt(fields[3]);
                    double amount = Double.parseDouble(fields[4]);
                    if (Double.isFinite(amount) && amount >= 0 && storedRoute == route) {
                        results.add(fields);
                    }
                } catch (NumberFormatException ignored) {
                    // Skip damaged records.
                }
            }
        }
        return results;
    }

    public static void main(String[] args) throws IOException {
        Path f = Files.createTempFile("passes", ".csv");
        PassStore store = new PassStore(f);
        store.add("P1001", "192311001", "Arun", 12, 4500);
        Files.writeString(f, "P1002,DAMAGED LINE\n", StandardOpenOption.APPEND);
        store.add("P1003", "192311003", "Divya", 7, 3800);
        System.out.println(java.util.Arrays.toString(store.find("P1003")));
        System.out.println(java.util.Arrays.toString(store.find("P1002")));
        System.out.println("Route 7 count: " + store.listByRoute(7).size());
        Files.deleteIfExists(f);
    }
}
