package org.eder.learning.ws.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface TempVerter {
	@WebMethod
	double c2f(double degrees);

	@WebMethod
	double f2c(double degrees);
}
