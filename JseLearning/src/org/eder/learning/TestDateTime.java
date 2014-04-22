package org.eder.learning;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.derby.iapi.services.timer.TimerFactory;

public class TestDateTime {

	//Useful not default api: Joda-Time
	//http://www.joda.org/joda-time/
	public static void main(String[] args) {
		//deprecated
		Date d = new Date(System.currentTimeMillis());
		System.out.println("Date.toString(): "+d);
		
		//Calendar
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2013);
		System.out.println("Calendar.get Methods: "+c.get(Calendar.DAY_OF_MONTH)+"-"+c.get(Calendar.MONTH)+1+"-"+c.get(Calendar.YEAR)+"");
		System.out.println("Calendar.getTime(): "+c.getTime());
		
		//DateFormat
		DateFormat dtf = DateFormat.getDateInstance(DateFormat.MEDIUM);
		System.out.println("DateFormat.getDateInstance(MEDIUM): "+dtf.format(c.getTime()));
		dtf = DateFormat.getTimeInstance();
		System.out.println("DateFormat.getTimeInstance(): "+dtf.format(c.getTime()));
		dtf = DateFormat.getDateTimeInstance();
		System.out.println("DateFormat.getDateTimeInstance(): "+dtf.format(c.getTime()));
		
		//SimpleDateFormat
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
		
		try {
			System.out.println("SimpleDateFormat.parse(): "+sdf.parse("27/01/14 10:11:0000"));
			System.out.println("SimpleDateFormat.format(): "+sdf.format(sdf.parse("27/01/14 10:11:0000")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//Locale - Timezone
		Calendar current = Calendar.getInstance();
		
		Locale uk = Locale.UK; 
		TimeZone utc = TimeZone.getTimeZone("UTC");
		
		Locale br = Locale.getDefault();//or new Locale("pt", "BR"); 
		TimeZone local = TimeZone.getDefault();
		
		DateFormat dfutcuk = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, uk);
		dfutcuk.setTimeZone(utc);

		DateFormat dflocalbr = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, br);
		dflocalbr.setTimeZone(local);
		
		System.out.println("DateTime br local tz: "+dfutcuk.format(current.getTime()));
		System.out.println("DateTime uk utc tz: "+dflocalbr.format(current.getTime()));
	}

}
