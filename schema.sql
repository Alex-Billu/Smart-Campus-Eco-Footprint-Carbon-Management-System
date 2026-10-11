-- Smart Campus Eco-Footprint & Carbon Management System
-- MySQL Database Schema
-- Target: MySQL 8.0+

DROP DATABASE IF EXISTS campus_eco;
CREATE DATABASE campus_eco CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE campus_eco;

-- ============ ADMIN ============
CREATE TABLE Admin (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============ DEPARTMENT ============
CREATE TABLE Department (
    dept_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_name VARCHAR(100) NOT NULL,
    dept_code VARCHAR(20) NOT NULL UNIQUE,
    building VARCHAR(100),
    floor_area_sqm DECIMAL(10,2),
    staff_count INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============ ENERGY USAGE ============
CREATE TABLE Energy_Usage (
    energy_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_id INT NOT NULL,
    usage_month VARCHAR(20) NOT NULL,
    electricity_kwh DECIMAL(12,2) NOT NULL,
    cost_amount DECIMAL(12,2),
    co2_emission_kg DECIMAL(12,2),
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dept_id) REFERENCES Department(dept_id) ON DELETE CASCADE
);

-- ============ WATER USAGE ============
CREATE TABLE Water_Usage (
    water_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_id INT NOT NULL,
    usage_month VARCHAR(20) NOT NULL,
    water_litres DECIMAL(12,2) NOT NULL,
    cost_amount DECIMAL(12,2),
    co2_emission_kg DECIMAL(12,2),
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dept_id) REFERENCES Department(dept_id) ON DELETE CASCADE
);

-- ============ TRANSPORT DETAILS ============
CREATE TABLE Transport_Details (
    transport_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_id INT NOT NULL,
    usage_month VARCHAR(20) NOT NULL,
    vehicle_type VARCHAR(50),
    distance_km DECIMAL(10,2),
    fuel_litres DECIMAL(10,2),
    co2_emission_kg DECIMAL(12,2),
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dept_id) REFERENCES Department(dept_id) ON DELETE CASCADE
);

-- ============ WASTE RECORD ============
CREATE TABLE Waste_Record (
    waste_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_id INT NOT NULL,
    usage_month VARCHAR(20) NOT NULL,
    recyclable_kg DECIMAL(10,2),
    non_recyclable_kg DECIMAL(10,2),
    co2_emission_kg DECIMAL(12,2),
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dept_id) REFERENCES Department(dept_id) ON DELETE CASCADE
);

-- ============ CARBON REPORT ============
CREATE TABLE Carbon_Report (
    report_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_id INT,
    usage_month VARCHAR(20) NOT NULL,
    energy_co2 DECIMAL(12,2),
    water_co2 DECIMAL(12,2),
    transport_co2 DECIMAL(12,2),
    waste_co2 DECIMAL(12,2),
    total_co2 DECIMAL(12,2),
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dept_id) REFERENCES Department(dept_id) ON DELETE SET NULL
);

-- ============ GREEN POINTS ============
CREATE TABLE Green_Points (
    point_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_id INT NOT NULL,
    usage_month VARCHAR(20) NOT NULL,
    points INT DEFAULT 0,
    reason VARCHAR(255),
    awarded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dept_id) REFERENCES Department(dept_id) ON DELETE CASCADE
);

-- ============ CARBON BUDGET ============
CREATE TABLE Carbon_Budget (
    budget_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_id INT NOT NULL,
    usage_month VARCHAR(20) NOT NULL,
    budget_limit_kg DECIMAL(12,2) NOT NULL,
    used_kg DECIMAL(12,2) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'OK',
    FOREIGN KEY (dept_id) REFERENCES Department(dept_id) ON DELETE CASCADE,
    UNIQUE KEY uq_budget (dept_id, usage_month)
);

-- ============ TREE TRACKER ============
CREATE TABLE Tree_Tracker (
    tree_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_id INT,
    tree_count INT NOT NULL,
    species VARCHAR(100),
    planted_date DATE,
    carbon_absorption_kg DECIMAL(12,2),
    location VARCHAR(150),
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dept_id) REFERENCES Department(dept_id) ON DELETE SET NULL
);

