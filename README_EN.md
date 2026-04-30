# NGO Ghost Net Management System (Prototyp)

A management tool to track and recover ghost nets, featuring automated coordinate conversion

## Technologiestack
- **Backend:** Java 17, Spring Boot, Spring Data JPA
- **data base:** H2 (relational, database-driven, persistent)
- **Frontend:** HTML5, CSS3, JavaScript (Fetch API)

## Implemented Features
In the first development sprint, the following core functions were implemented:
1. **reporting:** Reporting of ghost nets using DDM coordinates (degrees, decimal minutes) and estimated size
2. **coordinate conversion:** Automated conversion of nautical DDM coordinates to WGS84 decimal format to ensure precise mapping and recovery
3. **dashboard:** Overview of all reported nets with status filtering
4. **reservation:** Assigning a Recovery Diver (rescuer) to a reported net.
5. **recovery completion:** updating status to "Recovered" including Rescuer ID
6. **missing report:** marking nets that could´nt be located as "lost" (with requirement to provide the name).

## Getting Started
1. Import the project into an IDE (e.g., Eclipse).
2. Run the application using the `GhostnetApplication.java` file (Run as Java Application).
3. Open the following URL in your browser: `http://localhost:8080/index.html`
## Test Data
A test rescuer has already been added to the system for the rescue:
- **Rescuer ID:** `1`
- **Name:** Team Ocean Care
