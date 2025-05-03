-- 外部キー制約を一時的に無効化（MySQL）
SET FOREIGN_KEY_CHECKS = 0;

-- 既存のテーブルがあれば削除
DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS categories;

-- 外部キー制約を再有効化
SET FOREIGN_KEY_CHECKS = 1;

-- categories テーブル作成
CREATE TABLE categories (
  id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(256) NOT NULL,
  description TEXT
);

-- tasks テーブル作成
CREATE TABLE tasks (
  id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  category_id BIGINT NOT NULL,
  summary VARCHAR(256) NOT NULL,
  description TEXT,
  status VARCHAR(256) NOT NULL,
  CONSTRAINT fk_category
    FOREIGN KEY (category_id) REFERENCES categories(id)
    ON DELETE CASCADE
);
