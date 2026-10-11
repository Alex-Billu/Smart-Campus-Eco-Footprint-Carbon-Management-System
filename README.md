# Smart Campus Eco-Footprint & Carbon Management System

A complete Java Swing desktop application for tracking and managing campus sustainability metrics — energy, water, transport, waste, and carbon footprint — with advanced features like an AI recommendation engine, carbon savings simulator, weather impact analysis, cost-vs-carbon analyzer, green events, and QR-based resource monitoring.

## Tech Stack
- **Frontend:** Java Swing + AWT
- **Java Runtime:** JDK 25 LTS
- **Backend:** Core Java + JDBC
- **Database:** MySQL 8.0+
- **Architecture:** MVC (Model / DAO / Service / View)
- **Libraries:** MySQL Connector/J, JFreeChart, Apache POI, iText PDF, ZXing

## Project Structure
```
src/main/java/com/campus/eco/
├── Main.java                  # Application entry point
├── config/                     # DBConnection
├── util/                       # PasswordUtil, Validation, UIHelper
├── model/                      # 20 entity classes
├── dao/                        # 18 DAO classes (JDBC)
├── service/                    # CarbonFactors, CarbonCalculator, AIEngine, Alert, Score, Simulator, Weather, Cost, Chart, QR, PDF, Excel exports
└── ui/                         # LoginFrame, MainFrame, 21 feature panels
schema.sql                      # MySQL database schema + seed admin
pom.xml                         # Maven build with shade plugin (executable jar)
```

## Setup Instructions

### 1. Database
```bash
mysql -u root -p < schema.sql
```
This creates the `campus_eco` database with all 20 tables and a seed admin account.

### 2. Configure Connection
Set these environment variables for the application:
```java
CAMPUS_ECO_DB_URL=jdbc:mysql://localhost:3306/campus_eco?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
CAMPUS_ECO_DB_USER=root
CAMPUS_ECO_DB_PASSWORD=your_mysql_password
```
The URL defaults to the local `campus_eco` database, the user defaults to `root`, and the password has no default. In PowerShell, set them with `$env:CAMPUS_ECO_DB_USER = "root"` and `$env:CAMPUS_ECO_DB_PASSWORD = "your_mysql_password"` before launching.

### 3. Build & Run
```bash
mvn clean package
java -jar target/smart-campus-eco-1.0.0.jar
```
Or open in NetBeans/Eclipse as a Maven project and run `com.campus.eco.Main`.

### 4. Login
- **Username:** `admin`
- **Password:** `admin123`

## Modules Implemented
1. Authentication (login, SHA-256 password hashing, forgot password, logout)
2. Dashboard (8 KPI cards, carbon-by-source pie, department-carbon bar)
3. Department Management (CRUD + search)
4. Energy Management (kWh tracking, auto CO2 calc)
5. Water Management (litres tracking, auto CO2 calc)
6. Transportation Management (fuel/distance, vehicle-type-aware CO2)
7. Waste Management (recyclable vs non-recyclable)
8. Carbon Footprint Calculator (aggregates all sources per dept/month)
9. Eco Score System (0-100 benchmarked scores, A+ to D rating)
10. Report Generation (PDF via iText, Excel via Apache POI)
11. Green Points Reward System + Leaderboard
12. Carbon Budget Management (per-dept monthly limits, over-limit alerts)
13. Campus Tree Impact Tracker (net footprint = emission − absorption)
14. Smart Alert System (threshold-based scanning)
15. Carbon Comparison Engine (dashboard trends)
16. Sustainability Scorecard (energy/water/waste/transport/overall)
17. Carbon Savings Simulator (LED/trees/vehicle what-if)
18. Green Campus Leaderboard (ranked by green points)
19. QR-Based Resource Monitoring (ZXing generation + preview)
20. AI Recommendation Engine (rule-based, per-dept)
21. Smart Weather Impact Analysis (season detection, summer forecast)
22. Cost vs Carbon Analyzer (₹/kg CO2 efficiency)
23. Green Event Management (events + participants)
24. AI Sustainability Assistant (chat-based Q&A)

## Carbon Emission Factors
| Source        | Factor              |
|---------------|---------------------|
| Electricity   | 0.82 kg CO2 / kWh   |
| Water         | 0.0003 kg CO2 / L   |
| Petrol        | 2.31 kg CO2 / L     |
| Diesel        | 2.68 kg CO2 / L     |
| Waste (non-rec) | 0.45 kg CO2 / kg  |
| Waste (rec)   | 0.02 kg CO2 / kg    |
| Tree          | 21.77 kg CO2 / year |

## Documentation
See `docs/` for ER diagram, use case diagram, class diagram, DFD, and sequence diagram descriptions.

## License
Educational use.
