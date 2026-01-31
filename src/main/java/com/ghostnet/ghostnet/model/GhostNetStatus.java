package com.ghostnet.ghostnet.model;
//Alle Zustände die ein Ghostnet annehmen kann
public enum GhostNetStatus {
	REPORTED,             //Netz wurde gemeldet
	RECOVERY_SCHEDULED,   // Netz wurde zur Bergung gemerkt
	RECOVERED,            // Netz wurde Geborgen
	LOST				  // Netz ist verschollen
}
