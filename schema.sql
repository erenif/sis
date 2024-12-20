CREATE TABLE IF NOT EXISTS Course_Table (
                                            course_id INT PRIMARY KEY,
                                            course_name VARCHAR(255) NOT NULL,
                                            quota INT NOT NULL,
                                            credits INT NOT NULL,
                                            start_time TIME NOT NULL,
                                            end_time TIME NOT NULL,
                                            course_day VARCHAR(50) NOT NULL,
                                            syllabus VARCHAR(255) NOT NULL

);

CREATE TABLE IF NOT EXISTS Prerequisite_Table (
                                                  course_id INT NOT NULL,
                                                  prerequisite_course_id INT NOT NULL,
                                                  PRIMARY KEY (course_id, prerequisite_course_id),
                                                  FOREIGN KEY (course_id) REFERENCES Course_Table(course_id),
                                                  FOREIGN KEY (prerequisite_course_id) REFERENCES Course_Table(course_id)
);


CREATE TABLE IF NOT EXISTS Student_Table (
                                             student_id INT PRIMARY KEY,
                                             student_name VARCHAR(255) NOT NULL,
                                             password VARCHAR(255) NOT NULL,
                                             gpa FLOAT DEFAULT 0.0,
                                             available_credits INT NOT NULL
);

CREATE TABLE IF NOT EXISTS Professor_Table (
                                               professor_id INT PRIMARY KEY,
                                               professor_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Admin_Table(
                                          admin_id INT PRIMARY KEY,
                                          admin_name VARCHAR(255) NOT NULL,
                                          password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Enrollment_Table (
                                                student_id INT,
                                                course_id INT,
                                                grade VARCHAR(2),
                                                PRIMARY KEY (student_id, course_id),
                                                FOREIGN KEY (student_id) REFERENCES Student_Table(student_id),
                                                FOREIGN KEY (course_id) REFERENCES Course_Table(course_id)
);

CREATE TABLE IF NOT EXISTS Schedule_Table (
                                              course_id INT NOT NULL,
                                              day_of_week VARCHAR(10) NOT NULL,
                                              start_time TIME NOT NULL,
                                              end_time TIME NOT NULL,
                                              PRIMARY KEY (course_id, day_of_week),
                                              FOREIGN KEY (course_id) REFERENCES Course_Table(course_id)
);


CREATE TABLE IF NOT EXISTS Teaching_Table (
                                              professor_id INT,
                                              course_id INT UNIQUE,
                                              PRIMARY KEY (professor_id, course_id),
                                              FOREIGN KEY (professor_id) REFERENCES Professor_Table(professor_id),
                                              FOREIGN KEY (course_id) REFERENCES Course_Table(course_id)
);


