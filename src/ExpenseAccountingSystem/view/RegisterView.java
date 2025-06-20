package ExpenseAccountingSystem.view;

import ExpenseAccountingSystem.dao.UserDAO;
import ExpenseAccountingSystem.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class RegisterView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private UserDAO userDAO;

    public RegisterView() {
        userDAO = new UserDAO();
        initUI();
    }

    private void initUI() {
        setTitle("用户注册");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // 用户名标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("用户名:"), gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        add(usernameField, gbc);

        // 密码标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("密码:"), gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // 确认密码标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("确认密码:"), gbc);

        confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(confirmPasswordField, gbc);

        // 注册按钮
        JButton registerBtn = new JButton("注册");
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(registerBtn, gbc);

        // 返回登录按钮
        JButton backBtn = new JButton("返回登录");
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginView().setVisible(true);
                dispose();
            }
        });
        gbc.gridy = 4;
        add(backBtn, gbc);
    }

    private void register() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        // 输入验证
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "所有字段不能为空");
            return;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "两次密码输入不一致");
            return;
        }

        try {
            User user = new User(username, password);
            boolean success = userDAO.registerUser(user);
            if (success) {
                JOptionPane.showMessageDialog(this, "注册成功！请登录");
                new LoginView().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "注册失败，请重试");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "注册失败：" + ex.getMessage());
            ex.printStackTrace();
        }
    }
}