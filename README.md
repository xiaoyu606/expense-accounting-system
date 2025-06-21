# Java课设——智能记账管理系统

本项目是 Java 课程设计，基于 Java Swing 图形界面 和 MySQL 数据库实现的桌面端记账管理系统。

系统具备清晰的模块划分和完整的业务流程，涵盖了用户管理、账本管理、交易记录管理和权限控制等核心功能

通过该项目，实现了对数据库操作、Swing 界面交互、角色权限控制等内容的综合实践应用，具有良好的可读性与扩展性， 适合作为 Java 初中级课程的教学示例或课设参考项目。
## 📌功能特点

- **用户管理**：
   - 支持管理员添加、删除用户。
   - 用户可自行注册并修改密码。
   - 管理员账户不可被删除。

- **账本管理**：
   - 管理员可创建、编辑、删除账本。
   - 普通用户只能查看自己所属的账本。

- **交易记录**：
   - 管理员可添加、编辑、删除交易记录。
   - 用户仅能查看属于自己的交易记录。

- **权限控制**：
   - 区分管理员与普通用户权限。
   - 管理员拥有完整的系统操作权限。

## 🧱技术栈

- **编程语言**：Java
- **UI框架**：Swing
- **数据库**：MySQL
- **数据库连接**：JDBC
- **开发工具**：IntelliJ IDEA

## 🛠️安装步骤

### 前提条件
- Java JDK 8或更高版本
- MySQL 5.7或更高版本

### 数据库设置
1. 打开MySQL命令行或客户端
2. 执行sql目录下的数据库脚本：
   ```sql
   source expense-accounting-system\sql\expense-accountingDB.sql;
   source expense-accounting-system\sql\测试数据.sql;
   ```
3. （可选）执行视图脚本：
   ```sql
   source expense-accounting-system\sql\视图.sql;
   ```

### 配置数据库连接
修改`src\ExpenseAccountingSystem\model\DatabaseConnect.java`文件中的数据库连接信息：
```java
private static final String URL = "jdbc:mysql://localhost:3306/expense-accounting-system?useSSL=false&serverTimezone=Asia/Shanghai";
private static final String USER = "root";
private static final String PASSWORD = "0325"; // 修改为你的数据库密码
```

### 编译和运行
1. 使用IDE打开项目
2. 添加MySQL Connector依赖
3. 运行`src\ExpenseAccountingSystem\test.java`文件启动应用

## 使用说明

### 登录
- 初始管理员账户和密码：`admin`/`0325`
- 普通用户可通过注册功能创建账户

### 主要功能

- **用户管理**（仅限管理员）：
   - 添加新用户。
   - 删除现有用户（除管理员外）。
   - 修改任意用户的密码。

- **账本管理**（仅限管理员）：
   - 创建账本并指定所有者。
   - 编辑已有账本名称。
   - 删除账本。

- **交易记录**（仅限管理员）：
   - 添加交易记录，包括账本ID、类别、金额、时间、描述。
   - 编辑或删除已有交易记录。

- **个人信息管理**：
   - 用户可更改自己的密码。
   - 支持切换登录账号。

## 📂项目结构

```
expense-accounting-system/
├── sql/                  # 数据库脚本文件
│   ├── expense-accountingDB.sql  # 创建数据库和表
│   ├── 测试数据.sql              # 插入初始测试数据
│   └── 视图.sql                  # 可选视图定义
├── src/ExpenseAccountingSystem/
│   ├── dao/              # 数据访问对象
│   │   ├── AccountBookDAO.java
│   │   ├── TransactionDAO.java
│   │   └── UserDAO.java
│   ├── model/            # 数据模型和数据库连接
│   │   ├── AccountBook.java
│   │   ├── DatabaseConnect.java
│   │   ├── Transaction.java
│   │   └── User.java
│   ├── view/             # UI视图
│   │   ├── LoginView.java
│   │   ├── MainView.java
│   │   └── RegisterView.java
│   └── test.java         # 程序入口
└── README.md             # 项目说明文档
```

## 📖许可证

本项目采用MIT许可证 - 详情参见LICENSE文件

## ⚠️注意事项
- 修改`model\DatabaseConnect.java`中的数据库连接配置以匹配本地环境。
- 运行前请**务必确保**MySQL服务已启动
- 首次运行程序时，**请务必**先执行`sql/expense-accountingDB.sql`以创建数据库结构。
- 如需快速开始测试，请执行`sql/测试数据.sql`插入默认数据。
- 若需扩展查询功能，可选择执行`sql/视图.sql`创建视图。
- 在程序中，**管理员账户无法被删除**