package ExpenseAccountingSystem.model;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    private long recordId;
    private int bookId;
    private int categoryId;
    private BigDecimal amount;
    private Date transactionTime;
    private String description;

    public Transaction() {}

    public Transaction(int bookId, int categoryId, BigDecimal amount, Date transactionTime, String description) {
        this.bookId = bookId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.transactionTime = transactionTime;
        this.description = description;
    }

    public Transaction(long recordId, int bookId, int categoryId, BigDecimal amount, Date transactionTime, String description) {
        this.recordId = recordId;
        this.bookId = bookId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.transactionTime = transactionTime;
        this.description = description;
    }

    // Getters and setters
    public long getRecordId() { return recordId; }
    public void setRecordId(long recordId) { this.recordId = recordId; }
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Date getTransactionTime() { return transactionTime; }
    public void setTransactionTime(Date transactionTime) { this.transactionTime = transactionTime; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}