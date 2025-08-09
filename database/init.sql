-- Leave Management System Database Initialization Script

-- Create database
CREATE DATABASE leave_management;
\c leave_management;

-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'EMPLOYEE',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create employees table
CREATE TABLE employees (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    employee_id VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    department VARCHAR(50),
    position VARCHAR(50),
    hire_date DATE NOT NULL,
    manager_id BIGINT REFERENCES employees(id),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create leave_types table
CREATE TABLE leave_types (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    default_days INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create leave_requests table
CREATE TABLE leave_requests (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT REFERENCES employees(id),
    leave_type_id BIGINT REFERENCES leave_types(id),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    approved_by BIGINT REFERENCES employees(id),
    approved_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create leave_balances table
CREATE TABLE leave_balances (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT REFERENCES employees(id),
    leave_type_id BIGINT REFERENCES leave_types(id),
    total_days INTEGER DEFAULT 0,
    days_used INTEGER DEFAULT 0,
    days_remaining INTEGER DEFAULT 0,
    year INTEGER DEFAULT EXTRACT(YEAR FROM CURRENT_DATE),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(employee_id, leave_type_id, year)
);

-- Insert sample leave types
INSERT INTO leave_types (name, description, default_days) VALUES
('Annual Leave', 'Regular annual vacation leave', 20),
('Sick Leave', 'Medical and health-related leave', 10),
('Personal Leave', 'Personal and family matters', 5),
('Maternity Leave', 'Maternity and parental leave', 90),
('Paternity Leave', 'Paternity leave for new fathers', 14);

-- Insert sample admin user (password: admin123)
INSERT INTO users (username, password, email, first_name, last_name, role) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'admin@company.com', 'Admin', 'User', 'ADMIN');

-- Insert sample manager user (password: manager123)
INSERT INTO users (username, password, email, first_name, last_name, role) VALUES
('manager', '$2a$10$8K1p/a0dL1LXMIgoEDFrwOfgqwAGmQxH3gKxVxH3gKxVxH3gKxVxH', 'manager@company.com', 'Manager', 'User', 'MANAGER');

-- Insert sample employee user (password: employee123)
INSERT INTO users (username, password, email, first_name, last_name, role) VALUES
('employee', '$2a$10$8K1p/a0dL1LXMIgoEDFrwOfgqwAGmQxH3gKxVxH3gKxVxH3gKxVxH', 'employee@company.com', 'Employee', 'User', 'EMPLOYEE');

-- Insert sample employees
INSERT INTO employees (user_id, employee_id, first_name, last_name, email, phone, department, position, hire_date) VALUES
(1, 'EMP001', 'Admin', 'User', 'admin@company.com', '+1234567890', 'IT', 'System Administrator', '2023-01-01'),
(2, 'EMP002', 'Manager', 'User', 'manager@company.com', '+1234567891', 'HR', 'HR Manager', '2023-02-01'),
(3, 'EMP003', 'Employee', 'User', 'employee@company.com', '+1234567892', 'IT', 'Software Developer', '2023-03-01');

-- Insert sample leave balances
INSERT INTO leave_balances (employee_id, leave_type_id, total_days, days_used, days_remaining, year) VALUES
(1, 1, 25, 5, 20, 2024),
(1, 2, 10, 2, 8, 2024),
(2, 1, 28, 8, 20, 2024),
(2, 2, 10, 1, 9, 2024),
(3, 1, 20, 3, 17, 2024),
(3, 2, 10, 0, 10, 2024);

-- Create indexes for better performance
CREATE INDEX idx_leave_requests_employee_id ON leave_requests(employee_id);
CREATE INDEX idx_leave_requests_status ON leave_requests(status);
CREATE INDEX idx_leave_balances_employee_id ON leave_balances(employee_id);
CREATE INDEX idx_employees_user_id ON employees(user_id);
CREATE INDEX idx_employees_manager_id ON employees(manager_id);
