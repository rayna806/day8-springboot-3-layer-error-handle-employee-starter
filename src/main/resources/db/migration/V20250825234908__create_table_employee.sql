CREATE TABLE employee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT,
    gender VARCHAR(10),
    salary DOUBLE,
    status BOOLEAN DEFAULT TRUE,
    company_id INT,
    FOREIGN KEY (company_id) REFERENCES company(id)
);

