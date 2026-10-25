-- =========================================================
-- SAMPLE DATA - Campus Facility Booking System
--
-- Runs on every startup after Hibernate creates the tables.
-- INSERT IGNORE skips rows whose ID already exists, so this
-- never duplicates data or overwrites changes made in the app.
--
-- Demo logins (all passwords: Password123)
--   tmokoena  -> ADMIN    -> staff-dashboard.html
--   jvanwyk   -> LECTURER -> staff-dashboard.html
--   ndlamini  -> LECTURER -> staff-dashboard.html
--   sndlovu   -> STUDENT  -> student-dashboard.html
--   apatel    -> STUDENT  -> student-dashboard.html
--   lsmith    -> STUDENT  -> student-dashboard.html
-- =========================================================


-- ---------------------------------------------------------
-- DEPARTMENTS (IDs match the mapping in register.js)
-- ---------------------------------------------------------

INSERT IGNORE INTO department (department_id, name, building, head_of_department) VALUES
('D001', 'Information and Communication Technology', 'Engineering Building', 'Prof. L. Mahlangu'),
('D002', 'Business and Management Sciences', 'Commerce Building', 'Dr. R. Jacobs'),
('D003', 'Engineering and the Built Environment', 'Engineering Building', 'Prof. K. Naidoo'),
('D004', 'Education', 'Education Building', 'Dr. P. Fortuin'),
('D005', 'Health and Wellness Sciences', 'Health Sciences Building', 'Prof. M. Khumalo'),
('D006', 'Applied Sciences', 'Science Building', 'Dr. S. Pillay'),
('D007', 'General', 'Administration Building', 'Ms. A. Hendricks');


-- ---------------------------------------------------------
-- USERS
-- ---------------------------------------------------------

INSERT IGNORE INTO users (user_id, first_name, last_name, email, date_of_birth, department_id) VALUES
('U001', 'Thandi', 'Mokoena', 'thandi.mokoena@cput.ac.za', '1985-04-12', 'D007'),
('U002', 'Johan', 'van Wyk', 'johan.vanwyk@cput.ac.za', '1979-09-03', 'D001'),
('U003', 'Sipho', 'Ndlovu', 'sipho.ndlovu@mycput.ac.za', '2003-02-18', 'D001'),
('U004', 'Aisha', 'Patel', 'aisha.patel@mycput.ac.za', '2002-11-27', 'D002'),
('U005', 'Liam', 'Smith', 'liam.smith@mycput.ac.za', '2004-06-09', 'D003'),
('U006', 'Nomsa', 'Dlamini', 'nomsa.dlamini@cput.ac.za', '1988-01-22', 'D003');


-- ---------------------------------------------------------
-- REGISTER + LOGIN + ROLES
-- Login.registrar_id -> Register.registrar_id -> Register.email -> Users.email
-- (the same chain LoginServiceImpl.authenticate follows)
-- ---------------------------------------------------------

INSERT IGNORE INTO register (registrar_id, email, date_registered) VALUES
('U001', 'thandi.mokoena@cput.ac.za', '2026-01-15'),
('U002', 'johan.vanwyk@cput.ac.za', '2026-01-20'),
('U003', 'sipho.ndlovu@mycput.ac.za', '2026-02-02'),
('U004', 'aisha.patel@mycput.ac.za', '2026-02-05'),
('U005', 'liam.smith@mycput.ac.za', '2026-02-10'),
('U006', 'nomsa.dlamini@cput.ac.za', '2026-02-12');

INSERT IGNORE INTO login (login_id, registrar_id, username, password) VALUES
('L001', 'U001', 'tmokoena', 'Password123'),
('L002', 'U002', 'jvanwyk', 'Password123'),
('L003', 'U003', 'sndlovu', 'Password123'),
('L004', 'U004', 'apatel', 'Password123'),
('L005', 'U005', 'lsmith', 'Password123'),
('L006', 'U006', 'ndlamini', 'Password123');

INSERT IGNORE INTO user_role (user_role_id, user_id, role) VALUES
('UR001', 'U001', 'ADMIN'),
('UR002', 'U002', 'LECTURER'),
('UR003', 'U003', 'STUDENT'),
('UR004', 'U004', 'STUDENT'),
('UR005', 'U005', 'STUDENT'),
('UR006', 'U006', 'LECTURER');


-- ---------------------------------------------------------
-- STAFF + STUDENTS
-- ---------------------------------------------------------

INSERT IGNORE INTO staff (staff_id, user_id) VALUES
('S001', 'U001'),
('S002', 'U002'),
('S003', 'U006');

