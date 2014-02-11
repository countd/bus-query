package uk.ac.bradford.pisoc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.net.ProtocolException;
import java.net.MalformedURLException;

class JSONParser {
	private static Pattern
		JSONElementPattern = Pattern.compile("\"(\\w+)\":\"*([\\w\\d\\.\\-\\s]*)\"*");

	private static String getStopsJSON(String json) 
		throws MalformedServerResponseException {
		Matcher stopsMatcher = Pattern.compile("\"stops\":\\[([\\w\\W]+)\\]").matcher(json);
		if(stopsMatcher.find()) {
			return stopsMatcher.group(1);
		} else {
			throw new MalformedServerResponseException("Unable to find any bus stops in JSON");
		}
	}

	private static String[] getStopsInfo(String json) 
		throws NoBusStopFoundException {
		String[] stops = null;
		int filledStops = 0;
		
		//next, find all stops themselves
		Matcher stopsInfoMatcher = Pattern.compile("\\{([\\w\"\\-\\d\\s\\.:,])*\\}").matcher(json);
		int stopsCount = 0;
		while(stopsInfoMatcher.find()) {
			stopsCount++;
		}
		stopsInfoMatcher.reset();

		if (stopsCount == 0) {
			throw new NoBusStopFoundException("No bus stops extracted from JSON.");
		}

		stops = new String[stopsCount];

		while(stopsInfoMatcher.find()) {
			String infoBlock = stopsInfoMatcher.group();
			stops[filledStops++] = infoBlock;
		}
		
		return stops;
	}

	private static BusStop parseAStop(String json)
		throws UnableToContactServerException,
			   ProtocolException, MalformedURLException {
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

	
	public static BusStop parseNearest(String json, String bus) 
		throws NoBusStopFoundException, MalformedServerResponseException,
			   UnableToContactServerException, ProtocolException,
			   MalformedURLException {
		String[] stops = getStopsInfo(getStopsJSON(json));

		for (String infoBlock : stops) {
			BusStop s = parseAStop(infoBlock);
			if(s.hasBus(bus)) {
				return s;
			}
		}

		throw new NoBusStopFoundException("Could not find any bus stops sufficiently close.");
	}

	public static BusStop[] parseNearestAsArray(String json) 
		throws MalformedServerResponseException, NoBusStopFoundException, 
			   UnableToContactServerException, ProtocolException,
			   MalformedURLException {
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
