package ExpenseAccountingSystem.dao;

import ExpenseAccountingSystem.model.DatabaseConnect;
import ExpenseAccountingSystem.model.Transaction;
import ExpenseAccountingSystem.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class TransactionDAO {
    // Get transactions based on user role
    public List<Transaction> getTransactions(User user) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql;

        if ("admin".equals(user.getUsername())) {
            sql = "SELECT * FROM transaction_records ORDER BY transaction_time DESC";
        } else {
            sql = "SELECT tr.* FROM transaction_records tr JOIN account_book ab ON tr.book_id = ab.book_id WHERE ab.owner_id = ? ORDER BY transaction_time DESC";
        }

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (!"admin".equals(user.getUsername())) {
                pstmt.setInt(1, user.getId());
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                transactions.add(new Transaction(
                    rs.getLong("record_id"),
                    rs.getInt("book_id"),
                    rs.getInt("category_id"),
                    rs.getBigDecimal("amount"),
                    rs.getTimestamp("transaction_time"),
                    rs.getString("description")
                ));
            }
        }
        return transactions;
    }

    // Admin: Add new transaction
    public boolean addTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transaction_records (book_id, category_id, amount, transaction_time, description) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, transaction.getBookId());
            pstmt.setInt(2, transaction.getCategoryId());
            pstmt.setBigDecimal(3, transaction.getAmount());
            pstmt.setTimestamp(4, new Timestamp(transaction.getTransactionTime().getTime()));
            pstmt.setString(5, transaction.getDescription());
            return pstmt.executeUpdate() > 0;
        }
    }

    // Admin: Update transaction
    public boolean updateTransaction(Transaction transaction) throws SQLException {
        String sql = "UPDATE transaction_records SET book_id = ?, category_id = ?, amount = ?, transaction_time = ?, description = ? WHERE record_id = ?";
        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, transaction.getBookId());
            pstmt.setInt(2, transaction.getCategoryId());
            pstmt.setBigDecimal(3, transaction.getAmount());
            pstmt.setTimestamp(4, new Timestamp(transaction.getTransactionTime().getTime()));
            pstmt.setString(5, transaction.getDescription());
            pstmt.setLong(6, transaction.getRecordId());
            return pstmt.executeUpdate() > 0;
        }
    }

    // Admin: Delete transaction
    public boolean deleteTransaction(long recordId) throws SQLException {
        String sql = "DELETE FROM transaction_records WHERE record_id = ?";
        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, recordId);
            return pstmt.executeUpdate() > 0;
        }
    }
}