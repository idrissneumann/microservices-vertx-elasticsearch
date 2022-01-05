package com.bblvertx.utils;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNumeric;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bblvertx.SeConstants;
import com.bblvertx.exception.BadRequestException;
import com.bblvertx.exception.FunctionalException;
import com.bblvertx.pojo.SearchResult;

/**
 * Utils class.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public class CommonUtils {
  private static final Logger LOGGER = LogManager.getLogger(CommonUtils.class);

  /**
   * Static class => private constructor.
   */
  private CommonUtils() {}

  /**
   * Asserting the param is not empty.
   * 
   * @param value
   * @param msg
   */
  public static void assertParamNotEmpty(String value, String msg) {
    if (isEmpty(value)) {
      LOGGER.debug("[assertParamNotEmpty] message = " + msg);
      throw new BadRequestException(msg);
    }
  }

  /**
   * Asserting the param is a valid calendar.
   * 
   * @param value
   * @param msg
   * @param la valeur castée.
   */
  public static Calendar assertParamNotCalendar(String value, String msg) {
    Calendar rtn = null;

    try {
      if (StringUtils.isNotEmpty(value)
          && null == (rtn = DateUtils.stringToCalendar(value, DateUtils.FORMAT_Z))) {
        LOGGER.debug("[assertParamNotEmpty] message = " + msg);
        throw new BadRequestException(msg);
      }
    } catch (ParseException e) {
      LOGGER.debug("[assertParamNotEmpty] message = " + msg);
      throw new BadRequestException(msg);
    }

    return rtn;
  }

  /**
   * Asserting the param is numeric.
   * 
   * @param value
   * @param msg
   * @param la valeur castée.
   */
  public static Integer assertParamNumeric(String value, String msg) {
    if (isEmpty(value)) {
      return null;
    }

    if (!isNumeric(value)) {
      LOGGER.debug("[assertParamNotEmpty] message = " + msg);
      throw new BadRequestException(msg);
    }

    return Integer.valueOf(value);
  }

  /**
   * Vérifie que le paramètre d'une requête est obligatoirement vide (cf : id pour les POST)
   * 
   * @param value
   * @param msg
   */
  public static void assertParamEmpty(String value, String msg) {
    if (StringUtils.isNotEmpty(value)) {
      LOGGER.debug("[assertParamEmpty] message = " + msg);
      throw new BadRequestException(msg);
    }
  }

  /**
   * Asserting the param is not empty.
   * 
   * @param value
   * @param msg
   */
  public static void assertParamNotEmpty(Object value, String msg) {
    if (null == value) {
      LOGGER.debug("[assertParamNotEmpty] message = " + msg);
      throw new BadRequestException(msg);
    }
  }

  /**
   * Asserting the param is empty.
   * 
   * @param value
   * @param msg
   */
  public static void assertParamEmpty(Object value, String msg) {
    if (null != value) {
      LOGGER.debug("[assertParamEmpty] message = " + msg);
      throw new BadRequestException(msg);
    }
  }

  /**
   * Formatting args {0}, {1}, ... in a String.
   * 
   * @param str
   * @param args
   * @param locale
   * @return String
   */
  public static String format(String str, Object[] args, Locale locale) {
    if (isEmpty(str) || null == args || args.length <= 0) {
      return str;
    }

    MessageFormat mf = new MessageFormat(str, locale);
    return mf.format(args);
  }

  /**
   * Safe compareTo.
   * 
   * @param s1
   * @param s2
   * @param ignoreCase
   * @return int (1 si s1 > s2, -1 si s1 < s2 et 0 si s1 == s2)
   */
  public static int compareTo(String s1, String s2, boolean ignoreCase) {
    if (null == s1 && null == s2) {
      return 0;
    } else if (null == s1) {
      return -1;
    } else if (null == s2) {
      return 1;
    } else {
      return (ignoreCase) ? s1.compareToIgnoreCase(s2) : s1.compareTo(s2);
    }
  }

  /**
   * Safe compareTo.
   * 
   * @param s1
   * @param s2
   * @return int (1 si s1 > s2, -1 si s1 < s2 et 0 si s1 == s2)
   */
  public static int compareTo(String s1, String s2) {
    return compareTo(s1, s2, false);
  }

  /**
   * Split a list of id from a String with a separator.
   * 
   * @param strListIds
   * @param separator
   * @return List<Long>
   * @throws FunctionalException
   */
  public static List<Long> string2listIds(String strListIds, String separator)
      throws FunctionalException {
    if (isEmpty(strListIds)) {
      return new ArrayList<Long>();
    }

    String[] ids = strListIds.split(separator);

    List<Long> rtn = new ArrayList<Long>();

    try {
      if (null == ids || ids.length <= 0) {
        rtn.add(Long.valueOf(strListIds));
      } else {
        for (String id : ids) {
          rtn.add(Long.valueOf(id));
        }
      }

      return rtn;
    } catch (NumberFormatException e) {
      throw new FunctionalException("Les ids sont incorrectes : " + strListIds);
    }
  }

  /**
   * Split a list of id from a String with the separator ";".
   * 
   * @param strListIds
   * @return List<Long>
   * @throws FunctionalException
   */

  public static List<Long> string2listIds(String strListIds) throws FunctionalException {
    if (strListIds.contains(" ")) {
      return string2listIds(strListIds, " ");
    } else {
      return string2listIds(strListIds, SeConstants.SEPARATOR_DEFAULT);
    }
  }

  /**
   * String conversion.
   * 
   * @param Object value
   * @return String
   */
  public static String objectToString(Object value) {
    return (null != value) ? value.toString() : SeConstants.EMPTY_STRING;
  }

  /**
   * Parsing the hours (hh:mm ou hhmm) and getting a list of long.
   * 
   * @param strListNums
   * @return List<Long>
   */
  public static List<Long> parseHours(String strListNums) {

    List<Long> rtn = new ArrayList<Long>();
    String[] ids = strListNums.split(":");
    if (1 == ids.length) {

      rtn.add((Long.valueOf(ids[0].substring(0, 2))));
      rtn.add((Long.valueOf(ids[0].substring(2, 4))));

    } else {
      for (String id : ids) {
        rtn.add(Long.valueOf(id));
      }

    }

    return rtn;

  }

  /**
   * String pad with '0'.
   * 
   * @param pToPad
   * @param pPadLength Int length of padding
   * @return String
   */
  public static String pad(String pToPad, int pPadLength) {
    return pad(pToPad, pPadLength, '0');
  }

  /**
   * String unpad.
   * 
   * @param pToPad
   * @return String
   */
  public static String unpad(String pToPad) {
    return unpad(pToPad, '0');
  }

  /**
   * String pad.
   * 
   * @param pToPad
   * @param pPadLength Int Length of padding
   * @param pChar Char padding char
   * @return String
   */
  public static String pad(String pToPad, int pPadLength, char pChar) {
    if (pToPad != null) {
      int vStrLen = pToPad.length();
      final StringBuffer vBuffer = new StringBuffer(pToPad);

      if (vStrLen > 0) {
        while (vStrLen < pPadLength) {
          vBuffer.insert(0, pChar);
          vStrLen++;
        }
      }
      return vBuffer.toString();
    } else {
      return null;
    }
  }

  /**
   * Unpad.
   * 
   * @param pToPad
   * @param pChar padding char
   * @return String
   */
  public static String unpad(String pToPad, char pChar) {
    if (pToPad != null) {
      final int vStrLen = pToPad.length();
      final StringBuffer vBuffer = new StringBuffer("");
      final int vIndex = 0;
      boolean vEndUnPading = false;

      while ((vIndex < vStrLen) && !vEndUnPading) {
        if (pToPad.charAt(vIndex) != pChar) {
          vEndUnPading = true;
          vBuffer.append(pToPad.substring(vIndex, vStrLen - 1));
        }
      }
      return vBuffer.toString();
    } else {
      return null;
    }
  }

  /**
   * Init of SearchResult.
   * 
   * @param startIndex
   * @param maxResults
   * @return SearchResult<T>
   */
  public static <T> SearchResult<T> initSearchResult(Long startIndex, Long maxResults) {
    SearchResult<T> rtn = new SearchResult<T>();
    rtn.setTotalResults(0L);
    rtn.setStartIndex(startIndex);
    rtn.setMaxResults(maxResults);
    return rtn;
  }

  /**
   * Return true if the search result is empty.
   * 
   * @param result
   * @return Boolean
   */
  public static Boolean isNotEmpty(SearchResult<?> result) {
    return null != result && CollectionUtils.isNotEmpty(result.getResults());
  }

  /**
   * Return the name of a getter.
   * 
   * @param propertyName
   * @return String
   */
  public static String getterFromProperty(String propertyName) {
    return (null == propertyName) ? null
        : "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
  }

  /**
   * Return the name of a setter.
   * 
   * @param propertyName
   * @return String
   */
  public static String setterFromProperty(String propertyName) {
    return (null == propertyName) ? null
        : "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
  }

  /**
   * Safe compare between two Integer.
   * 
   * @param o1
   * @param o2
   * @return Integer
   */
  public static Integer safeCompare(Integer o1, Integer o2) {
    if (null == o1 && null == o2) {
      return 0;
    } else if (null == o1) {
      return -1;
    } else if (null == o2) {
      return 1;
    } else {
      return o1.compareTo(o2);
    }
  }
}
