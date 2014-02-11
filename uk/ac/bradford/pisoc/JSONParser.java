package uk.ac.bradford.pisoc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

class JSONParser {
	private static Pattern
		JSONElementPattern = Pattern.compile("\"(\\w+)\":\"*([\\w\\d\\.\\-\\s]*)\"*");

	private static void printBuses(BusStop[] stops) {
		for(int i = 0; i < stops.length; i++) {
			System.out.println(stops[i]);
		}
	}

	private static String getStopsJSON(String json) {
		Matcher stopsMatcher = Pattern.compile("\"stops\":\\[([\\w\\W]+)\\]").matcher(json);
		if(stopsMatcher.find()) {
			return stopsMatcher.group(1);
		} else {
			return ""; // thow exception here?
		}
	}

	private static String[] getStopsInfo(String json) {
		String[] stops = null;
		int filledStops = 0;
		
		//next, find all stops themselves
		Matcher stopsInfoMatcher = Pattern.compile("\\{([\\w\"\\-\\d\\s\\.:,])*\\}").matcher(json);
		int stopsCount = 0;
		while(stopsInfoMatcher.find()) {
			stopsCount++;
		}
		stopsInfoMatcher.reset();
		stops = new String[stopsCount]; // check that it's not zero

		while(stopsInfoMatcher.find()) {
			String infoBlock = stopsInfoMatcher.group();
			stops[filledStops++] = infoBlock;
		}
		
		return stops;
	}

	private static BusStop parseAStop(String json) {
		String lat = "", lon = "", atco = "";
		long distance = 0;
		Matcher infoElementMatcher = JSONElementPattern.matcher(json);
		while(infoElementMatcher.find()) {
			String elementName = infoElementMatcher.group(1);
			String elementContent = infoElementMatcher.group(2);
			if (elementName.equals("atcocode")) {
				atco = elementContent;
			} else if (elementName.equals("latitude")) {
				lat = elementContent;
			} else if (elementName.equals("longitude")) {
				lon = elementContent;
			} else if (elementName.equals("distance")) {
				distance = Long.parseLong(elementContent, 10);
			}
		}

		return new BusStop(lat,lon,atco,distance);
	}

	
	public static BusStop parseNearest(String json, String bus) {
		String[] stops = getStopsInfo(getStopsJSON(json));

		for (String infoBlock : stops) {
			BusStop s = parseAStop(infoBlock);
			if(s.hasBus(bus)) {
				return s;
			}
		}

		return null;
	}

	public static BusStop[] parseNearestAsArray(String json) {
		String[] stops = getStopsInfo(getStopsJSON(json));
		int filledStops = 0;
		BusStop[] busStops = new BusStop[stops.length];
		for(String infoBlock : stops) {
			busStops[filledStops++] = parseAStop(infoBlock);
		}
		
		return busStops;
	}
	

	public static String[] parseStop(String json) {
		Matcher infoElementMatcher = JSONElementPattern.matcher(json);
		Set<String> busSet = new HashSet<String>();
		while(infoElementMatcher.find()) {
			String elementName = infoElementMatcher.group(1);
			String elementContent = infoElementMatcher.group(2);
			if (elementName.equals("line")) {
				busSet.add(elementContent);
			}
		}

		String[] buses = new String[busSet.size()];
		int count = 0;
		Iterator iter = busSet.iterator();
		while(iter.hasNext()) {
			buses[count++] = (String) iter.next();
		}
		
		return buses;
	}

}
