package ExpenseAccountingSystem.view;

import ExpenseAccountingSystem.model.User;
import ExpenseAccountingSystem.model.Transaction;
import ExpenseAccountingSystem.model.AccountBook;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ExpenseAccountingSystem.dao.TransactionDAO;
import ExpenseAccountingSystem.dao.AccountBookDAO;
import ExpenseAccountingSystem.dao.UserDAO;
// import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.math.BigDecimal;
import java.sql.SQLException;

public class MainView extends JFrame {
    private User currentUser;
    private boolean isAdmin;

    public MainView(User user) {
        this.currentUser = user;
        this.isAdmin = "admin".equals(user.getUsername());
        initUI();
    }

    private void initUI() {
        setTitle("智能记账管理系统 - " + currentUser.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();

        // User management menu (only for admin)
        if (isAdmin) {
            JMenu userMenu = new JMenu("用户管理");
            JMenuItem addUserItem = new JMenuItem("添加用户");
            JMenuItem deleteUserItem = new JMenuItem("删除用户");
            JMenuItem editUserItem = new JMenuItem("修改密码");
            userMenu.add(addUserItem);
            userMenu.add(deleteUserItem);
            userMenu.add(editUserItem);
            menuBar.add(userMenu);

            // Add User action
            addUserItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JTextField usernameField = new JTextField(20);
                    JPasswordField passwordField = new JPasswordField(20);
                    JPanel panel = new JPanel(new GridLayout(2, 2));
                    panel.add(new JLabel("用户名:"));
                    panel.add(usernameField);
                    panel.add(new JLabel("密码:"));
                    panel.add(passwordField);

                    int result = JOptionPane.showConfirmDialog(MainView.this, panel, "添加新用户", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        String username = usernameField.getText().trim();
                        String password = new String(passwordField.getPassword()).trim();

                        if (username.isEmpty() || password.isEmpty()) {
                            JOptionPane.showMessageDialog(MainView.this, "用户名和密码不能为空");
                            return;
                        }

                        try {
                            UserDAO userDAO = new UserDAO();
                            boolean success = userDAO.registerUser(new User(username, password));
                            if (success) {
                                JOptionPane.showMessageDialog(MainView.this, "用户添加成功");
                            } else {
                                JOptionPane.showMessageDialog(MainView.this, "添加用户失败");
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(MainView.this, "Error: " + ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                }
            });

            // Delete User action
            deleteUserItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        UserDAO userDAO = new UserDAO();
                        List<User> users = userDAO.getAllUsers();
                        List<String> usernames = new ArrayList<>();
                        for (User user : users) {
                            if (!"admin".equals(user.getUsername())) {
                                usernames.add(user.getUsername());
                            }
                        }

                        if (usernames.isEmpty()) {
                            JOptionPane.showMessageDialog(MainView.this, "No users to delete");
                            return;
                        }

                        String selectedUser = (String) JOptionPane.showInputDialog(
                            MainView.this,
                            "选择要删除的用户:",
                            "删除用户",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            usernames.toArray(),
                            usernames.get(0)
                        );

                        if (selectedUser != null) {
                            int confirm = JOptionPane.showConfirmDialog(MainView.this, "Are you sure you want to delete " + selectedUser + "?");
                            if (confirm == JOptionPane.YES_OPTION) {
                                boolean success = userDAO.deleteUser(selectedUser);
                                if (success) {
                                    JOptionPane.showMessageDialog(MainView.this, "用户删除成功");
                                } else {
                                    JOptionPane.showMessageDialog(MainView.this, "删除用户失败");
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(MainView.this, "Error: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            });

            // Edit User Password action
            editUserItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        UserDAO userDAO = new UserDAO();
                        List<User> users = userDAO.getAllUsers();
                        List<String> usernames = new ArrayList<>();
                        for (User user : users) {
                            usernames.add(user.getUsername());
                        }

                        String selectedUser = (String) JOptionPane.showInputDialog(
                            MainView.this,
                            "选择要编辑的用户:",
                            "编辑用户密码",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            usernames.toArray(),
                            usernames.get(0)
                        );

                        if (selectedUser != null) {
                            JPasswordField newPasswordField = new JPasswordField(20);
                            JPanel panel = new JPanel(new GridLayout(1, 2));
                            panel.add(new JLabel("新密码:"));
                            panel.add(newPasswordField);

                            int result = JOptionPane.showConfirmDialog(MainView.this, panel, "设置新密码", JOptionPane.OK_CANCEL_OPTION);
                            if (result == JOptionPane.OK_OPTION) {
                                String newPassword = new String(newPasswordField.getPassword()).trim();
                                if (newPassword.isEmpty()) {
                                    JOptionPane.showMessageDialog(MainView.this, "密码不能为空");
                                    return;
                                }

                                boolean success = userDAO.updatePassword(selectedUser, newPassword);
                                if (success) {
                                    JOptionPane.showMessageDialog(MainView.this, "密码更新成功");
                                } else {
                                    JOptionPane.showMessageDialog(MainView.this, "密码更新失败");
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(MainView.this, "Error: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            });
        }

        // Account book menu
        JMenu bookMenu = new JMenu("账本");
        JMenuItem viewBooksItem = new JMenuItem("查看账本");
        if (isAdmin) {
            JMenuItem addBookItem = new JMenuItem("添加账本");
            JMenuItem editBookItem = new JMenuItem("编辑账本");
            JMenuItem deleteBookItem = new JMenuItem("删除账本");
            bookMenu.add(addBookItem);
            bookMenu.add(editBookItem);
            bookMenu.add(deleteBookItem);

            // Add Book action
            addBookItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        // Get all users for owner selection
                        UserDAO userDAO = new UserDAO();
                        List<User> users = userDAO.getAllUsers();
                        List<String> userOptions = new ArrayList<>();
                        for (User user : users) {
                            userOptions.add(user.getUsername() + " (ID: " + user.getId() + ")");
                        }

                        JTextField bookNameField = new JTextField(20);
                        String selectedUserOption = (String) JOptionPane.showInputDialog(
                            MainView.this,
                            "选择账本所有者:",
                            "添加新账本",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            userOptions.toArray(),
                            userOptions.get(0)
                        );
                        if (selectedUserOption != null) {
                            int ownerId = Integer.parseInt(selectedUserOption.split("ID: ")[1].replace(")", ""));
                            String bookName = JOptionPane.showInputDialog(MainView.this, "输入账本名称:");
                            if (bookName != null && !bookName.trim().isEmpty()) {
                                AccountBookDAO bookDAO = new AccountBookDAO();
                                boolean success = bookDAO.addBook(new AccountBook(ownerId, bookName.trim()));
                                if (success) {
                                    JOptionPane.showMessageDialog(MainView.this, "账本添加成功");
                                } else {
                                    JOptionPane.showMessageDialog(MainView.this, "账本添加失败");
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(MainView.this, "Error: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            });

            // Edit Book action
            editBookItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        AccountBookDAO bookDAO = new AccountBookDAO();
                        List<AccountBook> books = bookDAO.getBooks(currentUser);
                        if (books.isEmpty()) {
                            JOptionPane.showMessageDialog(MainView.this, "No books to edit");
                            return;
                        }

                        List<String> bookOptions = new ArrayList<>();
                        for (AccountBook book : books) {
                            bookOptions.add(book.getBookName() + " (ID: " + book.getBookId() + ")");
                        }

                        String selectedBookOption = (String) JOptionPane.showInputDialog(
                            MainView.this,
                            "选择要编辑的账本:",
                            "编辑账本",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            bookOptions.toArray(),
                            bookOptions.get(0)
                        );

                        if (selectedBookOption != null) {
                            int bookId = Integer.parseInt(selectedBookOption.split("ID: ")[1].replace(")", ""));
                            String newBookName = JOptionPane.showInputDialog(MainView.this, "输入新账本名称:");

                            if (newBookName != null && !newBookName.trim().isEmpty()) {
                                AccountBook book = new AccountBook(bookId, 0, newBookName.trim());
                                boolean success = bookDAO.updateBook(book);
                                if (success) {
                                    JOptionPane.showMessageDialog(MainView.this, "账本更新成功");
                                } else {
                                    JOptionPane.showMessageDialog(MainView.this, "账本更新失败");
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(MainView.this, "Error: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            });

            // Delete Book action
            deleteBookItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        AccountBookDAO bookDAO = new AccountBookDAO();
                        List<AccountBook> books = bookDAO.getBooks(currentUser);
                        if (books.isEmpty()) {
                            JOptionPane.showMessageDialog(MainView.this, "No books to delete");
                            return;
                        }

                        List<String> bookOptions = new ArrayList<>();
                        for (AccountBook book : books) {
                            bookOptions.add(book.getBookName() + " (ID: " + book.getBookId() + ")");
                        }

                        String selectedBookOption = (String) JOptionPane.showInputDialog(
                            MainView.this,
                            "选择要删除的账本:",
                            "删除账本",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            bookOptions.toArray(),
                            bookOptions.get(0)
                        );

                        if (selectedBookOption != null) {
                            int bookId = Integer.parseInt(selectedBookOption.split("ID: ")[1].replace(")", ""));
                            int confirm = JOptionPane.showConfirmDialog(MainView.this, "Are you sure you want to delete this book?");
                            if (confirm == JOptionPane.YES_OPTION) {
                                boolean success = bookDAO.deleteBook(bookId);
                                if (success) {
                                    JOptionPane.showMessageDialog(MainView.this, "账本删除成功");
                                } else {
                                    JOptionPane.showMessageDialog(MainView.this, "Failed to delete book");
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(MainView.this, "Error: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            });
        }
        bookMenu.add(viewBooksItem);
        menuBar.add(bookMenu);

        // View Books action
        viewBooksItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AccountBookDAO bookDAO = new AccountBookDAO();
                    List<AccountBook> books = bookDAO.getBooks(currentUser);

                    String[] columnNames = {"账本ID", "所有者ID", "账本名称"};
                    Object[][] data = new Object[books.size()][3];

                    for (int i = 0; i < books.size(); i++) {
                        AccountBook book = books.get(i);
                        data[i][0] = book.getBookId();
                        data[i][1] = book.getOwnerId();
                        data[i][2] = book.getBookName();
                    }

                    JTable table = new JTable(data, columnNames);
                    JScrollPane scrollPane = new JScrollPane(table);
                    JFrame frame = new JFrame("Account Books");
                    frame.add(scrollPane);
                    frame.setSize(400, 300);
                    frame.setLocationRelativeTo(MainView.this);
                    frame.setVisible(true);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(MainView.this, "Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        // Transaction menu
        JMenu transactionMenu = new JMenu("交易记录");
        JMenuItem viewTransactionsItem = new JMenuItem("查看交易记录");
        viewTransactionsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    TransactionDAO transactionDAO = new TransactionDAO();
                    List<Transaction> transactions = transactionDAO.getTransactions(currentUser);
                    String[] columnNames = {"记录ID", "账本ID", "类别ID", "金额", "交易时间", "描述"};
                    Object[][] data = new Object[transactions.size()][6];
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    for (int i = 0; i < transactions.size(); i++) {
                        Transaction t = transactions.get(i);
                        data[i][0] = t.getRecordId();
                        data[i][1] = t.getBookId();
                        data[i][2] = t.getCategoryId();
                        data[i][3] = t.getAmount();
                        data[i][4] = sdf.format(t.getTransactionTime());
                        data[i][5] = t.getDescription();
                    }
                    JTable table = new JTable(data, columnNames);
                    JScrollPane scrollPane = new JScrollPane(table);
                    JFrame frame = new JFrame("交易记录列表");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.add(scrollPane);
                    frame.pack();
                    frame.setVisible(true);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(MainView.this, "错误: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
        if (isAdmin) {
            JMenuItem addTransactionItem = new JMenuItem("添加交易记录");
            JMenuItem editTransactionItem = new JMenuItem("编辑交易记录");
            JMenuItem deleteTransactionItem = new JMenuItem("删除交易记录");
            transactionMenu.add(addTransactionItem);
            transactionMenu.add(editTransactionItem);
            transactionMenu.add(deleteTransactionItem);

            // Add Transaction action
            addTransactionItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        AccountBookDAO bookDAO = new AccountBookDAO();
                        List<AccountBook> books = bookDAO.getBooks(currentUser);
                        if (books.isEmpty()) {
                            JOptionPane.showMessageDialog(MainView.this, "No books available to add transactions");
                            return;
                        }

                        List<String> bookOptions = new ArrayList<>();
                        for (AccountBook book : books) {
                            bookOptions.add(book.getBookName() + " (ID: " + book.getBookId() + ")");
                        }

                        String selectedBookOption = (String) JOptionPane.showInputDialog(
                            MainView.this,
                            "选择账本:",
                            "添加新交易记录",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            bookOptions.toArray(),
                            bookOptions.get(0)
                        );

                        if (selectedBookOption != null) {
                            int bookId = Integer.parseInt(selectedBookOption.split("ID: ")[1].replace(")", ""));
                            String categoryIdStr = JOptionPane.showInputDialog(MainView.this, "输入类别ID:");
                            String amountStr = JOptionPane.showInputDialog(MainView.this, "输入金额:");
                            String description = JOptionPane.showInputDialog(MainView.this, "输入描述:");

                            if (categoryIdStr == null || amountStr == null || description == null) return;

                            int categoryId;
                            BigDecimal amount;
                            try {
                                categoryId = Integer.parseInt(categoryIdStr.trim());
                                amount = new BigDecimal(amountStr.trim());
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(MainView.this, "Invalid category ID or amount");
                                return;
                            }

                            Transaction transaction = new Transaction(
                                bookId,
                                categoryId,
                                amount,
                                new Date(),
                                description.trim()
                            );

                            TransactionDAO transactionDAO = new TransactionDAO();
                            boolean success = transactionDAO.addTransaction(transaction);
                            if (success) {
                                JOptionPane.showMessageDialog(MainView.this, "交易记录添加成功");
                            } else {
                                JOptionPane.showMessageDialog(MainView.this, "交易记录添加失败");
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(MainView.this, "Error: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            });

            // Edit Transaction action
            editTransactionItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        TransactionDAO transactionDAO = new TransactionDAO();
                        List<Transaction> transactions = transactionDAO.getTransactions(currentUser);
                        if (transactions.isEmpty()) {
                            JOptionPane.showMessageDialog(MainView.this, "No transactions to edit");
                            return;
                        }

                        List<String> transactionOptions = new ArrayList<>();
                        for (Transaction t : transactions) {
                            transactionOptions.add(t.getRecordId() + ": " + t.getDescription() + " (" + t.getAmount() + ")");
                        }

                        String selectedTransactionOption = (String) JOptionPane.showInputDialog(
                            MainView.this,
                            "选择要编辑的交易记录:",
                            "编辑交易记录",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            transactionOptions.toArray(),
                            transactionOptions.get(0)
                        );

                        if (selectedTransactionOption != null) {
                            long recordId = Long.parseLong(selectedTransactionOption.split(":")[0]);
                            Transaction transaction = null;
                            for (Transaction t : transactions) {
                                if (t.getRecordId() == recordId) {
                                    transaction = t;
                                    break;
                                }
                            }

                            if (transaction != null) {
                                String categoryIdStr = JOptionPane.showInputDialog(MainView.this, "输入新类别ID:", transaction.getCategoryId());
                                String amountStr = JOptionPane.showInputDialog(MainView.this, "输入新金额:", transaction.getAmount());
                                String description = JOptionPane.showInputDialog(MainView.this, "输入新描述:", transaction.getDescription());

                                if (categoryIdStr == null || amountStr == null || description == null) return;

                                int categoryId;
                                BigDecimal amount;
                                try {
                                    categoryId = Integer.parseInt(categoryIdStr.trim());
                                    amount = new BigDecimal(amountStr.trim());
                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(MainView.this, "Invalid category ID or amount");
                                    return;
                                }

                                transaction.setCategoryId(categoryId);
                                transaction.setAmount(amount);
                                transaction.setDescription(description.trim());
                                transaction.setTransactionTime(new Date());

                                boolean success = transactionDAO.updateTransaction(transaction);
                                if (success) {
                                    JOptionPane.showMessageDialog(MainView.this, "交易记录更新成功");
                                } else {
                                    JOptionPane.showMessageDialog(MainView.this, "交易记录更新失败");
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(MainView.this, "Error: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            });

            // Delete Transaction action
            deleteTransactionItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        TransactionDAO transactionDAO = new TransactionDAO();
                        List<Transaction> transactions = transactionDAO.getTransactions(currentUser);
                        if (transactions.isEmpty()) {
                            JOptionPane.showMessageDialog(MainView.this, "No transactions to delete");
                            return;
                        }

                        List<String> transactionOptions = new ArrayList<>();
                        for (Transaction t : transactions) {
                            transactionOptions.add(t.getRecordId() + ": " + t.getDescription() + " (" + t.getAmount() + ")");
                        }

                        String selectedTransactionOption = (String) JOptionPane.showInputDialog(
                            MainView.this,
                            "选择要删除的交易记录:",
                            "删除交易记录",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            transactionOptions.toArray(),
                            transactionOptions.get(0)
                        );

                        if (selectedTransactionOption != null) {
                            long recordId = Long.parseLong(selectedTransactionOption.split(":")[0]);
                            int confirm = JOptionPane.showConfirmDialog(MainView.this, "Are you sure you want to delete this transaction?");
                            if (confirm == JOptionPane.YES_OPTION) {
                                boolean success = transactionDAO.deleteTransaction(recordId);
                                if (success) {
                                    JOptionPane.showMessageDialog(MainView.this, "交易记录删除成功");
                                } else {
                                    JOptionPane.showMessageDialog(MainView.this, "交易记录删除失败");
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(MainView.this, "Error: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            });
        }
        transactionMenu.add(viewTransactionsItem);
        menuBar.add(transactionMenu);

        // User profile menu
        JMenu profileMenu = new JMenu("账户");
        JMenuItem editPasswordItem = new JMenuItem("更改密码");
        JMenuItem switchAccountItem = new JMenuItem("切换用户");
        profileMenu.add(editPasswordItem);
        profileMenu.add(switchAccountItem);
        menuBar.add(profileMenu);

        // Add password change functionality
        editPasswordItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPasswordField newPasswordField = new JPasswordField(20);
                JPasswordField confirmPasswordField = new JPasswordField(20);
                JPanel panel = new JPanel(new GridLayout(2, 2));
                panel.add(new JLabel("新密码:"));
                panel.add(newPasswordField);
                panel.add(new JLabel("请二次输入以确认:"));
                panel.add(confirmPasswordField);

                int result = JOptionPane.showConfirmDialog(MainView.this, panel, "Change Password", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String newPassword = new String(newPasswordField.getPassword());
                    String confirmPassword = new String(confirmPasswordField.getPassword());

                    if (!newPassword.equals(confirmPassword)) {
                        JOptionPane.showMessageDialog(MainView.this, "Passwords do not match");
                        return;
                    }

                    if (newPassword.isEmpty()) {
                        JOptionPane.showMessageDialog(MainView.this, "密码不能为空");
                        return;
                    }

                    try {
                        UserDAO userDAO = new UserDAO();
                        boolean success = userDAO.updatePassword(currentUser.getUsername(), newPassword);
                        if (success) {
                            JOptionPane.showMessageDialog(MainView.this, "密码更新成功");
                            currentUser.setPassword(newPassword);
                        } else {
                            JOptionPane.showMessageDialog(MainView.this, "密码更新失败");
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(MainView.this, "Error: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        });

        setJMenuBar(menuBar);

        // Welcome panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("欢迎, " + currentUser.getUsername() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("", Font.BOLD, 24));
        panel.add(welcomeLabel, BorderLayout.CENTER);
        add(panel);

        // Switch account action
        switchAccountItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginView().setVisible(true);
                dispose();
            }
        });
    }
}