/*
 * Demonstration template for a LOCAL assignment database only.
 * Create a users table with columns user_id and password_hash.
 * Never use this unsafe example in a real application.
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginSafetyDemo {
    // Unsafe example: included only to compare the design; do not deploy.
    static boolean unsafeLogin(Connection c, String id, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id='" + id
                + "' AND password_hash='" + password + "'";
        try (var st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return rs.next();
        }
    }

    static boolean safeLogin(Connection c, String id, String passwordHash)
            throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id=? AND password_hash=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, passwordHash);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
