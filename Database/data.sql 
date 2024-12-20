-- Course_Table Data
INSERT INTO Course_Table (course_id, course_name, quota, credits, start_time, end_time, course_day, syllabus)
VALUES
(1, 'Introduction to Programming', 30, 4, '09:00:00', '11:00:00', 'MONDAY', 'Basic programming concepts'),
(2, 'Data Structures', 25, 3, '11:30:00', '13:00:00', 'TUESDAY', 'Learn about data organization'),
(3, 'Algorithms', 20, 3, '14:00:00', '15:30:00', 'WEDNESDAY', 'Algorithm design and analysis'),
(4, 'Database Systems', 15, 4, '10:00:00', '12:00:00', 'THURSDAY', 'Relational databases'),
(5, 'Operating Systems', 20, 4, '13:00:00', '15:00:00', 'FRIDAY', 'OS fundamentals');

INSERT INTO Prerequisite_Table (course_id, prerequisite_course_id)
VALUES
(2, 1),  -- Data Structures requires Introduction to Programming
(3, 2),  -- Algorithms requires Data Structures
(4, 3),  -- Database Systems requires Algorithms
(5, 3);  -- Operating Systems requires Algorithms

INSERT INTO Student_Table (student_id, student_name, password, gpa, available_credits)
VALUES
(101, 'Alice Smith', 'password123', 3.8, 15),
(102, 'Bob Johnson', 'password456', 3.5, 20),
(103, 'Charlie Brown', 'password789', 3.0, 25);

INSERT INTO Professor_Table (professor_id, professor_name)
VALUES
(1, 'Dr. John Doe'),
(2, 'Dr. Jane Roe'),
(3, 'Dr. Alan Turing');

INSERT INTO Admin_Table (admin_id, admin_name, password)
VALUES
(1, 'System Admin', 'adminpass');

INSERT INTO Enrollment_Table (student_id, course_id, grade)
VALUES
(101, 1, 'A'),
(101, 2, 'B+'),
(102, 3, 'A-'),
(103, 4, NULL),
(102, 5, NULL);

-- Schedule_Table Data
INSERT INTO Schedule_Table (course_id, day_of_week, start_time, end_time)
VALUES
(1, 'MONDAY', '09:00:00', '11:00:00'),
(2, 'TUESDAY', '11:30:00', '13:00:00'),
(3, 'WEDNESDAY', '14:00:00', '15:30:00'),
(4, 'THURSDAY', '10:00:00', '12:00:00'),
(5, 'FRIDAY', '13:00:00', '15:00:00');

-- Teaching_Table Data
INSERT INTO Teaching_Table (professor_id, course_id)
VALUES
(1, 1), -- Dr. John Doe teaches Introduction to Programming
(2, 2), -- Dr. Jane Roe teaches Data Structures
(3, 3), -- Dr. Alan Turing teaches Algorithms
(1, 4), -- Dr. John Doe teaches Database Systems
(2, 5); -- Dr. Jane Roe teaches Operating Systems