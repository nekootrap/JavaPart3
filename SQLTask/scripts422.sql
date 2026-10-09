
CREATE TABLE car (
    id INT PRIMARY KEY,         
    brand VARCHAR(100),        
    model VARCHAR(100),
    cost INT
);

CREATE TABLE human (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    age INT,
    driver_license BOOLEAN,         
    car_id INT REFERENCES car(id) 
);