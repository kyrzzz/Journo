package journo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class UserAuth {
    public int currentUserId;
    private String currentUsername; // To store the username of the logged-in user

    public boolean login(String username, String password) {
        String query = "SELECT user_id, username, password_hash FROM Users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                if (PasswordUtil.verifyPassword(password, storedHash)) {
                    currentUserId = rs.getInt("user_id");
                    currentUsername = rs.getString("username"); // Store the username
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean register(String username, String password, String email) {
        // Check if username or password is empty
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username and password cannot be empty.", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (usernameExists(username)) {
            JOptionPane.showMessageDialog(null, "Username already exists.", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false; // Username already exists
        }
        
        String hashedPassword = PasswordUtil.hashPassword(password);
        String query = "INSERT INTO Users (username, password_hash, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            
            // Handle null email
            if (email == null || email.isEmpty()) {
                stmt.setNull(3, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(3, email);
            }
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean usernameExists(String username) {
        String query = "SELECT user_id FROM Users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Username already exists in the database.");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getCurrentUsername() {
        return currentUsername; // Return the stored username
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public boolean isLoggedIn() {
        return currentUserId != 0;
    }

    public void logout() {
        currentUserId = 0; // Reset the session
        currentUsername = null; // Clear the username
    }

    // New method to check if the current user is an admin
    public boolean isAdmin() {
        return "admin".equals(currentUsername);
    }
}