INSERT IGNORE INTO student (student_id, student_number, user_id) VALUES
('ST001', '230145678', 'U003'),
('ST002', '229876543', 'U004'),
('ST003', '231002345', 'U005');


-- ---------------------------------------------------------
-- CONTACT DETAILS + ADDRESSES
-- ---------------------------------------------------------

INSERT IGNORE INTO contact (contact_id, contact, description, user_id) VALUES
('C001', '021 959 6000', 'Office', 'U001'),
('C002', '021 959 6123', 'Office', 'U002'),
('C003', '072 345 6789', 'Mobile', 'U003'),
('C004', '083 456 7890', 'Mobile', 'U004'),
('C005', '061 567 8901', 'Mobile', 'U005'),
('C006', '021 959 6456', 'Office', 'U006');

INSERT IGNORE INTO address (address_id, address, description, user_id) VALUES
('A001', 'Symphony Way, Bellville, Cape Town, 7535', 'Work', 'U001'),
('A002', '12 Durban Road, Bellville, Cape Town, 7530', 'Home', 'U002'),
('A003', 'Room 214, Catsville Residence, Bellville, 7535', 'Residence', 'U003'),
('A004', '45 Voortrekker Road, Parow, Cape Town, 7500', 'Home', 'U004'),
('A005', '8 Kloof Street, Gardens, Cape Town, 8001', 'Home', 'U005'),
('A006', '22 Main Road, Rondebosch, Cape Town, 7700', 'Home', 'U006');


-- ---------------------------------------------------------
-- FACILITIES
-- ---------------------------------------------------------

INSERT IGNORE INTO facility (facility_id, name, capacity, location, department_id, facility_type) VALUES
('F001', 'Computer Lab 1', 40, 'Engineering Building, Level 2', 'D001', 'LAB'),
('F002', 'Computer Lab 2', 35, 'Engineering Building, Level 2', 'D001', 'LAB'),
('F003', 'Main Hall', 300, 'Administration Building, Ground Floor', 'D007', 'HALL'),
('F004', 'Seminar Room A', 25, 'Commerce Building, Level 1', 'D002', 'ROOM'),
('F005', 'Seminar Room B', 20, 'Commerce Building, Level 1', 'D002', 'ROOM'),
('F006', 'Chemistry Lab', 30, 'Science Building, Level 3', 'D006', 'LAB'),
('F007', 'Auditorium', 500, 'Student Centre', 'D007', 'AUDITORIUM'),
('F008', 'Electronics Workshop', 24, 'Engineering Building, Level 1', 'D003', 'LAB');


-- ---------------------------------------------------------
-- EQUIPMENT
-- ---------------------------------------------------------

INSERT IGNORE INTO equipment (equipment_id, name, serial_number, facility_id, equipment_status) VALUES
('E001', 'Epson Projector', 'EP-2024-0012', 'F001', 'AVAILABLE'),
('E002', 'Dell OptiPlex Desktop', 'DL-7090-1145', 'F001', 'IN_USE'),
('E003', 'Smart Whiteboard', 'SW-8812-0031', 'F002', 'AVAILABLE'),
('E004', 'PA Sound System', 'PA-3300-0007', 'F003', 'AVAILABLE'),
('E005', 'Wireless Microphone Set', 'MC-1200-0045', 'F007', 'UNDER_MAINTENANCE'),
('E006', 'Conference Camera', 'CC-4400-0019', 'F004', 'AVAILABLE'),
('E007', 'Fume Hood', 'FH-0900-0003', 'F006', 'IN_USE'),
('E008', 'Digital Oscilloscope', 'OS-5520-0088', 'F008', 'AVAILABLE'),
('E009', 'Laser Printer', 'LP-2210-0154', 'F002', 'UNDER_MAINTENANCE'),
('E010', 'Stage Lighting Rig', 'SL-7700-0002', 'F007', 'AVAILABLE');


-- ---------------------------------------------------------
-- TIME SLOTS (spread around late Oct / Nov 2026 so there
-- are both past and upcoming bookings)
-- ---------------------------------------------------------

INSERT IGNORE INTO time_slot (time_slot_id, date, start_time, end_time) VALUES
('TS001', '2026-10-12', '08:00:00', '10:00:00'),
('TS002', '2026-10-15', '10:00:00', '12:00:00'),
('TS003', '2026-10-20', '13:00:00', '15:00:00'),
('TS004', '2026-10-28', '09:00:00', '11:00:00'),
('TS005', '2026-10-30', '14:00:00', '16:00:00'),
('TS006', '2026-11-03', '08:00:00', '10:00:00'),
('TS007', '2026-11-05', '11:00:00', '13:00:00'),
('TS008', '2026-11-10', '15:00:00', '17:00:00'),
('TS009', '2026-11-14', '09:00:00', '12:00:00'),
('TS010', '2026-11-20', '18:00:00', '21:00:00');


