package uk.ac.bradford.pisoc;

public class BusStop {

	protected BusStop(String lat, String lon, String atco, long distance) {
		this.lat = lat;
		this.lon = lon;
		this.atco = atco;
		this.distance = distance;
		buses = JSONParser.parseStop(DataFetcher.fetchStopInfo(atco));
	}
	
	/**
	   Returns the latitude of the bus stop.
	   @return Bus stop's latitude.
	 */
	public String getLat() {
		return lat;
	}

	/**
	   Returns the longitude of the bus stop.
	   @return Bus stop's longitude.
	 */
	public String getLon() {
		return lon;
	}

	/**
	   Returns the atcocode of the bus stop.
	   @return Bus stop's atcocode.
	 */
	public String getAtco() {
		return atco;
	}

	/**
	   Returns the distance from the bus stop to the original destination point.
	   @return The distance between the bus stop and the destinations point.
	 */
	public long getDistance() {
		return distance;
	}

	/**
	   Checks whether the supplied bus number stops at this bus stop.
	   @return true if the bus stops at bus stop; false otherwise.
	 */
	public boolean hasBus(String bus) {
		for (int i = 0; i < buses.length; i++) {
			if (buses[i].equals(bus)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		String result =  String.format("A bus stop with atcocode %s, located at %s ; %s, approximately %d meters from your destination. ", atco, lat, lon, distance);
		result += "Buses that stop at this stop: ";
		for(int i = 0; i < buses.length - 1; i++) {
			result += buses[i] + ", ";
		}
		result += buses[buses.length - 1];
		return result;
	}

	private String lat;
	private String lon;
	private String atco;
	private long distance;
	private String[] buses;

}
