package uk.ac.bradford.pisoc;

import java.net.ProtocolException;
import java.net.MalformedURLException;

public class BusStop {

	protected BusStop(String lat, String lon, String atco, long distance) 
		throws UnableToContactServerException, ProtocolException,
			   MalformedURLException {
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
	   @return The distance between the bus stop and the destinations point in meters.
	 */
	public long getDistance() {
		return distance;
	}

	/**
	   Returns the approximate distance between the bus stop and coordinates lat and lon, computed using the Haversine formula.
	   Exceptionally inaccurate.
	   @param lat The latitude of the point distance to which is to be computed.
	   @param lon The longitude of the point distance to which is to be computed.
	   @return The distance between the bus stop on the specified point in meters.
	 */
	public double getDistance(String lat, String lon) {
		double lat1 = Double.valueOf(this.lat);
		double lat2 = Double.valueOf(lat);
		double lon1 = Double.valueOf(this.lon);
		double lon2 = Double.valueOf(lon);

		double r = 6371; // radius of Earth in kilometers
		
		double dLat = Math.toRadians(lat2-lat1);
		double dLon = Math.toRadians(lon2-lon1);
		double a = 
			Math.sin(dLat/2) * Math.sin(dLon/2) +
			Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
			Math.sin(dLon/2) * Math.sin(dLat/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double d = r * c;

		return d * 1000;
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
