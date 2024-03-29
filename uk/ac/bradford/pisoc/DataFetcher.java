package uk.ac.bradford.pisoc;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.MalformedURLException;

class DataFetcher {
	static final String BASE_URL = "http://api.stride-project.com/transportapi/7c60e7f4-20ff-11e3-857c-fcfb53959281/bus";
	static final String NEAR_URL = "/stops/near";
	static final String API_KEY = "d74fce25-7c10-4e0d-b137-0c752f454d5f";
	
	private static String readStream(InputStream s) 
		throws UnableToContactServerException {
		InputStreamReader reader = new InputStreamReader(s);
		String result = "";
		try {
			for(int r = reader.read(); r != -1; r = reader.read()) {
				char c = (char) r;
				result += c;
			}
		} catch (IOException e) {
			throw new UnableToContactServerException("Failed to read data from server.");
		}

		return result;
	}

	// fetch the bus station info
	public static String fetchStopInfo(String atcocode) 
		throws UnableToContactServerException,
			   ProtocolException, 
			   MalformedURLException {
		String urlString = String.format(BASE_URL + "/stop/%s/live", atcocode);
		return fetchJSON(urlString);
	}
	
	// fetch the nearest bus station
	public static String fetchNearestStops(String lat, String lon) 
		throws UnableToContactServerException,
			   ProtocolException,
			   MalformedURLException
	{
		String urlString = String.format(BASE_URL + NEAR_URL + "?lat=%s&lon=%s&page=%d&rpp=%d", lat, lon, 1, 25);
		return fetchJSON(urlString);
	}

	private static String fetchJSON(String urlString) 
		throws MalformedURLException,
			   ProtocolException, 
			   UnableToContactServerException {
		URL url = null;
		HttpURLConnection con = null;
		String json = "";

		// set up the URL
		url = new URL(urlString); // Throws MalformedURLException

		// set up a connection to the server
		try {
			con = (HttpURLConnection) url.openConnection();			
			con.setRequestProperty("Accept", "application/json");
			con.setRequestMethod("GET"); // throws ProtocolException
			con.setRequestProperty("x-api-key", API_KEY);
		} catch (IOException e) {
			throw new UnableToContactServerException("Failed to communicate with the Stride API Server.");
		}

		// process data
		try {
			InputStream is = new BufferedInputStream(con.getInputStream());
			json = readStream(is);
		} catch (IOException e) {
			throw new UnableToContactServerException("Failed to bind a stream to the HTTP connection.");
		} finally {
			con.disconnect();
		}

		return json;
	}
	
}
