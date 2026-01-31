package com.ghostnet.ghostnet.service;

import com.ghostnet.ghostnet.model.*;
import com.ghostnet.ghostnet.repository.*;
import com.ghostnet.ghostnet.util.CoordinateConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class GhostNetService {

    private final GhostNetRepository nets;
    private final RescuerRepository rescuers;
    
    // Konstruktor für die Abhängigkeiten (Repositorys)
    public GhostNetService(GhostNetRepository nets, RescuerRepository rescuers) {
        this.nets = nets;
        this.rescuers = rescuers;
    }

    // Ein neues Netz im System registrieren
    public GhostNet reportNet(String latInput, String lonInput, String size, String reporterName, String reporterPhone) {
        // Umwandlung der Eingabewerte in Zahlenformate
        double lat = CoordinateConverter.parseLatitude(latInput);
        double lon = CoordinateConverter.parseLongitude(lonInput);
        
        GhostNet net = new GhostNet();
        net.setLatitude(lat);
        net.setLongitude(lon);
        net.setSizeEstimate(size);
        net.setStatus(GhostNetStatus.REPORTED); // Standard-Status bei Meldung
        net.setReporterName(reporterName);
        net.setReporterPhone(reporterPhone);
        
        return nets.save(net);
    }

    // Ein Netz für eine Bergung durch eine Person reservieren
    public GhostNet claimNet(Long netId, String rescuerName, String rescuerPhone) {
        GhostNet net = nets.findById(netId)
                .orElseThrow(() -> new IllegalArgumentException("Netz nicht gefunden"));
        
        // Prüfen, ob das Netz überhaupt noch zur Verfügung steht
        if (net.getAssignedRescuer() != null)
            throw new IllegalStateException("Netz ist bereits einer bergenden Person zugewiesen");
        
        if (net.getStatus() == GhostNetStatus.RECOVERED || net.getStatus() == GhostNetStatus.LOST)
            throw new IllegalStateException("Netz ist bereits abgeschlossen");
        
        // Neuen Rescuer-Eintrag erstellen
        Rescuer rescuer = new Rescuer();
        rescuer.setName(rescuerName);
        rescuer.setPhone(rescuerPhone);
        rescuer = rescuers.save(rescuer);
        
        // Netz aktualisieren
        net.setAssignedRescuer(rescuer);
        net.setStatus(GhostNetStatus.RECOVERY_SCHEDULED);
        
        return nets.save(net);
    }

    // Gibt alle Netze zurück, die noch niemandem zugewiesen sind
    public List<GhostNet> listAvailable(){
        return nets.findByAssignedRescuerIsNullAndStatus(GhostNetStatus.REPORTED);
    }

    // Ein Netz final als erfolgreich geborgen markieren
    public GhostNet markRecovered(Long netId, Long rescuerId) {
        GhostNet net = nets.findById(netId)
            .orElseThrow(() -> new IllegalArgumentException("Netz nicht gefunden"));

        // Statusänderung auf "Geborgen"
        net.setStatus(GhostNetStatus.RECOVERED);
        return nets.save(net);
    }

    // Ein Netz als verschollen markieren (erfordert Namen des Meldenden)
    public GhostNet markLost(Long netId, String reporterName) {
        // Validierung: Der Name darf nicht leer sein
        if (reporterName == null || reporterName.isBlank()) {
            throw new IllegalArgumentException("Verschollen melden erfordert zwingend einen Namen.");
        }

        GhostNet net = nets.findById(netId)
                .orElseThrow(() -> new IllegalArgumentException("Netz nicht gefunden"));
        
        // Status auf "Verschollen" setzen und Melder hinterlegen
        net.setStatus(GhostNetStatus.LOST);
        net.setReporterName(reporterName);

        return nets.save(net);
    }
}