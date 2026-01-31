package com.ghostnet.ghostnet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ghostnet.ghostnet.model.*;
import java.util.List;

//Das Interface dient als Brücke zur Datenbank für die GhostNet-Objekte.
public interface GhostNetRepository extends JpaRepository<GhostNet, Long>{
	// Findet alle Netze, die noch niemandem zugewiesen sind und einen bestimmten Status haben
	List<GhostNet> findByAssignedRescuerIsNullAndStatus(GhostNetStatus status);
	
	// Findet alle Netze anhand ihres aktuellen Status
	List<GhostNet> findByStatus(GhostNetStatus status);

}
