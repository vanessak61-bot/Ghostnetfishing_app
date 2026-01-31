/**
 * Zentrale Steuerung für Dashboard, Berichterstattung und Bergungsmanagement.
 * Kommuniziert mit dem Spring Boot Backend unter Port 8080.
 */

const API = "http://localhost:8080/api/nets";

// 1. Initialisierung beim Laden der Seite

document.addEventListener("DOMContentLoaded", () => {
    // Erkennt automatisch, auf welcher Seite wir uns befinden anhand der IDs
    if (document.getElementById("netList")) {
        loadAllNets();
    }
    
    const reportForm = document.getElementById("reportForm");
    if (reportForm) {
        initReportForm(reportForm);
    }
    
    if (document.getElementById("detailsContent")) {
        loadDetails();
    }
});

// 2. Dashboard & Filter-Logik (index.html)

async function loadAllNets(statusFilter = "") {
    let url = statusFilter ? `${API}?status=${statusFilter}` : API;
    const container = document.getElementById("netList");
    
    try {
        const res = await fetch(url);
        if (!res.ok) throw new Error(`HTTP-Fehler: ${res.status}`);
        const nets = await res.json();
        
        if (nets.length === 0) {
            container.innerHTML = "<p>Keine Netze unter diesen Kriterien gefunden.</p>";
            return;
        }

        // Mapping der Status-Farben für konsistentes UI-Feedback
        const statusColors = {
            'REPORTED': '#f39c12',           // Orange: Handlungsbedarf
            'RECOVERY_SCHEDULED': '#3498db', // Blau: In Arbeit
            'RECOVERED': '#2ecc71',          // Grün: Erfolgreich abgeschlossen
            'LOST': '#e74c3c'                // Rot: Nicht auffindbar
        };

        container.innerHTML = nets.map(n => {
            const currentColor = statusColors[n.status] || '#95a5a6';
            return `
                <div class="net-card">
                    <div>
                        <strong>Netz ID: ${n.id}</strong><br>
                        <small>Position: ${n.nauticalDDM}</small><br>
                        <span>Größe: ${n.sizeEstimate}</span>
                    </div>
                    <div style="text-align: right;">
                        <span class="status-badge" style="background: ${currentColor}; color: white; padding: 4px 8px; border-radius: 12px; font-size: 0.8em; font-weight: bold;">
                            ${n.status}
                        </span>
                        <br>
                        <a href="details.html?id=${n.id}">
                            <button style="margin-top:10px; cursor:pointer; background: #002b36;">Details & Aktion</button>
                        </a>
                    </div>
                </div>
            `;
        }).join("");
    } catch (err) {
        console.error("Fetch-Fehler:", err);
        container.innerHTML = `<p style="color:red;">⚠️ Verbindung zum Backend fehlgeschlagen. Bitte Server prüfen.</p>`;
    }
}

function applyFilters() {
    const status = document.getElementById("filterStatus").value;
    loadAllNets(status);
}

// 3. Netz Melden (report.html)

function initReportForm(form) {
    form.addEventListener("submit", async (e) => {
        e.preventDefault(); // Verhindert Neuladen der Seite
        const formData = new FormData(form);
        const data = {
            latitude: parseFloat(formData.get("latitude")),
            longitude: parseFloat(formData.get("longitude")),
            sizeEstimate: formData.get("sizeEstimate"),
            reporterName: formData.get("reporterName") || null,
            reporterPhone: formData.get("reporterPhone") || null
        };

        try {
            const res = await fetch(API, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data)
            });

            if (res.ok) {
                form.reset();
                document.getElementById("result").innerHTML = 
                    "<div style='background: #859900; color: white; padding: 10px; margin-top: 10px; border-radius: 5px;'>✅ Erfolgreich gemeldet! Vielen Dank für deinen Beitrag.</div>";
            } else {
                alert("Fehler beim Senden der Daten.");
            }
        } catch (err) {
            alert("Server-Fehler: Überprüfe deine Internetverbindung oder den Serverstatus.");
        }
    });
}

