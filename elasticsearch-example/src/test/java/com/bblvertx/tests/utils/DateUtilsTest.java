package com.bblvertx.tests.utils;

import static com.bblvertx.utils.DateUtils.calendarToString;
import static com.bblvertx.utils.DateUtils.getCurrentTime;
import static com.bblvertx.utils.DateUtils.stringToCalendar;
import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

import com.bblvertx.tests.AbstractTest;

/**
 * Tests sur les fonction de la classe utilitaire DateUtils
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public class DateUtilsTest extends AbstractTest {
	/**
	 * Date courrante
	 */
	@Test
	public final void testGetCurrentTime() {
		assertEquals(calendarToString(Calendar.getInstance()), calendarToString(getCurrentTime()));
	}

	/**
	 * Test conversion date en string
	 */
	@Test
	public final void testCalendarToString() {
		Calendar d = new GregorianCalendar();
		d.set(Calendar.YEAR, 2014);
		d.set(Calendar.MONTH, Calendar.SEPTEMBER);
		d.set(Calendar.DAY_OF_MONTH, 5);

		assertEquals("05/09/2014", calendarToString(d));
	}

	/**
	 * Test conversion d'un string en calendar
	 */
	@Test
	public final void testStringToCalendar() {
		try {
			Calendar d = stringToCalendar("05/09/2014");
			assertEquals(2014, d.get(Calendar.YEAR));
			assertEquals(Calendar.SEPTEMBER, d.get(Calendar.MONTH));
			assertEquals(5, Calendar.DAY_OF_MONTH);
		} catch (ParseException e) {
			failWithException(e);
		}
	}

	/**
	 * Test StringToCalendar
	 */
	@Test
	public final void testStringToCalendar2() {
		try {

			Calendar d1 = stringToCalendar("05/09/2014", "dd/MM/yyyy");
			assertEquals(5, d1.get(Calendar.DAY_OF_MONTH));
			assertEquals(8, d1.get(Calendar.MONTH));
			assertEquals(2014, d1.get(Calendar.YEAR));
			assertEquals(0, d1.get(Calendar.HOUR));
			assertEquals(0, d1.get(Calendar.MINUTE));
			assertEquals(0, d1.get(Calendar.SECOND));
		} catch (ParseException e) {
			failWithException(e);
		}
	}
}
