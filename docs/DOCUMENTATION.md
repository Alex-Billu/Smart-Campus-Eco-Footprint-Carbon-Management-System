# Smart Campus Eco-Footprint — System Documentation

## 1. ER Diagram (Entity-Relationship)

```
                         +------------------+
                         |      Admin       |
                         +------------------+
                         | admin_id (PK)    |
                         | username         |
                         | password_hash    |
                         | full_name        |
                         | email            |
                         +------------------+

+-------------------+        +-------------------+
|    Department     |        |   Energy_Usage    |
+-------------------+        +-------------------+
| dept_id (PK)      |<------| energy_id (PK)    |
| dept_name         |  1   N | dept_id (FK)      |
| dept_code         |        | usage_month       |
| building          |        | electricity_kwh   |
| floor_area_sqm    |        | cost_amount        |
| staff_count       |        | co2_emission_kg   |
+-------------------+        +-------------------+
        || 1
        ||  N
   +----+----+----+----+----+----+----+----+
   |    |    |    |    |    |    |    |    |
  Water Transport Waste Carbon Green Carbon Tree Alert
  _Usage _Details _Rec Report Points Budget _Tracker _Log

+----------------+  +-------------------+  +-------------------+
| Sustainability |  |  Carbon_Budget    |  |   Tree_Tracker    |
| _Score         |  +-------------------+  +-------------------+
+----------------+  | budget_id (PK)    |  | tree_id (PK)      |
| score_id (PK)  |  | dept_id (FK)      |  | dept_id (FK)      |
| dept_id (FK)   |  | usage_month       |  | tree_count        |
| usage_month    |  | budget_limit_kg   |  | species           |
| energy_score   |  | used_kg           |  | planted_date      |
| water_score    |  | status            |  | carbon_absorption |
| waste_score    |  +-------------------+  | location          |
| transport_score|                          +-------------------+
| overall_score  |  +-------------------+  +-------------------+
| rating         |  |  Green_Points     |  | Weather_Impact    |
+----------------+  +-------------------+  +-------------------+
                    | point_id (PK)     |  | weather_id (PK)   |
+----------------+  | dept_id (FK)      |  | usage_month       |
| Department_    |  | usage_month       |  | temperature_c     |
| Ranking        |  | points            |  | rainfall_mm       |
+----------------+  | reason            |  | humidity_pct      |
| rank_id (PK)   |  +-------------------+  | season            |
| dept_id (FK)   |                          | energy_trend      |
| usage_month    |  +-------------------+    | water_trend       |
| rank_position  |  | Cost_Analysis     |   | notes             |
| total_co2      |  +-------------------+    +-------------------+
| green_points   |  | cost_id (PK)      |
+----------------+  | dept_id (FK)      |
                    | usage_month       |
+----------------+  | energy_cost       |
| Green_Events   |  | water_cost        |
+----------------+  | transport_cost    |
| event_id (PK)  |  | waste_cost        |
| event_name     |  | total_cost        |
| event_type     |  | total_co2         |
| event_date     |  | cost_per_co2      |
| location       |  +-------------------+
| description    |
| green_points   |  +-------------------+
+----------------+  | AI_Recommendations|
        || 1        +-------------------+
        || N        | rec_id (PK)       |
+----------------+  | dept_id (FK)      |
| Event_         |  | usage_month       |
| Participants   |  | category           |
+----------------+  | recommendation    |
| participant_id|  | priority           |
| event_id (FK)  |  +-------------------+
| dept_id (FK)   |
| participant_   |  +-------------------+  +-------------------+
|   name         |  | Certificate_      |  | QR_Tracking       |
| attended       |  | Record            |  +-------------------+
| points_awarded |  +-------------------+  | qr_id (PK)        |
+----------------+  | cert_id (PK)      |  | dept_id (FK)      |
                    | dept_id (FK)      |  | resource_type     |
                    | cert_type         |  | encoded_data      |
                    | cert_month        |  +-------------------+
                    | title             |
                    | description       |
                    +-------------------+
```

**Relationships:**
- Department 1:N Energy_Usage, Water_Usage, Transport_Details, Waste_Record, Carbon_Report, Green_Points, Carbon_Budget, Sustainability_Score, Department_Ranking, Alert_Log, Cost_Analysis, AI_Recommendations, Certificate_Record, QR_Tracking
- Department 1:N Tree_Tracker
- Green_Events 1:N Event_Participants
- Department 1:N Event_Participants

---

## 2. Use Case Diagram

```
                    +-----------------------------------------+
                    |     Smart Campus Eco-Footprint System   |
                    +-----------------------------------------+
                                   |
                          [Admin Actor]
                                   |
   +-----------+   +-------------+   +--------------+   +-----------+
   | Login /   |   | Manage      |   | Generate     |   | Export     |
   | Forgot    |   | Departments |   | Carbon Report|   | PDF/Excel  |
   | Password  |   +-------------+   +--------------+   +-----------+
   +-----------+
   +-----------+   +-------------+   +--------------+   +-----------+
   | Track     |   | Track Water |   | Track Transp. |   | Track Waste|
   | Energy    |   +-------------+   +--------------+   +-----------+
   +-----------+
   +-----------+   +-------------+   +--------------+   +-----------+
   | Set Carbon|   | Award Green |   | Plant Trees  |   | Scan Alerts|
   | Budget    |   | Points      |   |              |   +-----------+
   +-----------+   +-------------+   +--------------+
   +-----------+   +-------------+   +--------------+   +-----------+
   | Compute   |   | Run Carbon  |   | Analyze      |   | Generate   |
   | Eco Score |   | Simulator   |   | Weather      |   | QR Codes   |
   +-----------+   +-------------+   +--------------+   +-----------+
   +-----------+   +-------------+   +--------------+   +-----------+
   | Cost vs   |   | Manage Green|   | Chat with AI |   | Issue      |
   | Carbon    |   | Events      |   | Assistant    |   | Certificates|
   +-----------+   +-------------+   +--------------+   +-----------+
   +-----------+
   | View      |
   | Leaderboard|
   +-----------+
```

