package uk.ac.bradford.pisoc;

public class MalformedServerResponseException extends Exception {
	public MalformedServerResponseException() {
		super();
	}

	public MalformedServerResponseException(String s) {
		super(s);
	}
}
