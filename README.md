# NGO Geisternetz-Management System (Prototyp)
(english version below: https://github.com/vanessak61-bot/Ghostnetfishing_app/blob/main/README_EN.md)
Dieses Projekt steht unter MIT-Lizenz
Dieser Prototyp dient der Erfassung und Nachverfolgung von Geisternetzen. Die Anwendung ermöglicht es NGOs und Bergungsteams, den Status von Netzen von der Meldung bis zur Bergung zu verwalten.

## Technologiestack
- **Backend:** Java 17, Spring Boot, Spring Data JPA
- **Datenbank:** H2 (Relational, Dateibasiert persistent)
- **Frontend:** HTML5, CSS3, JavaScript (Fetch API)

## Umgesetzte Anforderungen
Im Rahmen des ersten Sprints wurden folgende 5 Kernfunktionen umgesetzt:
1. **Netz-Meldung:** Erfassung von Koordinaten (DDM-Format) und geschätzter Größe.
2. **Dashboard:** Übersicht aller Netze mit Filterfunktion nach Status.
3. **Reservierung:** Einem gemeldeten Netz einen Bergungsschwimmer (Rescuer) zuweisen.
4. **Bergung abschließen:** Statusänderung auf "GEBORGEN" unter Angabe der Rescuer-ID.
5. **Verschollen-Meldung:** Markierung von unauffindbaren Netzen als "VERSCHOLLEN" (mit Namenspflicht).

## Startanleitung
1. Das Projekt in eine IDE (z. B. Eclipse) importieren.
2. Die Anwendung über die Datei `GhostnetApplication.java` (Run as Java Application) starten.
3. Im Browser folgende URL aufrufen: `http://localhost:8080/index.html`

## 👤 Test-Daten
Für die Bergung ist ein Test-Rescuer bereits im System hinterlegt:
- **Rescuer-ID:** `1`
- **Name:** Team Ocean Care
