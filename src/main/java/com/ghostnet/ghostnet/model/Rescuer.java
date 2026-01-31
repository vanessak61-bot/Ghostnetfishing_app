package com.ghostnet.ghostnet.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
/**
 * Diese Entity speichert die Informationen über die Personen, 
 * die eine Bergung übernehmen (Name und Telefonnummer).
 */
@Entity 
public class Rescuer {
	@Id //Primärschlüssel; Eindeutige ID für jede bergende Person
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id; 
	
	@NotBlank // Namensfeld der bergenden Person darf nicht leer sein
	private String name;
	
	@NotBlank // Telefonfeld der bergenden Person darf nicht leer sein
	private String phone;
	// Getter- und Settermethoden
	public Long getId() { return id; }
	public void setId(Long id) {this.id = id;}
	
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	
	public String getPhone() {return phone;}
	public void setPhone(String phone) {this.phone = phone;}
}
