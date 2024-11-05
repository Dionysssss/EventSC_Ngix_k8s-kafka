DROP SCHEMA IF EXISTS event_sc;
-- Create the schema
CREATE SCHEMA IF NOT EXISTS event_sc;

-- Use the schema
USE event_sc;
-- User table
CREATE TABLE User (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    isAuthenticated BOOLEAN DEFAULT FALSE
);

-- Event table
CREATE TABLE Event (
    event_id INT PRIMARY KEY AUTO_INCREMENT,
    eventName VARCHAR(255) NOT NULL,
    eventLocation VARCHAR(255),
    eventDescription TEXT,
    eventDate DATE,
    eventTime TIME,
    eventCreator_id INT,
    FOREIGN KEY (eventCreator_id) REFERENCES User(user_id) ON DELETE SET NULL
);

-- Comment table
CREATE TABLE Comment (
    comment_id INT PRIMARY KEY AUTO_INCREMENT,
    content TEXT NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    postedBy INT,
    event_id INT,
    FOREIGN KEY (postedBy) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES Event(event_id) ON DELETE CASCADE
);

CREATE TABLE VerificationToken (
    id SERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
    expiration TIMESTAMP
);

-- CREATE TABLE SecurityQuestion (
--     id SERIAL PRIMARY KEY,
--     user_id INT,
--     question VARCHAR(255) NOT NULL,
--     answer VARCHAR(255) NOT NULL,
--     FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
-- );