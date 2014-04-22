package org.eder.learning.ws.soap;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;

public class TempVerterClient {

	public static void main(String[] args) {
		callWsWithLogging();
		/*
		try {
			//raw method
			long start = System.currentTimeMillis();
			URL wsdlLocation = new URL("http://localhost:9901/TempVerter?wsdl");
			QName serviceName = new QName("http://soap.ws.learning.eder.org/","TempVerterImplService");
			Service service = Service.create(wsdlLocation, serviceName);
			QName portName = new QName("http://soap.ws.learning.eder.org/", "TempVerterImplPort");
			TempVerter tv = service.getPort(portName, TempVerter.class);
			System.out.println(tv.c2f(100));
			long end = System.currentTimeMillis();

			//via auto generated class
			long start2 = System.currentTimeMillis();
			callWsWithLogging();
			long end2 = System.currentTimeMillis();
			
			System.out.println("1: "+(end-start));
			System.out.println("2: "+(end2-start2));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}*/
	}

	private static void callWsWithLogging() {
		TempVerterImplService tvis = new TempVerterImplService();
		TempVerter tv = tvis.getTempVerterImplPort();
		BindingProvider bp = (BindingProvider) tv;
		Binding binding = bp.getBinding();
		List<Handler> hc = binding.getHandlerChain();
		hc.add(new SOAPLoggingHandler());
		binding.setHandlerChain(hc);
		System.out.println(tv.c2f(100));
	}

}
