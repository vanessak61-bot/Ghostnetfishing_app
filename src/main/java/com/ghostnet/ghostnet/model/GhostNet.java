package com.ghostnet.ghostnet.model;

import jakarta.persistence.*;
import java.time.Instant;
/**
 * Die Entity-Klasse GhostNet definiert, welche Daten über ein Netz 
 * in der Datenbank gespeichert werden.
 */
@Entity // Markiert diese Klasse für die Datenbank als Tabelle
public class GhostNet {
	@Id // Primärschlüssel für jedes Netz
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	
	private Double latitude;
	private Double longitude;
	
	private String sizeEstimate;
	
	@Enumerated(EnumType.STRING) // Speichert den Status als Text in der Datenbank
	private GhostNetStatus status = GhostNetStatus.REPORTED;
	
	//Meldende Person (optional, anonyme Meldung möglich)
	private String reporterName;
	private String reporterPhone; 
	
	@ManyToOne // Verknüpfung: 1:N Beziehung Viele Netze können demselben Bergungsschwimmer zugeordnet sein
	private Rescuer assignedRescuer;
	private Instant reportedAt = java.time.Instant.now();
	private Instant updatedAt;
	
	@PreUpdate
	public void onUpdate() {this.updatedAt = java.time.Instant.now();}
	// Getter und Setter Methoden
    // Dienen dazu, dass andere Klassen sicher auf die privaten Felder zugreifen können
	
	public Long getId() { return id;}
	public void setId(Long id) {this.id = id;}
	
	public Double getLatitude() {return latitude;}
	public void setLatitude( Double latitude) {this.latitude = latitude;}
	
	public Double getLongitude() {return longitude;}
	public void setLongitude(Double longitude) {this.longitude = longitude;}
	
	public String getSizeEstimate() {return sizeEstimate;}
	public void setSizeEstimate(String sizeEstimate) {this.sizeEstimate = sizeEstimate;}
	
	public GhostNetStatus getStatus() {return status;}
	public void setStatus(GhostNetStatus status) {this.status = status;}
	
	public String getReporterName() {return reporterName;}
	public void setReporterName(String reporterName) {this.reporterName = reporterName;}

	public String getReporterPhone() {return reporterPhone;}
	public void setReporterPhone(String reporterPhone) {this.reporterPhone = reporterPhone;}
	
	public Rescuer getAssignedRescuer() {return assignedRescuer;}
	public void setAssignedRescuer(Rescuer assignedRescuer) {this.assignedRescuer = assignedRescuer;}
	
	public Instant getReportedAt() {return reportedAt;}
	public Instant getUpdatedAt() {return updatedAt;}

}
