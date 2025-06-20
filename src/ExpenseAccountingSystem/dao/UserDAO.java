package ExpenseAccountingSystem.dao;

import ExpenseAccountingSystem.model.DatabaseConnect;
import ExpenseAccountingSystem.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                return new User(rs.getString("username"), rs.getString("password"));
            }
        }
        return null;
    }
}