-- ============ ALERT LOG ============
CREATE TABLE Alert_Log (
    alert_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_id INT,
    alert_type VARCHAR(50) NOT NULL,
    severity VARCHAR(20),
    message TEXT,
    usage_month VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dept_id) REFERENCES Department(dept_id) ON DELETE SET NULL
);

-- ============ DEPARTMENT RANKING ============
CREATE TABLE Department_Ranking (
    rank_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_id INT NOT NULL,
    usage_month VARCHAR(20) NOT NULL,
    rank_position INT,
    total_co2 DECIMAL(12,2),
    green_points INT DEFAULT 0,
    FOREIGN KEY (dept_id) REFERENCES Department(dept_id) ON DELETE CASCADE
);

-- ============ SUSTAINABILITY SCORE ============
CREATE TABLE Sustainability_Score (
    score_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_id INT NOT NULL,
    usage_month VARCHAR(20) NOT NULL,
    energy_score DECIMAL(5,2),
    water_score DECIMAL(5,2),
    waste_score DECIMAL(5,2),
    transport_score DECIMAL(5,2),
    overall_score DECIMAL(5,2),
    rating VARCHAR(10),
    FOREIGN KEY (dept_id) REFERENCES Department(dept_id) ON DELETE CASCADE,
    UNIQUE KEY uq_score (dept_id, usage_month)
);

-- ============ WEATHER IMPACT ============
CREATE TABLE Weather_Impact (
    weather_id INT AUTO_INCREMENT PRIMARY KEY,
    usage_month VARCHAR(20) NOT NULL,
    temperature_c DECIMAL(5,2),
    rainfall_mm DECIMAL(6,2),
    humidity_pct DECIMAL(5,2),
    season VARCHAR(20),
    energy_trend VARCHAR(50),
    water_trend VARCHAR(50),
    notes TEXT
);

-- ============ COST ANALYSIS ============
CREATE TABLE Cost_Analysis (
    cost_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_id INT,
    usage_month VARCHAR(20) NOT NULL,
    energy_cost DECIMAL(12,2),
    water_cost DECIMAL(12,2),
    transport_cost DECIMAL(12,2),
    waste_cost DECIMAL(12,2),
    total_cost DECIMAL(12,2),
    total_co2 DECIMAL(12,2),
    cost_per_co2 DECIMAL(10,4),
    FOREIGN KEY (dept_id) REFERENCES Department(dept_id) ON DELETE SET NULL
);

-- ============ GREEN EVENTS ============
CREATE TABLE Green_Events (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    event_name VARCHAR(150) NOT NULL,
    event_type VARCHAR(50),
    event_date DATE,
    location VARCHAR(150),
    description TEXT,
    green_points INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============ EVENT PARTICIPANTS ============
CREATE TABLE Event_Participants (
    participant_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,
    dept_id INT,
    participant_name VARCHAR(100),
    attended TINYINT(1) DEFAULT 0,
    points_awarded INT DEFAULT 0,
    FOREIGN KEY (event_id) REFERENCES Green_Events(event_id) ON DELETE CASCADE,
    FOREIGN KEY (dept_id) REFERENCES Department(dept_id) ON DELETE SET NULL
);

-- ============ AI RECOMMENDATIONS ============
CREATE TABLE AI_Recommendations (
    rec_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_id INT,
    usage_month VARCHAR(20),
    category VARCHAR(50),
    recommendation TEXT,
    priority VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dept_id) REFERENCES Department(dept_id) ON DELETE SET NULL
);

-- ============ CERTIFICATE RECORD ============
CREATE TABLE Certificate_Record (
    cert_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_id INT,
    cert_type VARCHAR(50),
    cert_month VARCHAR(20),
    title VARCHAR(150),
    description TEXT,
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dept_id) REFERENCES Department(dept_id) ON DELETE SET NULL
);

-- ============ QR TRACKING ============
CREATE TABLE QR_Tracking (
    qr_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_id INT,
    resource_type VARCHAR(50),
    encoded_data TEXT,
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dept_id) REFERENCES Department(dept_id) ON DELETE SET NULL
);

-- ============ SEED ADMIN ============
-- Default credentials: admin / admin123
INSERT INTO Admin (username, password_hash, full_name, email)
VALUES ('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'System Administrator', 'admin@campus.edu');
