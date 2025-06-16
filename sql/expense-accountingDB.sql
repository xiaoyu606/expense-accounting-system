USE `expense-accounting-system`;

-- 用户表 (移除非必要的 updated_at)
CREATE TABLE user_info (
    user_id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 账本表 (保留核心关联字段)
CREATE TABLE account_book (
    book_id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    owner_id INT UNSIGNED NOT NULL,
    book_name VARCHAR(100) NOT NULL DEFAULT '默认账本',
    FOREIGN KEY (owner_id) REFERENCES user_info(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 分类表 (移除非必要的级联外键，保持层级)
CREATE TABLE category_dict (
    category_id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    parent_id INT UNSIGNED DEFAULT NULL,
    category_name VARCHAR(50) NOT NULL,
    is_income TINYINT(1) NOT NULL DEFAULT 0 COMMENT '0=支出,1=收入'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 交易记录表 (移除非必要的 created_at)
CREATE TABLE transaction_records (
    record_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    book_id INT UNSIGNED NOT NULL,
    category_id INT UNSIGNED NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    transaction_time DATETIME NOT NULL,
    description VARCHAR(200),
    FOREIGN KEY (book_id) REFERENCES account_book(book_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES category_dict(category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 预算表 (移除非必要的 created_at)
CREATE TABLE budget_plan (
    plan_id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    book_id INT UNSIGNED NOT NULL,
    category_id INT UNSIGNED NOT NULL,
    budget_amount DECIMAL(12,2) NOT NULL,
    month CHAR(7) NOT NULL COMMENT '格式:YYYY-MM',
    UNIQUE (book_id, category_id, month),
    FOREIGN KEY (book_id) REFERENCES account_book(book_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES category_dict(category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;