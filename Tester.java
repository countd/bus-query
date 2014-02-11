import uk.ac.bradford.pisoc.StopLocator;
import uk.ac.bradford.pisoc.BusStop;

class Tester {
	public static void main(String[] argv) {
		System.out.println("Fetching the closest bus stop to the point 53.794333, -1.7675, where bus 615 stops.");
		BusStop stop = StopLocator.getNearest("53.794333", "-1.7675", "615");
		System.out.println(stop);
	}
}
