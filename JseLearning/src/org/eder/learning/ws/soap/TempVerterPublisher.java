package org.eder.learning.ws.soap;

import javax.xml.ws.Endpoint;

public class TempVerterPublisher {
	public static void main(String[] args){
		
		Endpoint.publish("http://localhost:9901/TempVerter", new TempVerterImpl());
	}
}
