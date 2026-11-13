-- =============================================================================
-- Campus Facility Booking Database Setup & Initial Seed Data
-- Run this script in MySQL Workbench to prepare your database.
-- =============================================================================

CREATE DATABASE IF NOT EXISTS CampusFacilityBookingDB;
USE CampusFacilityBookingDB;

-- 1. Departments
CREATE TABLE IF NOT EXISTS department (
    department_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    building VARCHAR(255),
    head_of_department VARCHAR(255)
);

-- 2. Users
CREATE TABLE IF NOT EXISTS user (
    user_id VARCHAR(50) PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    date_of_birth DATE,
    department_id VARCHAR(50)
);

-- 3. Register
CREATE TABLE IF NOT EXISTS register (
    registrar_id VARCHAR(50) PRIMARY KEY,
    email VARCHAR(150) NOT NULL,
    date_registered DATE NOT NULL
);

-- 4. Login Credentials
CREATE TABLE IF NOT EXISTS login (
    login_id VARCHAR(50) PRIMARY KEY,
    registrar_id VARCHAR(50) NOT NULL,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- 5. User Roles
CREATE TABLE IF NOT EXISTS user_role (
    user_role_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- 6. Students
CREATE TABLE IF NOT EXISTS student (
    student_id VARCHAR(50) PRIMARY KEY,
    student_number VARCHAR(50) NOT NULL,
    user_id VARCHAR(50) NOT NULL
);

-- 7. Staff
CREATE TABLE IF NOT EXISTS staff (
    staff_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL
);

-- 8. Facilities
CREATE TABLE IF NOT EXISTS facility (
    facility_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    location VARCHAR(255) NOT NULL,
    department_id VARCHAR(50),
    facility_type VARCHAR(50) NOT NULL
);

-- 9. Time Slots
CREATE TABLE IF NOT EXISTS time_slot (
    time_slot_id VARCHAR(50) PRIMARY KEY,
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
);

-- 10. Equipment
CREATE TABLE IF NOT EXISTS equipment (
    equipment_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    serial_number VARCHAR(100),
    facility_id VARCHAR(50),
    equipment_status VARCHAR(50) NOT NULL
);

-- 11. Bookings
CREATE TABLE IF NOT EXISTS booking (
    booking_id VARCHAR(50) PRIMARY KEY,
    facility_id VARCHAR(50) NOT NULL,
    time_slot_id VARCHAR(50) NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    purpose TEXT NOT NULL,
    booking_status VARCHAR(50) NOT NULL
);

-- 12. Invoices
CREATE TABLE IF NOT EXISTS invoice (
    invoice_id VARCHAR(50) PRIMARY KEY,
    amount DOUBLE NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    paid BOOLEAN DEFAULT FALSE,
    booking_booking_id VARCHAR(50)
);

-- 13. Maintenance Requests
CREATE TABLE IF NOT EXISTS maintenance_request (
    request_id VARCHAR(50) PRIMARY KEY,
    equipment_id VARCHAR(50) NOT NULL,
    reported_by VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    date_reported DATE NOT NULL,
    maintenance_priority VARCHAR(50) NOT NULL,
    maintenance_status VARCHAR(50) NOT NULL
);

-- 14. Notifications
CREATE TABLE IF NOT EXISTS notification (
    notification_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    sent_date DATE NOT NULL,
    notification_type VARCHAR(50) NOT NULL
);

-- =============================================================================
-- SEED INITIAL DATA (Idempotent: Uses INSERT IGNORE)
-- =============================================================================

-- Departments
INSERT IGNORE INTO department (department_id, name, building, head_of_department) VALUES
('D001', 'Information and Communications Technology', 'Engineering & IT Building', 'Prof. D. Naidoo'),
('D002', 'Business & Management Sciences', 'Commerce Hall', 'Dr. S. Khumalo'),
('D003', 'Engineering & Built Environment', 'Mechanical Wing', 'Dr. A. Peters'),
('D007', 'Campus Administration', 'Administration Building', 'Campus Admin');

-- Users
INSERT IGNORE INTO user (user_id, first_name, last_name, email, date_of_birth, department_id) VALUES
('USR-ADMIN-1', 'Campus', 'Administrator', 'admin@cput.ac.za', '1985-05-15', 'D007'),
('USR-STUDENT-1', 'John', 'Doe', 'student@mycput.ac.za', '2002-03-20', 'D001');

-- Registers
INSERT IGNORE INTO register (registrar_id, email, date_registered) VALUES
('USR-ADMIN-1', 'admin@cput.ac.za', '2024-01-01'),
('USR-STUDENT-1', 'student@mycput.ac.za', '2024-02-10');

-- Logins (admin / admin123 and student / student123)
INSERT IGNORE INTO login (login_id, registrar_id, username, password) VALUES
('LOG-ADMIN-1', 'USR-ADMIN-1', 'admin', 'admin123'),
('LOG-STUDENT-1', 'USR-STUDENT-1', 'student', 'student123');

-- Roles
INSERT IGNORE INTO user_role (user_role_id, user_id, role) VALUES
('ROL-ADMIN-1', 'USR-ADMIN-1', 'ADMIN'),
('ROL-STUDENT-1', 'USR-STUDENT-1', 'STUDENT');

-- Student / Staff link
INSERT IGNORE INTO student (student_id, student_number, user_id) VALUES
('STU-001', '221009845', 'USR-STUDENT-1');

INSERT IGNORE INTO staff (staff_id, user_id) VALUES
('STF-001', 'USR-ADMIN-1');

-- Facilities
INSERT IGNORE INTO facility (facility_id, name, capacity, location, department_id, facility_type) VALUES
('FAC-001', 'Main Auditorium', 400, 'Campus Centre, Ground Floor', 'D007', 'AUDITORIUM'),
('FAC-002', 'ICT Computer Lab 3', 50, 'IT Building, 2nd Floor, Room 204', 'D001', 'COMPUTER_LAB'),
('FAC-003', 'Seminar Hall B', 80, 'Commerce Hall, Room 102', 'D002', 'LECTURE_HALL'),
('FAC-004', 'Engineering Workshop 1', 35, 'Mechanical Wing, Room 015', 'D003', 'LABORATORY');

-- Time Slots
INSERT IGNORE INTO time_slot (time_slot_id, date, start_time, end_time) VALUES
('TS-001', '2026-10-26', '08:00:00', '10:00:00'),
('TS-002', '2026-10-26', '10:00:00', '12:00:00'),
('TS-003', '2026-10-26', '12:00:00', '14:00:00'),
('TS-004', '2026-10-26', '14:00:00', '16:00:00'),
('TS-005', '2026-10-27', '09:00:00', '11:00:00');

-- Equipment
INSERT IGNORE INTO equipment (equipment_id, name, serial_number, facility_id, equipment_status) VALUES
('EQ-001', 'Epson 4K Laser Projector', 'EPS-98214', 'FAC-001', 'AVAILABLE'),
('EQ-002', 'Dell Precision Workstations (x20)', 'DELL-LAB-2024', 'FAC-002', 'AVAILABLE'),
('EQ-003', 'Shure Wireless Microphone System', 'SHURE-WM-04', 'FAC-001', 'AVAILABLE');

-- Bookings
INSERT IGNORE INTO booking (booking_id, facility_id, time_slot_id, user_id, purpose, booking_status) VALUES
('BK-001', 'FAC-002', 'TS-001', 'USR-STUDENT-1', 'Computer Science Society Coding Hackathon', 'APPROVED'),
('BK-002', 'FAC-001', 'TS-002', 'USR-STUDENT-1', 'Student Union General Council Meeting', 'PENDING');

-- Invoices
INSERT IGNORE INTO invoice (invoice_id, amount, issue_date, due_date, paid, booking_booking_id) VALUES
('INV-001', 250.00, '2026-10-20', '2026-11-05', FALSE, 'BK-001');

-- Notifications
INSERT IGNORE INTO notification (notification_id, user_id, message, sent_date, notification_type) VALUES
('NT-001', 'USR-STUDENT-1', 'Your booking for ICT Computer Lab 3 (BK-001) has been APPROVED.', '2026-10-23', 'BOOKING_CONFIRMATION'),
('NT-002', 'USR-STUDENT-1', 'Invoice INV-001 of R250.00 has been issued for your upcoming reservation.', '2026-10-23', 'INVOICE_REMINDER');
