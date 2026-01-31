package com.ghostnet.ghostnet.util;

import java.util.regex.Pattern;

//Dies ist die Klasse, die Koordinaten in WGS84-Format umwandelt
public final class CoordinateConverter {
	/** Reguläre Ausdrücke zum Erkennen der Formate:
	 * DMS: Grad Minuten Sekunden (z.B. 52° 30' 00")
	 * DDM: Grad Dezimalminuten (nautisches Format, z.B. 52° 30.5')
	 *  DD:  Dezimalgrad (z.B. 52.5)
	 */
	private static final Pattern DMS_LAT = Pattern.compile("(?i)\\s*([NS])?\\s*(\\d{1,2}\\s*[°\\s]\\s*(\\d{1,2})\\s*['\\s]\\s*(\\d{1,2})"
			+ "(?:\\.\\d+)?)\\s*\"?\\s*([NS])?\\s*");
	private static final Pattern DMS_LON = Pattern.compile("(?i)\\s*([EW])?\\s*(\\d{1,3})\\s*[°\\s]\\s*(\\d{1,2})\\s*['\\s]\\s*(\\d{1,2}"
			+ "(?:\\.\\d+)?)\\s*\"?\\s*([EW])?\\s*");
	
	private static final Pattern DDM_LAT = Pattern.compile("(?i)\\s*([NS])?\\s*(\\d{1,2})\\s*[°\\s]\\s*(\\d{1,2}(?:\\.\\d+)?)\\s*['m]?\\s*(NS)?\\s*");
	private static final Pattern DDM_LON = Pattern.compile("(?i)\\s*([EW])?\\s*(\\d{1,3})\\s*[°\\s]\\s*(\\d{1,2}(?:\\.\\d+)?)\\s*['m]?\\s*(EW)?\\s*");
	
	private static final Pattern DD_LAT = Pattern.compile("(?i)\\s*([+-]?\\d{1,2}(?:\\.\\d+)?)\\s*([NS])?\\s*");
	private static final Pattern DD_LON = Pattern.compile("(?i)\\s*([+-]?\\d{1,3}(?:\\.\\d+)?)\\s*([EW])?\\s*");
	
	private CoordinateConverter() {} // Privater Konstruktor verhindert Instanziierung
	
	//Wandelt einen Breitengrad-String in einen Double-Wert um
	public static double parseLatitude(String input) {
		if (input == null) throw new IllegalArgumentException("Breitengrad fehlt");
		input = input.trim();
		
		// Prüfung auf DMS Format
		var mDms = DMS_LAT.matcher(input);
		if (mDms.matches()) {
			String hemi = firstNonNull(mDms.group(1), mDms.group(5));
			int deg = Integer.parseInt(mDms.group(2));
			int min = Integer.parseInt(mDms.group(3));
			double sec = Double.parseDouble(mDms.group(4));
			double dd = deg + min/60.0 + sec/3600.0;
			return ensureLatRange(applyHemisphere(dd, hemi, true));
		}
		
		//Prüfung auf DDM Format
		var mDdm = DDM_LAT.matcher(input);
		if(mDdm.matches()) {
			String hemi = firstNonNull(mDdm.group(1), mDdm.group(4));
			int deg = Integer.parseInt(mDdm.group(2));
			double minutes = Double.parseDouble(mDdm.group(3));
			double dd = deg + minutes/60.0;
			return ensureLatRange(applyHemisphere(dd, hemi, true));
		}
		
		//Prüfung auf DD Format
		var mDd = DD_LAT.matcher(input);
		if (mDd.matches()) {
			double dd = Double.parseDouble(mDd.group(1));
			String hemi = mDd.group(2);
			if (hemi != null) dd = Math.abs(dd);
			return ensureLatRange(applyHemisphere(dd, hemi, true));
		}
		throw new IllegalArgumentException("Unbekanntes Breitengrad-Format: "+ input);
	}
	
	//Wandelt einen Längengrad-String in einen Double-Wert um
	public static double parseLongitude(String input) {
		if(input == null) throw new IllegalArgumentException("Längengrad fehlt");
		input = input.trim();
		
		var mDms = DMS_LON.matcher(input);
		if (mDms.matches()) {
			String hemi = firstNonNull(mDms.group(1), mDms.group(5));
			int deg = Integer.parseInt(mDms.group(2));
			int min = Integer.parseInt(mDms.group(3));
			double sec = Double.parseDouble(mDms.group(4));
			double dd = deg + min/60.0 + sec/3600.0;
			return ensureLonRange(applyHemisphere(dd, hemi, true));
		}
		var mDdm = DDM_LON.matcher(input);
		if(mDdm.matches()) {
			String hemi = firstNonNull(mDdm.group(1), mDdm.group(4));
			int deg = Integer.parseInt(mDdm.group(2));
			double minutes = Double.parseDouble(mDdm.group(3));
			double dd = deg + minutes/60.0;
			return ensureLonRange(applyHemisphere(dd, hemi, true));
		}
		var mDd = DD_LON.matcher(input);
		if (mDd.matches()) {
			double dd = Double.parseDouble(mDd.group(1));
			String hemi = mDd.group(2);
			if (hemi != null) dd = Math.abs(dd);
			return ensureLonRange(applyHemisphere(dd, hemi, true));
		}
		throw new IllegalArgumentException("Unbekanntes Längengrad-Format:" + input);
	}
	// Hilfsmethode zur Bestimmung der Hemisphäre (N/S, E/W)
	private static String firstNonNull(String a, String b) {
		return a != null ? a : b;
	}
	// Wandelt den Wert basierend auf der Hemisphäre in positive/negative Zahlen um
	private static double applyHemisphere(double dd, String hemi, boolean lat) {
		if (hemi == null) return dd; 
		hemi = hemi.toUpperCase();
		if (lat) {
			return "S".equals(hemi) ? -Math.abs(dd) : Math.abs(dd);
		} else {
			return "W".equals(hemi) ? -Math.abs(dd) : Math.abs(dd);
		}
	}
	// Breitengrade dürfen nur zwischen -90 und 90 liegen
	private static double ensureLatRange(double dd) {
		if (dd < -90 || dd > 90) throw new IllegalArgumentException("Breitengrad außerhalb des Bereichs [-90,90]");
		return dd;
	}
	// Längengrade dürfen nur zwischen -180 und 180 liegen
	private static double ensureLonRange(double dd) {
		if (dd < -180 || dd > 180) throw new IllegalArgumentException("Längengrad außerhalb des Bereiches [-180,180]");
		return dd;
	}
	//Nautische Ausgabe als DDM-String
	public static String toNauticalDDM(double lat, double lon) {
		return formatDDM(lat, true) + " , " + formatDDM(lon, false);
	}
	private static String formatDDM(double dd, boolean lat) {
		double abs = Math.abs(dd);
		int deg = (int) Math.floor(abs);
		double minutes = (abs - deg) * 60.0;
		char hemi = lat ? (dd >= 0 ? 'N' : 'S') : (dd >= 0 ? 'E' : 'W');
		if (lat) return String.format("%02d° %06.3f' %c", deg, minutes, hemi);
		else     return String.format("%03d° %06.3f' %c", deg, minutes, hemi);
	}
}
