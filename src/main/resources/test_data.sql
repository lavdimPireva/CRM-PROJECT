-- Create the customers table
CREATE TABLE IF NOT EXISTS customers (
                                         id SERIAL PRIMARY KEY,
                                         name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    address VARCHAR(255),
    date_added DATE DEFAULT CURRENT_DATE
    );

-- Insert some test data
INSERT INTO customers (name, email, phone, address) VALUES
                                                        ('John Doe', 'john@example.com', '555-1234', '123 Main St'),
                                                        ('Jane Smith', 'jane@example.com', '555-5678', '456 Oak St'),
                                                        ('Alice Johnson', 'alice@example.com', '555-7890', '789 Pine St'),
                                                        ('Bob Brown', 'bob@example.com', '555-2345', '101 Maple St');
