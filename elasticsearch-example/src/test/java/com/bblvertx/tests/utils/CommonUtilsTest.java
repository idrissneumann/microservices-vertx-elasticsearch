package com.bblvertx.tests.utils;

import static com.bblvertx.utils.CommonUtils.assertParamEmpty;
import static com.bblvertx.utils.CommonUtils.assertParamNotEmpty;
import static com.bblvertx.utils.CommonUtils.compareTo;
import static com.bblvertx.utils.CommonUtils.objectToString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.bblvertx.exception.BadRequestException;
import com.bblvertx.tests.AbstractTest;

/**
 * Tests sur la classe CommonUtils
 * 
 * @author Idriss Neumann <idriss.neumann@capgemini.com>
 *
 */
public class CommonUtilsTest extends AbstractTest {

	/**
	 * Test nominal : assertParamNotEmpty
	 */
	@Test
	public final void testAssertParamNotEmptyNominal() {
		String param = "test";
		Object param2 = new Object();
		try {
			assertParamNotEmpty(param, "message d'erreur");
			assertParamNotEmpty(param2, "message d'erreur");
			assertTrue(true);
		} catch (BadRequestException e) {
			failWithException(e);
		}
	}

	/**
	 * Test exception : assertParamNotEmpty
	 */
	@Test(expected = BadRequestException.class)
	public final void testAssertParamNotEmptyStringException() {
		String param = "";
		assertParamNotEmpty(param, "message d'erreur");
	}

	/**
	 * Test exception : assertParamNotEmpty
	 */
	@Test(expected = BadRequestException.class)
	public final void testAssertParamNotEmptyObjectException() {
		Object param = null;
		assertParamNotEmpty(param, "message d'erreur");
	}

	/**
	 * Test nominal : assertParamEmpty
	 */
	@Test
	public final void testAssertParamEmptyNominal() {
		String param = "";
		Object param2 = null;
		try {
			assertParamEmpty(param, "message d'erreur");
			assertParamEmpty(param2, "message d'erreur");
			assertTrue(true);
		} catch (BadRequestException e) {
			failWithException(e);
		}
	}

	/**
	 * Test exception : assertParamEmpty
	 */
	@Test(expected = BadRequestException.class)
	public final void testAssertParamEmptyStringException() {
		String param = "test";
		assertParamEmpty(param, "message d'erreur");
	}

	/**
	 * Test exception : assertParamEmpty
	 */
	@Test(expected = BadRequestException.class)
	public final void testAssertParamEmptyObjectException() {
		Object param = new Object();
		assertParamEmpty(param, "message d'erreur");
	}

	/**
	 * Test nominal : objectToString
	 */
	@Test
	public final void testObjectToString() {
		Long obj1 = 1L;
		Long obj2 = null;
		assertEquals("1", objectToString(obj1));
		assertEquals("", objectToString(obj2));
	}

	@Test
	public final void testCompareTo() {

		String s1 = "string1";
		String s2 = "string2";

		assertEquals(-1, compareTo(s1, s2, true));

	}
}
