package ExpenseAccountingSystem.model;

import java.sql.DriverManager; // 表示数据库的连接会话
import java.sql.SQLException; // 表示用于获取数据库连接的工厂类
import java.sql.Connection; // 表示处理数据库操作中可能出现的异常

/**
 * DatabaseConnection表示用于链接MySQL数据库的工具集
 */
public class DatabaseConnect {
    /**
     * 数据库连接配置
     * 1. URL：表示数据库连接的地址，格式如下：jdbc:mysql://主机名:端口号/数据库鸣潮
     * 2. USER：表述数据库的用户名（如root）
     * 3. PASSWORD：表示数据库密码
     */

    // 此处引号内的字段需替换成你自己数据库的配置，否则会报错
    private static final String URL = "jdbc:mysql://localhost:3306/expense-accounting-system?useSSL=false&serverTimezone=Asia/Shanghai";
    private static final String USER = "root";
    private static final String PASSWORD = "0325";
    /**
     * 获取数据库连接的方法
     * 1. 异常处理问题：throws SQLException声明可能会抛出的SQL异常，调用者需处理异常
     * 2. DriverManager.getConnection()会根据URL自动加载MySQL驱动（需要提前添加依赖），
     *    并尝试建立与数据库的连接，成功后返回Connection对象，用户后续SQL操作，
     * 3. throw和throws的区别：throw表示主动抛出异常，而throws表示在定义方法时提示可嫩会出现某种异常
     */
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }
}