package ExpenseAccountingSystem;

import ExpenseAccountingSystem.view.LoginView;

public class test {
    public static void main(String[] args) {
        // 启动登录界面
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ExpenseAccountingSystem.view.LoginView loginView = new ExpenseAccountingSystem.view.LoginView();
                loginView.setVisible(true);
            }
        });
    }
}