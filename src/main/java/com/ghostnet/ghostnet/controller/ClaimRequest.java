package com.ghostnet.ghostnet.controller;
/**
 * Ein Data Transfer Object (DTO), realisiert als Java Record.
 * Dient dazu, die vom Frontend gesendeten Daten (Name und Telefonnummer) 
 * bei der Reservierung eines Netzes strukturiert entgegenzunehmen.
 */
public record ClaimRequest(String rescuerName, String rescuerPhone) {
}
/**Ein Record ist unveränderlich und eignet sich daher 
* für den Datenaustausch zwischen API und Service
*/