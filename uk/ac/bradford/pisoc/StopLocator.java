package uk.ac.bradford.pisoc;

public class StopLocator {

	/**
	   Returns the nearest bus stop to a point specified by latitude and longitude, taking the bus we're travelling by into account.
	   @param lat The latitude of the destination coordinate.
	   @param lon The longitude of the destination coordinate.
	   @param bus The number of the bus line of interest.
	   @return A BusStop object representing the nearest bus stop to the specified coordinates.
	   @see BusStop
	 */
	public static BusStop getNearest(String lat, String lon, String bus) {
		return JSONParser.parseNearest(DataFetcher.fetchNearestStops(lat, lon), bus);
	}
	
}