// 4. Detailansicht & Aktionen (details.html)

async function loadDetails() {
    const id = new URLSearchParams(window.location.search).get("id");
    if (!id) return;

    try {
        const res = await fetch(API); 
        const all = await res.json();
        // Suchen des spezifischen Objekts im Array 
        const net = all.find(n => n.id === Number(id));

        if (!net) {
            document.getElementById("detailsContent").innerHTML = "<p>Netz nicht gefunden.</p>";
            return;
        }

        // Mapping der Datenfelder in das UI
        document.getElementById("netIdTitle").innerText = "Geisternetz #" + net.id;
        document.getElementById("viewStatus").innerText = net.status;
        document.getElementById("viewCoords").innerText = net.nauticalDDM;
        document.getElementById("viewSize").innerText = net.sizeEstimate;
        document.getElementById("viewReporterName").innerText = net.reporterName || "Anonym";
        document.getElementById("viewReporterPhone").innerText = net.reporterPhone || "Nicht angegeben";

        // Dynamische Anzeige der Aktions-Sektionen basierend auf dem Status
        const actionsArea = document.getElementById("actionsArea");
        const claimSec = document.getElementById("claimSection");
        const recoverSec = document.getElementById("recoverSection");
        const lostSec = document.getElementById("lostSection");

        actionsArea.style.display = "block";
        claimSec.style.display = (net.status === "REPORTED") ? "block" : "none";
        recoverSec.style.display = (net.status === "RECOVERY_SCHEDULED") ? "block" : "none";

        // Ein Netz kann nur als verschollen gemeldet werden, wenn es noch nicht geborgen wurde
        lostSec.style.display = (net.status === "REPORTED" || net.status === "RECOVERY_SCHEDULED") ? "block" : "none";

    } catch (err) {
        console.error("Detail-Ladefehler:", err);
    }
}

// POST-Requests für Statusänderungen
async function handleClaim() {
    const id = new URLSearchParams(window.location.search).get("id");
    const name = document.getElementById("resName").value.trim();
    const phone = document.getElementById("resPhone").value.trim();

    if (!name) return alert("Bitte gib deinen Namen für die Reservierung an!");

    try {
        const res = await fetch(`${API}/${id}/claim`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ rescuerName: name, rescuerPhone: phone })
        });
        if (res.ok) location.reload();
        else alert("Fehler beim Reservieren des Netzes.");
    } catch (err) {
        alert("Server nicht erreichbar.");
    }
}

async function handleRecover() {
    const id = new URLSearchParams(window.location.search).get("id");
    const resId = document.getElementById("resId").value.trim();

    if (!resId) return alert("Bitte gib deine Rescuer-ID ein!");

    try {
        const res = await fetch(`${API}/${id}/recover`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ rescuerId: Number(resId) })
        });
        if (res.ok) {
            alert("Status: GEBORGEN. Vielen Dank für den Einsatz!");
            window.location.href = "index.html";
        } else {
            alert("Fehler bei der Bergung. Bitte Rescuer-ID prüfen.");
        }
    } catch (err) {
        alert("Server nicht erreichbar.");
    }
}

async function handleLost() {
    const id = new URLSearchParams(window.location.search).get("id");
    const nameInput = document.getElementById("lostName");

    if (!nameInput || !nameInput.value.trim()) {
        alert("Bitte gib einen Namen an. Verschollen melden darf nicht anonym erfolgen.");
        return;
    }

    try {
        const response = await fetch(`${API}/${id}/lost`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ reporterName: nameInput.value.trim(), reporterPhone: "" })
        });

        if (response.ok) {
            alert("Status auf VERSCHOLLEN gesetzt.");
            window.location.href = "index.html";
        } else {
            alert("Fehler beim Aktualisieren des Status.");
        }
    } catch (err) {
        alert("Server nicht erreichbar.");
    }
}