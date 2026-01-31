package com.ghostnet.ghostnet.controller;

// für schöne Antworten inkl. nautischer DDM-Darstellung
import com.ghostnet.ghostnet.model.GhostNet;
import com.ghostnet.ghostnet.model.GhostNetStatus;
import com.ghostnet.ghostnet.util.CoordinateConverter;
/**
 * Dieses DTO (Data Transfer Object) bereitet die Daten aus der Datenbank 
 * so vor, dass sie vom Frontend verarbeitet werden können.
 */
public record GhostNetDto(
        Long id,
        double latitude,
        double longitude,
        String nauticalDDM,// Enthält die Koordinaten bereits im nautischen Format
        String sizeEstimate,
        GhostNetStatus status,
        String reporterName,   
        String reporterPhone   
) {
    public static GhostNetDto from(GhostNet g) {
        return new GhostNetDto(
                g.getId(),
                g.getLatitude(),
                g.getLongitude(),
                // Hier wird der Converter genutzt, um die Grad-Zahlen umzurechnen
                CoordinateConverter.toNauticalDDM(g.getLatitude(), g.getLongitude()),
                g.getSizeEstimate(),
                g.getStatus(),
                g.getReporterName(),  
                g.getReporterPhone()  
        );
    }
}