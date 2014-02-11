import java.util.regex.Matcher;
import java.util.regex.Pattern;

// (\"\w+\":\"*[\w\d\.\-\s]*\"*)

public class JSONParser {
	private static void printBuses(BusStop[] stops) {
		for(int i = 0; i < stops.length; i++) {
			System.out.println(stops[i]);
		}
	}

	public static BusStop[] parse(String json) {
		BusStop[] stops = null;
		int filledStops = 0;
		Matcher stopsMatcher = Pattern.compile("\"stops\":\\[([\\w\\W]+)\\]").matcher(json);
		if(stopsMatcher.find()) {
			String stopsStr = stopsMatcher.group(1);
			//next, find all stops themselves
			Matcher stopsInfoMatcher = Pattern.compile("\\{([\\w\"\\-\\d\\s\\.:,])*\\}").matcher(stopsStr);
			int stopsCount = 0;
			while(stopsInfoMatcher.find()) {
				stopsCount++;
			}
			stopsInfoMatcher.reset();
			stops = new BusStop[stopsCount]; // check that it's not zero

			while(stopsInfoMatcher.find()) {
				String lat = "", lon = "", atco = "";
				long distance = 0;
				String infoBlock = stopsInfoMatcher.group();
				Matcher infoElementMatcher = Pattern.compile("\"(\\w+)\":\"*([\\w\\d\\.\\-\\s]*)\"*").matcher(infoBlock);
				while(infoElementMatcher.find()) {
					String elementName = infoElementMatcher.group(1);
					String elementContent = infoElementMatcher.group(2);
					if (elementName.equals("atcocode")) {
						atco = elementContent; // need to strip "s
					} else if (elementName.equals("latitude")) {
						lat = elementContent;
					} else if (elementName.equals("longitude")) {
						lon = elementContent;
					} else if (elementName.equals("distance")) {
						distance = Long.parseLong(elementContent, 10);
					}
				}
				stops[filledStops++] = new BusStop(lat, lon, atco, distance);
			}
		} else {
			System.out.println("ERROR! 1");
		}
		

		return stops;
	}

	public static void main(String[] args) {
		BusStop[] stops = parse(DataFetcher.fetchJSON("53.794333", "-1.7675"));

		printBuses(stops);
	}
}
