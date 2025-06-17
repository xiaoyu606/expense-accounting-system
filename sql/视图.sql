-- 交易记录详情视图，包含账本和分类信息
CREATE VIEW transaction_details AS
SELECT 
    tr.record_id,
    tr.book_id,
    ab.book_name,
    tr.category_id,
    cd.category_name AS category,
    cd.is_income,
    tr.amount,
    tr.transaction_time,
    tr.description,
    ui.username AS owner
FROM transaction_records tr
JOIN account_book ab ON tr.book_id = ab.book_id
JOIN category_dict cd ON tr.category_id = cd.category_id
JOIN user_info ui ON ab.owner_id = ui.user_id;

-- 按账本和分类统计的收入视图
CREATE VIEW income_by_category AS
SELECT 
    book_id,
    book_name,
    category_id,
    category,
    SUM(amount) AS total_income
FROM transaction_details
WHERE is_income = 1
GROUP BY book_id, book_name, category_id, category;

-- 按账本和分类统计的支出视图
CREATE VIEW expense_by_category AS
SELECT 
    book_id,
    book_name,
    category_id,
    category,
    SUM(amount) AS total_expense
FROM transaction_details
WHERE is_income = 0
GROUP BY book_id, book_name, category_id, category;

-- 月度支出统计视图
CREATE VIEW monthly_expense AS
SELECT 
    book_id,
    book_name,
    DATE_FORMAT(transaction_time, '%Y-%m') AS month,
    SUM(amount) AS total_expense
FROM transaction_details
WHERE is_income = 0
GROUP BY book_id, book_name, month;