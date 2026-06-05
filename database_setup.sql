-- =============================================
-- JDBC Banking System - Database Setup Script
-- Author: Gonuguntala Jaikar Ramu
-- =============================================

-- Step 1: Create the database
CREATE DATABASE IF NOT EXISTS banking_db;

-- Step 2: Use the database
USE banking_db;

-- Step 3: Create accounts table
CREATE TABLE IF NOT EXISTS accounts (
    account_id    INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100)   NOT NULL,
    email         VARCHAR(100)   NOT NULL,
    account_type  VARCHAR(20)    NOT NULL DEFAULT 'SAVINGS',
    balance       DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    created_at    TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);

-- Step 5: Verify data
SELECT * FROM accounts;
