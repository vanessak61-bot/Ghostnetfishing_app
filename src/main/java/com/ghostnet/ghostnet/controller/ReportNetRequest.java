package com.ghostnet.ghostnet.controller;

/**
 * Daten-Container für das Melde-Formular.
 * Nimmt die Informationen eines neu entdeckten Geisternetzes entgegen.
 */
public record ReportNetRequest(
		/**
		 * Koordinaten als String, damit verschiedene Formate (z.B. 48.123 oder 48° 10') 
		 * eingegeben und später vom Converter verarbeitet werden können.
		 */
		String latitude,		//beliebige Eingabe: DD,DDM, DMS
		String longitude,		//beliebige Eingabe: DD, DDM, DMS
		String sizeEstimate,
		String reporterName,
		String reporterPhone) {

}
