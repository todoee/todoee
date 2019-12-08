package io.todoee.base.utils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Singleton pattern implementation using enums.
 * See "Effective Java" by Joshua Bloch
 *
 * @author James.zhang
 */
public enum DateUtils {

  INSTANCE;

  private transient DatatypeFactory factory;

  DateUtils() {
    try {
      factory = DatatypeFactory.newInstance();
    } catch (DatatypeConfigurationException dtce) {
      throw new RuntimeException(dtce);
    }
  }

  public Date parseIso8601(String dateStr) {
	  SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  Date date = null;
	  try {
		date = simpleDateFormat.parse(dateStr);
	} catch (ParseException e) {
		throw new RuntimeException("date parse error for: " + dateStr, e);
	}
	  GregorianCalendar gc = new GregorianCalendar();
	  gc.setTime(date);
    XMLGregorianCalendar cal = factory.newXMLGregorianCalendar(gc);
    return cal.toGregorianCalendar().getTime();
  }

  public String formatIso8601(Date date, TimeZone zone) {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(date);
    if (zone != null) {
      cal.setTimeZone(zone);
    }
    XMLGregorianCalendar calXml = factory.newXMLGregorianCalendar(cal);
    if (zone == null) {
      // display as UTC
      calXml = calXml.normalize();
    }
    return calXml.toXMLFormat();
  }

  public String formatIso8601(Date date) {
    return formatIso8601(date, null);
  }

}
