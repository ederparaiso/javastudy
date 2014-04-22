package org.eder.learning.ws.soap;

import javax.jws.WebService;

@WebService(endpointInterface="org.eder.learning.ws.soap.TempVerter")
public class TempVerterImpl implements TempVerter {

	@Override
	public double c2f(double degrees) {
		return degrees*9.0/5.0+32;
	}

	@Override
	public double f2c(double degrees) {
		return (degrees-32)*5.0/9.0;
	}

}
