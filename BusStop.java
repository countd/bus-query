public class BusStop {
	public BusStop(String lat, String lon, String atco, long distance) {
		this.lat = lat;
		this.lon = lon;
		this.atco = atco;
		this.distance = distance;
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

	@Override
	public String toString() {
		return String.format("A bus stop with atcocode %s, located at %s ; %s, approximately %d meters from your destination.", atco, lat, lon, distance);
	}

	private String lat;
	private String lon;
	private String atco;
	private long distance;

}
