-- ============================================================
-- Script: alter_tables.sql
-- Muc dich: Bo sung cac truong con thieu vao mbbank_db
-- Database: mbbank_db | User: root | Pass: (rong)
-- Chay: mysql -u root mbbank_db < alter_tables.sql
-- ============================================================

USE mbbank_db;

-- -------------------------------------------------------
-- 1. Bang branches: them ma_chi_nhanh
-- -------------------------------------------------------
ALTER TABLE branches
    ADD COLUMN IF NOT EXISTS ma_chi_nhanh VARCHAR(50) UNIQUE COMMENT 'Ma chi nhanh nghiep vu';

-- -------------------------------------------------------
-- 2. Bang employees: them ma_nv, bo_phan, luong
-- -------------------------------------------------------
ALTER TABLE employees
    ADD COLUMN IF NOT EXISTS ma_nv    VARCHAR(50) UNIQUE COMMENT 'Ma nhan vien nghiep vu',
    ADD COLUMN IF NOT EXISTS bo_phan  NVARCHAR(255)        COMMENT 'Bo phan cong tac',
    ADD COLUMN IF NOT EXISTS luong    DECIMAL(18,2)        COMMENT 'Luong (VND)';

-- -------------------------------------------------------
-- 3. Bang customers: them ma_kh, ngay_sinh
-- -------------------------------------------------------
ALTER TABLE customers
    ADD COLUMN IF NOT EXISTS ma_kh      VARCHAR(50) UNIQUE COMMENT 'Ma khach hang nghiep vu',
    ADD COLUMN IF NOT EXISTS ngay_sinh  DATE               COMMENT 'Ngay sinh';

-- -------------------------------------------------------
-- 4. Bang accounts: them loai_tk, ngay_mo
-- -------------------------------------------------------
ALTER TABLE accounts
    ADD COLUMN IF NOT EXISTS loai_tk  VARCHAR(50)  DEFAULT 'THANH_TOAN' COMMENT 'Loai tai khoan: THANH_TOAN, TIET_KIEM...',
    ADD COLUMN IF NOT EXISTS ngay_mo  DATE                              COMMENT 'Ngay mo tai khoan';

-- Cap nhat ngay_mo = CURDATE() cho cac ban ghi hien co con NULL
UPDATE accounts SET ngay_mo = CURDATE() WHERE ngay_mo IS NULL;

-- -------------------------------------------------------
-- 5. Bang transactions: them ma_gd
-- -------------------------------------------------------
ALTER TABLE transactions
    ADD COLUMN IF NOT EXISTS ma_gd VARCHAR(100) UNIQUE COMMENT 'Ma giao dich nghiep vu (tu dong sinh)';

-- -------------------------------------------------------
-- Ket qua: xem lai cau truc cac bang
-- -------------------------------------------------------
DESCRIBE branches;
DESCRIBE employees;
DESCRIBE customers;
DESCRIBE accounts;
DESCRIBE transactions;
