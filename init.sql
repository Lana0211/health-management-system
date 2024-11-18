CREATE TABLE users (
    user_id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100),
    age INTEGER,
    gender VARCHAR(10)
);

CREATE TABLE health_data (
    data_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) REFERENCES users(user_id),
    heart_rate DOUBLE PRECISION,
    blood_pressure VARCHAR(20),
    body_temperature DOUBLE PRECISION,
    weight DOUBLE PRECISION,
    steps INTEGER,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE health_goals (
    goal_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) REFERENCES users(user_id),
    goal_type VARCHAR(50),
    target_value DOUBLE PRECISION,
    start_date DATE,
    end_date DATE,
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE health_reports (
    report_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) REFERENCES users(user_id),
    report_date TIMESTAMP,
    report_content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
); 