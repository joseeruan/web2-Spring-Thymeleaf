CREATE TABLE inspection_agency (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE donor_agency (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(200),
    phone VARCHAR(20),
    working_hours VARCHAR(50),
    description TEXT
);

CREATE TABLE product (
    code INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE batch (
    id INT AUTO_INCREMENT PRIMARY KEY,
    delivery_date DATE NOT NULL,
    notes TEXT,
    inspection_agency_id INT NOT NULL,
    donor_agency_id INT NOT NULL,
    FOREIGN KEY (inspection_agency_id) REFERENCES inspection_agency(id),
    FOREIGN KEY (donor_agency_id) REFERENCES donor_agency(id)
);

CREATE TABLE batch_product (
    batch_id INT NOT NULL,
    product_code INT NOT NULL,
    PRIMARY KEY (batch_id, product_code),
    FOREIGN KEY (batch_id) REFERENCES batch(id) ON DELETE CASCADE,
    FOREIGN KEY (product_code) REFERENCES product(code) ON DELETE CASCADE
);
