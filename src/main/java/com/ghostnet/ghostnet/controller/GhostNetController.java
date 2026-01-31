package com.ghostnet.ghostnet.controller;

import com.ghostnet.ghostnet.model.GhostNet;
import com.ghostnet.ghostnet.model.GhostNetStatus;
import com.ghostnet.ghostnet.repository.GhostNetRepository;
import com.ghostnet.ghostnet.service.GhostNetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

@CrossOrigin(origins = "*") // // Erlaubt, dass das Frontend die Daten vom Server abrufen darf
@RestController
@RequestMapping("/api/nets")
public class GhostNetController {
	private final GhostNetService service;
	private final GhostNetRepository nets;
	
	// Konstruktor: Hier werden Service und Repository geladen
	public GhostNetController(GhostNetService service, GhostNetRepository nets) {
		this.service = service;
		this.nets = nets;
	}
// Neues Netz wird gemeldet
	@PostMapping
	public ResponseEntity<GhostNetDto> report(@RequestBody ReportNetRequest req){
		GhostNet net = service.reportNet(req.latitude(), req.longitude(), req.sizeEstimate(),
				req.reporterName(), req.reporterPhone());
		return ResponseEntity.ok(GhostNetDto.from(net));
	}
	//Gemeldetes Netz wird für die Bergung reserviert
	@PostMapping("/{id}/claim")
	public ResponseEntity<GhostNetDto> claim(@PathVariable Long id, @RequestBody ClaimRequest req){
		GhostNet net = service.claimNet(id, req.rescuerName(),req.rescuerPhone());
		return ResponseEntity.ok(GhostNetDto.from(net));
	}
	//Verfügbare Netze werden angezeigt
	@GetMapping("/available")
	public List<GhostNetDto> available(){
		return service.listAvailable().stream().map(GhostNetDto::from).toList();
	}
	// Netzstatus wird als geborgen gemeldet
	@PostMapping("/{id}/recover")
	public ResponseEntity<GhostNetDto> recover(@PathVariable Long id, @RequestBody RecoverRequest req){
		GhostNet net = service.markRecovered(id, req.rescuerId());
		return ResponseEntity.ok(GhostNetDto.from(net));
	}
	//Netz wird als verschollen gemeldet
	@PostMapping("{id}/lost")
	public ResponseEntity<GhostNetDto> lost(@PathVariable Long id, @RequestBody LostRequest req){
		GhostNet net = service.markLost(id, req.reporterName());
		return ResponseEntity.ok(GhostNetDto.from(net));
	}
	//Liste aller Netze abrufen; kann nach Status gefiltert werden
	@GetMapping
	public List<GhostNetDto> list(@RequestParam(required = false)GhostNetStatus status){
		return (status == null ? nets.findAll() : nets.findByStatus(status))
				.stream().map(GhostNetDto::from).toList();
	}
}
