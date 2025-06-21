package ExpenseAccountingSystem.view;

import ExpenseAccountingSystem.dao.UserDAO;
import ExpenseAccountingSystem.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserDAO userDAO;

    public LoginView() {
        userDAO = new UserDAO();
        initUI();
    }

    private void initUI() {
        setTitle("用户登录");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
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

        // 登录按钮
        JButton loginBtn = new JButton("登录");
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginBtn, gbc);

        // 注册按钮
        JButton registerBtn = new JButton("注册");
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterView().setVisible(true);
                dispose();
            }
        });
        gbc.gridy = 3;
        add(registerBtn, gbc);
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名和密码不能为空");
            return;
        }

        try {
            User user = userDAO.loginUser(username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "登录成功！");
                // 登录成功后跳转到主界面（此处需根据实际主界面类名修改）
                new MainView(user).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "用户名或密码错误");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "登录失败：" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginView().setVisible(true);
            }
        });
    }
}