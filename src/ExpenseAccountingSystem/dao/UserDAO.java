package ExpenseAccountingSystem.dao;

import ExpenseAccountingSystem.model.DatabaseConnect;
import ExpenseAccountingSystem.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class UserDAO {
    // 用户注册
    public boolean registerUser(User user) throws SQLException {
        String sql = "INSERT INTO user_info (username, password) VALUES (?, ?)";
        try (Connection conn = DatabaseConnect.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            return pstmt.executeUpdate() > 0;
        }
    }

    // 用户登录
    public User loginUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM user_info WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnect.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password"));
            }
        }
        return null;
    }

    // Admin: Delete user
    public boolean deleteUser(String username) throws SQLException {
        if ("admin".equals(username)) {
            throw new SQLException("Cannot delete admin user");
        }
        String sql = "DELETE FROM user_info WHERE username = ?";
        try (Connection conn = DatabaseConnect.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Update password (admin can update any user, user can update own)
    public boolean updatePassword(String username, String newPassword) throws SQLException {
        String sql = "UPDATE user_info SET password = ? WHERE username = ?";
        try (Connection conn = DatabaseConnect.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Admin: Get all users
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user_info";
        try (Connection conn = DatabaseConnect.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password")));
            }
        }
        return users;
    }
}