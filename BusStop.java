public class BusStop {
	public BusStop(String lat, String lon, String atco, long distance) {
		this.lat = lat;
		this.lon = lon;
		this.atco = atco;
		this.distance = distance;
		buses = JSONParser.parseStop(DataFetcher.fetchStopInfo(atco));
	}

	public String getLat() {
		return lat;
	}

	public String getLon() {
		return lon;
	}

	public String getAtco() {
		return atco;
	}

	public long getDistance() {
		return distance;
	}

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
