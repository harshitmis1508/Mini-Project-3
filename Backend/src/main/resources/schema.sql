-- =====================================================
-- Department Expense Approval System - MySQL Schema
-- =====================================================

CREATE DATABASE IF NOT EXISTS expense_db;
USE expense_db;

DROP TABLE IF EXISTS expense_claim;
DROP TABLE IF EXISTS department_budget;
DROP TABLE IF EXISTS department;

CREATE TABLE department (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE department_budget (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    department_id BIGINT NOT NULL,
    budget_month INT NOT NULL,
    budget_year INT NOT NULL,
    budget_amount DECIMAL(15,2) NOT NULL,
    CONSTRAINT fk_budget_department FOREIGN KEY (department_id) REFERENCES department(id),
    CONSTRAINT uq_department_month_year UNIQUE (department_id, budget_month, budget_year)
);

CREATE TABLE expense_claim (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_name VARCHAR(100) NOT NULL,
    department_id BIGINT NOT NULL,
    category VARCHAR(100) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    expense_date DATE NOT NULL,
    description VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    review_remark VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_claim_department FOREIGN KEY (department_id) REFERENCES department(id)
);

-- Sample seed data
INSERT INTO department (name) VALUES ('Engineering'), ('Sales'), ('HR'), ('Finance');

INSERT INTO department_budget (department_id, budget_month, budget_year, budget_amount)
VALUES (1, 6, 2026, 500000), (2, 6, 2026, 300000), (3, 6, 2026, 150000), (4, 6, 2026, 200000);