---

## 3. Class Diagram (MVC Layers)

```
+-------------------+        +-------------------+        +-------------------+
|   Model Layer     |        |   DAO Layer       |        |   Service Layer   |
+-------------------+        +-------------------+        +-------------------+
| Admin             |<>---->| AdminDAO          |        | CarbonFactors     |
| Department        |<>---->| DepartmentDAO     |        | CarbonCalculator  |
| EnergyUsage       |<>---->| EnergyDAO         |        | AlertService      |
| WaterUsage        |<>---->| WaterDAO          |        | AIEngineService   |
| TransportDetails  |<>---->| TransportDAO      |        | SustainabilityScore|
| WasteRecord       |<>---->| WasteDAO          |        | SimulatorService  |
| CarbonReport      |<>---->| (CarbonReport via |        | WeatherImpact     |
| GreenPoints       |<>---->|  CarbonCalculator)|        | CostAnalysis      |
| CarbonBudget      |<>---->| CarbonBudgetDAO   |        | ChartService      |
| TreeTracker       |<>---->| TreeDAO           |        | PdfExportService  |
| AlertLog          |<>---->| AlertLogDAO       |        | ExcelExportService|
| SustainabilityScore<>--->| SustainabilityDAO |        | QRService         |
| WeatherImpact     |<>---->| WeatherImpactDAO  |        +-------------------+
| CostAnalysis      |<>---->| CostAnalysisDAO   |
| GreenEvent        |<>---->| GreenEventDAO     |        +-------------------+
| EventParticipant  |       |                   |        |   View Layer      |
| AIRecommendation  |<>---->| AIRecommendationDAO|       +-------------------+
| CertificateRecord |<>---->| CertificateDAO   |        | LoginFrame        |
| QRTracking        |<>---->| QRTrackingDAO     |        | MainFrame         |
+-------------------+        +-------------------+        | DashboardPanel    |
                                                          | DepartmentPanel   |
<<uses>>  Model <-- DAO <-- Service <-- View             | EnergyPanel ...     |
                                                          | (21 panels)        |
                                                          +-------------------+
```

---

## 4. Data Flow Diagram (DFD)

### Level 0 (Context)
```
  +--------+     Login credentials      +------------------+
  | Admin  | -------------------------> | Eco-Footprint   |
  +--------+                            | System          |
       ^                                 |                  |
       |  Reports / Charts / Alerts      |                  |
       +-------------------------------- +------------------+
                                                |
                                         MySQL Database
```

### Level 1
```
  [Admin] --(1. Enter usage data)--> [Validate Input] --(2. Valid data)--> [Calculate CO2]
  [Calculate CO2] --(3. CO2 values)--> [Store in DB via DAO]
  [Admin] --(4. Request dashboard)--> [Aggregate Metrics] --(5. Query)--> [DB]
  [DB] --(6. Raw data)--> [Aggregate] --(7. KPIs + Charts)--> [Admin]
  [Admin] --(8. Generate report)--> [Export Service] --(9. PDF/Excel file)--> [Admin]
  [Admin] --(9. Ask AI)--> [AI Engine] --(10. Query DB)--> [DB]
  [DB] --(11. Data)--> [AI Engine] --(12. Recommendation)--> [Admin]
```

---

## 5. Sequence Diagram — Login Flow
```
Admin        LoginFrame        AdminDAO         DBConnection     MySQL
  |               |                |                 |             |
  |--enter creds->|                |                 |             |
  |               |--login(u,p)---->|                 |             |
  |               |                |--getConnection->|             |
  |               |                |                 |--connect---->|
  |               |                |                 |<--conn-------|
  |               |                |--SELECT admin-->|             |
  |               |                |                 |---query----->|
  |               |                |                 |<--result-----|
  |               |                |<--Admin obj-----|             |
  |               |<--Admin---------|                |             |
  |               |--open MainFrame |                 |             |
  |<--dashboard---|                 |                 |             |
```

## Sequence Diagram — Carbon Report Generation
```
Admin   CarbonCalculatorPanel   CarbonCalculatorService   EnergyDAO/WaterDAO/...   MySQL
  |            |                          |                        |                  |
  |--click----->|                         |                        |                  |
  |            |--generateForMonth(m)----->|                        |                  |
  |            |                          |--SUM(co2) grouped------>|                  |
  |            |                          |                        |--SELECT SUM---->|
  |            |                          |                        |<--results--------|
  |            |                          |<--per-dept sums--------|                  |
  |            |                          |--INSERT Carbon_Report-->|                  |
  |            |                          |                        |--INSERT-------->|
  |            |<--List<CarbonReport>-----|                        |                  |
  |<--table----|                          |                        |                  |
```
