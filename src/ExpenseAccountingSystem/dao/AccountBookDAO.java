package ExpenseAccountingSystem.dao;

import ExpenseAccountingSystem.model.AccountBook;
import ExpenseAccountingSystem.model.DatabaseConnect;
import ExpenseAccountingSystem.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountBookDAO {
    // Get all books for admin or user's own books
    public List<AccountBook> getBooks(User user) throws SQLException {
        List<AccountBook> books = new ArrayList<>();
        String sql;

        if ("admin".equals(user.getUsername())) {
            sql = "SELECT * FROM account_book";
        } else {
            sql = "SELECT * FROM account_book WHERE owner_id = ?";
        }

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (!"admin".equals(user.getUsername())) {
                pstmt.setInt(1, user.getId());
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                books.add(new AccountBook(
                    rs.getInt("book_id"),
                    rs.getInt("owner_id"),
                    rs.getString("book_name")
                ));
            }
        }
        return books;
    }

    // Admin: Add new book
    public boolean addBook(AccountBook book) throws SQLException {
        String sql = "INSERT INTO account_book (owner_id, book_name) VALUES (?, ?)";
        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, book.getOwnerId());
            pstmt.setString(2, book.getBookName());
            return pstmt.executeUpdate() > 0;
        }
    }

    // Admin: Update book
    public boolean updateBook(AccountBook book) throws SQLException {
        String sql = "UPDATE account_book SET book_name = ? WHERE book_id = ?";
        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getBookName());
            pstmt.setInt(2, book.getBookId());
            return pstmt.executeUpdate() > 0;
        }
    }

    // Admin: Delete book
    public boolean deleteBook(int bookId) throws SQLException {
        String sql = "DELETE FROM account_book WHERE book_id = ?";
        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            return pstmt.executeUpdate() > 0;
        }
    }
}