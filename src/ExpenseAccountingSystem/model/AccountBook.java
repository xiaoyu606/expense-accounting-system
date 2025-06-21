package ExpenseAccountingSystem.model;

public class AccountBook {
    private int bookId;
    private int ownerId;
    private String bookName;

    public AccountBook() {}

    public AccountBook(int ownerId, String bookName) {
        this.ownerId = ownerId;
        this.bookName = bookName;
    }

    public AccountBook(int bookId, int ownerId, String bookName) {
        this.bookId = bookId;
        this.ownerId = ownerId;
        this.bookName = bookName;
    }

    // Getters and setters
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }
    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }
}