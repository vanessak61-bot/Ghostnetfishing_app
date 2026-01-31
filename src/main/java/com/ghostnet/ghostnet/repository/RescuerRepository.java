package com.ghostnet.ghostnet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ghostnet.ghostnet.model.Rescuer;

//Schnittstelle zur Datenbank für die Verwaltung der bergenden Personen
public interface RescuerRepository extends JpaRepository<Rescuer, Long>{
}
