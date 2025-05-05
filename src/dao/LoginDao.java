package dao;

import java.sql.*;

public class LoginDao {
    private static final String URL = "jdbc:mysql://101.37.17.111:3306/CerebralStroke?useSSL=false&serverTimezone=UTC";
    private static final String USER = "zxt";
    private static final String PASSWORD = "123456";

    public boolean authenticate(String username, String password) {
        String sql = "SELECT 1 FROM Doctor WHERE user_name = ? AND password = ? LIMIT 1";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("数据库操作失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}