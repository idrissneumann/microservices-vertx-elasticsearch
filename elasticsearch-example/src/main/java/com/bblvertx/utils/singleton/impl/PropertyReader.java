package com.bblvertx.utils.singleton.impl;

import static org.apache.commons.lang3.StringUtils.isNumeric;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.inject.Singleton;

/**
 * Properties file reader singleton.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
@Singleton
public class PropertyReader {
  Map<String, Properties> prop;

  /**
   * Constructor.
   * 
   */
  public PropertyReader() {
    prop = new HashMap<String, Properties>();
  }

  /**
   * Loading a file in memory.
   * 
   * @param fileName
   * @throws IOException
   */
  public void load(String fileName) throws IOException {
    if (!prop.containsKey(fileName)) {
      InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
      Properties p = new Properties();
      p.load(is);
      prop.put(fileName, p);
    }
  }

  /**
   * Getting the value.
   * 
   * @param fileName
   * @param key
   * @return String
   * @throws IOException
   */
  public String get(String fileName, String key) throws IOException {
    load(fileName);
    return prop.get(fileName).getProperty(key);
  }

  /**
   * Getting the value as integer.
   * 
   * @param fileName
   * @param key
   * @return Integer
   * @throws IOException
   */
  public Integer getInt(String fileName, String key) throws IOException {
    String val = this.get(fileName, key);
    return (!isNumeric(val)) ? null : Integer.valueOf(val);
  }

  /**
   * Getting the value as long.
   * 
   * @param fileName
   * @param key
   * @return Integer
   * @throws IOException
   */
  public Long getLong(String fileName, String key) throws IOException {
    String val = this.get(fileName, key);
    return (!isNumeric(val)) ? null : Long.valueOf(val);
  }

  /**
   * Getting the value quietly (return null if not exists).
   * 
   * @param fileName
   * @param key
   * @return String
   * @throws IOException
   */
  public String getQuietly(String fileName, String key) {
    try {
      return this.get(fileName, key);
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * Getting the value as Integer quietly (return null if not exists).
   * 
   * @param fileName
   * @param key
   * @return Integer
   * @throws IOException
   */
  public Integer getIntQuietly(String fileName, String key) {
    try {
      return this.getInt(fileName, key);
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * Getting the value as Long quietly (return null if not exists).
   * 
   * @param fileName
   * @param key
   * @return Integer
   * @throws IOException
   */
  public Long getLongQuietly(String fileName, String key) {
    try {
      return this.getLong(fileName, key);
    } catch (IOException e) {
      return null;
    }
  }
}
