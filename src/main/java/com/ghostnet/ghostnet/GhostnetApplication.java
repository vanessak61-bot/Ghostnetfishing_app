package com.ghostnet.ghostnet;

import com.ghostnet.ghostnet.model.Rescuer;
import com.ghostnet.ghostnet.repository.RescuerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//Hauptklasse der Spring Boot Anwendung. 
//Aktiviert Auto-Configuration, Component Scan und den eingebetteten Webserver.
public class GhostnetApplication {

    public static void main(String[] args) {
        SpringApplication.run(GhostnetApplication.class, args);
    }
    /**
     * Initialisierung von Testdaten beim Anwendungsstart.
     * Der CommandLineRunner wird ausgeführt, sobald der Spring-Kontext geladen ist.
     */
    @Bean
    CommandLineRunner initDatabase(RescuerRepository rescuerRepo) {
        return args -> {
            // Prüft, ob schon Rescuer existieren
            if (rescuerRepo.count() == 0) {
                Rescuer testTeam = new Rescuer();
                testTeam.setName("Team Ocean Care");
                testTeam.setPhone("+49 123 456789");
                
                // Speichert den Rescuer in der H2-Datenbank (Persistenz-Layer)
                Rescuer saved = rescuerRepo.save(testTeam);
                
                // Konsolen-Ausgabe als Orientierungshilfe
                System.out.println("=========================================");
                System.out.println("INITIALISIERUNG: Test-Rescuer angelegt!");
                System.out.println("Name: " + saved.getName());
                System.out.println("Verfügbare ID für Bergung: " + saved.getId());
                System.out.println("=========================================");
            }
        };
    }
}