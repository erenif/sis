CREATE TABLE IF NOT EXISTS Course_Table (
    course_id INT PRIMARY KEY,
    course_name VARCHAR(255) NOT NULL,
    quota INT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    schedule DATETIME NOT NULL,
    syllabus VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Student_Table (
    student_id INT PRIMARY KEY,
    student_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Professor_Table (
    professor_id INT PRIMARY KEY,
    professor_name VARCHAR(255) NOT NULL,
);

CREATE TABLE IF NOT EXISTS Admin_Table(
    admin_id INT PRIMARY KEY,
    admin_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Enrollment_Table (
    student_id INT,
    course_id INT,
    grade INT,
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES Student_Table(student_id),
    FOREIGN KEY (course_id) REFERENCES Course_Table(course_id)
);

CREATE TABLE IF NOT EXISTS Teaching_Table (
    professor_id INT,
    course_id INT UNIQUE,
    PRIMARY KEY (professor_id, course_id),
    FOREIGN KEY (professor_id) REFERENCES Professor_Table(professor_id),
    FOREIGN KEY (course_id) REFERENCES Course_Table(course_id)
);



