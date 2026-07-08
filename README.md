# vehicular-cloud-resource-trading-system

A distributed client-server platform simulating a real-time vehicular cloud, where vehicle owners lease unused compute/storage resources and clients submit jobs for scheduling and execution.**

[![Java](https://img.shields.io/badge/Java-11%2B-orange?logo=openjdk)](https://www.oracle.com/java/)
[![SQL](https://img.shields.io/badge/Database-SQL-blue?logo=mysql)](https://www.mysql.com/)
[![Swing](https://img.shields.io/badge/GUI-Java%20Swing-green)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![License](https://img.shields.io/badge/License-Educational-lightgrey)](#-license)
[![Team Project](https://img.shields.io/badge/Type-Team%20Project-9cf)](#team--my-contribution)

</div>

---

## 📌 Overview

The **Vehicular Cloud Resource Trading System (VCRTS)** models a real-world scenario in edge/cloud computing: idle vehicles (parked, charging, or in transit) contribute spare compute and storage capacity to a shared resource pool. Clients submit computational **jobs**, which are matched, scheduled, and executed against available vehicle resources in real time.

This project was built to demonstrate practical experience with **networked systems, concurrent client-server architecture, persistent data storage, and GUI application design** — core skills for backend and full-stack software engineering roles.

## 👥 Team & My Contribution

This was a **team project** built with 3 teammates as part of a Software Engineering course.

**My role focused on the backend and data layer**, specifically:
- Designed and implemented the **client-server networking layer**, including the TCP server (`VCControllerServer`), request handling, and client connections (`JobClient`, `VehicleOwnerClient`)
- Built the **database and storage architecture**, including the SQL schema (`sql/vcrts_table.sql`), `DatabaseManager` (JDBC integration), and the CSV-based storage fallback (`CSVStorageManager`, `CSVTransactionRepository`, `JobStorageManager`)
- Designed the `ServerStorageAdapter` interface so the system could switch between CSV and database persistence without changes to business logic

**Team:**
| Name | Contribution |
|---|---|
| **Sadia Sultana** (me) | Backend, client-server networking, database & storage layer |
| Christopher | GUI / input validation |
| Ryan | GUI / input validation |
| Rezwan | GUI / input validation |

*Contribution breakdown for teammates is approximate — update if their specific areas differed.*

## 💡 What This Project Demonstrates

| Area | Implementation | Owned By |
|---|---|---|
| **Client-Server Networking** | Custom TCP-based server (`VCControllerServer`) handling concurrent client and vehicle-owner connections | Me |
| **Database Design** | Normalized SQL schema for jobs, transactions, and vehicle/owner records (`sql/vcrts_table.sql`) | Me |
| **Data Persistence** | Dual storage strategy — CSV-based repository and relational database (JDBC + MySQL) behind a common storage interface (`ServerStorageAdapter`) | Me |
| **System Design** | Layered architecture separating controller, model, network, storage, and validation concerns | Team |
| **Object-Oriented Design** | Role-based modeling (`Role`, `Job`, `ClientInput`, `OwnerInput`) with clear separation between data models and business logic | Team |
| **GUI Development** | Java Swing desktop interface with multiple views (`WelcomePage`, `VCControllerGUI`, `VehicularCloudGUI`) | Teammate(s) |
| **Input Validation & Error Handling** | Dedicated validation layer (`InputValidator`, `InputController`) to enforce data integrity across client and console inputs | Teammate(s) |

## 🏗️ Architecture

```
                ┌────────────────────┐
                │   Client / Owner    │
                │  (GUI or Console)   │
                └──────────┬───────────┘
                           │
                 JobClient / VehicleOwnerClient
                           │
                           ▼
                 ┌───────────────────┐
                 │   VCController      │
                 │  VCControllerServer │
                 └──────────┬───────────┘
                           │
                 ┌─────────┴──────────┐
                 ▼                    ▼
         InputValidator        JobStorageManager
                 │                    │
                 ▼                    ▼
           Job / Role       ServerStorageAdapter
                              ┌──────┴───────┐
                              ▼              ▼
                     CSVStorageManager   DatabaseManager
```

## 🛠️ Tech Stack

- **Language:** Java 11+
- **GUI:** Java Swing
- **Database:** MySQL (JDBC)
- **Networking:** Java Sockets (TCP)
- **Persistence:** CSV file storage + relational database
- **Build/Run:** Shell (`run.sh`) and PowerShell (`run.ps1`, `build.ps1`) scripts

## 📂 Project Structure

```
src/main/java/com/vehicularcloud/
├── controller/          # Server logic and request handling
│   └── gui/              # Swing GUI views
├── model/                # Core domain models (Job, Role, Client/Owner input)
├── network/              # TCP client implementations
├── storage/              # CSV & database persistence layer
├── validation/           # Input validation and sanitization
└── VehicularCloudApplication.java   # Entry point
sql/
└── vcrts_table.sql       # Database schema
```

## 🚀 Getting Started

### Prerequisites
- Java JDK 11 or later
- MySQL (optional — required only for database-backed storage)

### Setup

```bash
git clone https://github.com/SadiaSultanaa/vehicular-cloud-resource-trading-system.git
cd vehicular-cloud-resource-trading-system
```

To use database storage, load the schema:
```bash
mysql -u <user> -p < sql/vcrts_table.sql
```

### Run

**macOS / Linux**
```bash
chmod +x run.sh
./run.sh
```

**Windows**
```powershell
./build.ps1
./run.ps1
```

## 🎯 Key Design Decisions

- **Pluggable storage layer** — `ServerStorageAdapter` abstracts persistence so the system can swap between CSV and SQL storage without touching business logic, demonstrating an interface-driven, maintainable design.
- **Separation of client roles** — distinct input flows for vehicle owners vs. job-submitting clients keep validation and permissions logic isolated and testable.
- **Dual interface support** — both console and GUI clients share the same underlying controller and validation logic, avoiding code duplication.

## 📈 Future Improvements

- Add multithreaded job scheduling with priority queues
- Implement authentication and session management
- Add unit test coverage (JUnit)
- Containerize with Docker for easier deployment

## 👤 Author

**Sadia Sultana** — Backend & Database
[GitHub](https://github.com/SadiaSultanaa) · [LinkedIn](https://www.linkedin.com/in/sadia-sultana-11162927b/)

*This repository is maintained under my GitHub account for portfolio purposes; see the [Team & My Contribution](#-team--my-contribution) section above for a breakdown of individual work on this team project.*

## 📄 License

Educational project for SWE course.