-- ---------------------------------------------------------
-- BOOKINGS
-- ---------------------------------------------------------

INSERT IGNORE INTO booking (booking_id, facility_id, time_slot_id, user_id, purpose, booking_status) VALUES
('B001', 'F001', 'TS001', 'U003', 'Programming group project', 'APPROVED'),
('B002', 'F004', 'TS002', 'U004', 'Marketing presentation rehearsal', 'APPROVED'),
('B003', 'F002', 'TS003', 'U003', 'Database assignment study session', 'CANCELLED'),
('B004', 'F001', 'TS004', 'U003', 'Web development workshop', 'APPROVED'),
('B005', 'F006', 'TS005', 'U005', 'Chemistry practical revision', 'PENDING'),
('B006', 'F003', 'TS006', 'U002', 'ICT department guest lecture', 'APPROVED'),
('B007', 'F005', 'TS007', 'U004', 'Accounting tutorial', 'PENDING'),
('B008', 'F008', 'TS008', 'U006', 'Circuits lab demonstration', 'APPROVED'),
('B009', 'F002', 'TS009', 'U003', 'Exam preparation session', 'PENDING'),
('B010', 'F007', 'TS010', 'U001', 'End-of-year awards ceremony', 'REJECTED');


-- ---------------------------------------------------------
-- INVOICES (one per booking - booking_id is a one-to-one)
-- ---------------------------------------------------------

INSERT IGNORE INTO invoice (invoice_id, amount, issue_date, due_date, booking_id) VALUES
('INV001', 150.00, '2026-10-05', '2026-10-19', 'B001'),
('INV002', 200.00, '2026-10-08', '2026-10-22', 'B002'),
('INV003', 150.00, '2026-10-21', '2026-11-04', 'B004'),
('INV004', 1500.00, '2026-10-25', '2026-11-08', 'B006'),
('INV005', 350.00, '2026-10-25', '2026-11-08', 'B008');


-- ---------------------------------------------------------
-- MAINTENANCE REQUESTS
-- ---------------------------------------------------------

INSERT IGNORE INTO maintenance_request (request_id, equipment_id, reported_by, description, date_reported, maintenance_priority, maintenance_status) VALUES
('MR001', 'E005', 'U002', 'Microphones cut out intermittently during lectures', '2026-10-14', 'HIGH', 'IN_PROGRESS'),
('MR002', 'E009', 'U003', 'Printer jams on every second page', '2026-10-18', 'MEDIUM', 'OPEN'),
('MR003', 'E001', 'U006', 'Projector bulb flickering', '2026-09-30', 'LOW', 'RESOLVED'),
('MR004', 'E007', 'U005', 'Fume hood extractor fan is very loud', '2026-10-22', 'HIGH', 'OPEN');


-- ---------------------------------------------------------
-- NOTIFICATIONS
-- ---------------------------------------------------------

INSERT IGNORE INTO notification (notification_id, user_id, message, sent_date, notification_type) VALUES
('N001', 'U003', 'Your booking for Computer Lab 1 on 28 Oct has been approved.', '2026-10-21', 'BOOKING_CONFIRMATION'),
('N002', 'U003', 'Invoice INV003 of R150.00 is due on 4 Nov.', '2026-10-21', 'INVOICE_REMINDER'),
('N003', 'U003', 'Your printer fault report (MR002) has been logged.', '2026-10-18', 'MAINTENANCE_UPDATE'),
('N004', 'U004', 'Your booking for Seminar Room A on 15 Oct has been approved.', '2026-10-09', 'BOOKING_CONFIRMATION'),
('N005', 'U005', 'Your booking for the Chemistry Lab is awaiting approval.', '2026-10-23', 'BOOKING_CONFIRMATION'),
('N006', 'U002', 'Maintenance on the wireless microphone set is in progress.', '2026-10-20', 'MAINTENANCE_UPDATE'),
('N007', 'U001', 'Invoice INV004 of R1500.00 has been issued.', '2026-10-25', 'INVOICE_REMINDER'),
('N008', 'U006', 'Your booking for the Electronics Workshop on 10 Nov has been approved.', '2026-10-24', 'BOOKING_CONFIRMATION');
