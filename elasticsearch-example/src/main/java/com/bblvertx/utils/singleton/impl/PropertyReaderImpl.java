package com.bblvertx.utils.singleton.impl;

import com.bblvertx.utils.singleton.IPropertyReader;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isNumeric;

/**
 * Singleton pour lire un fichier properties.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
@Singleton
public class PropertyReaderImpl implements IPropertyReader {
  Map<String, Properties> prop;

  private PropertyReaderImpl() {
    prop = new HashMap<>();
  }

  public static PropertyReaderImpl newInstance() {
    return new PropertyReaderImpl();
  }

  private void setValueIfNotEmpty(Object key, Properties properties, String value) {
    if (isNotBlank(value)) {
      properties.put(key, value);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void load(String fileName) throws IOException {
    if (!prop.containsKey(fileName)) {
      InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
      Properties p = new Properties();
      p.load(is);

      // Check if an environment variable exist for each keys of the property file
      // if this env var exists, replace the value of the property with its value
      p.keySet().stream().forEach(k -> {
        String keyStr = k.toString();
        String envKey = keyStr.toUpperCase().replace(".", "_");

        setValueIfNotEmpty(k, p, System.getenv(envKey));
        setValueIfNotEmpty(k, p, System.getenv(keyStr));
        setValueIfNotEmpty(k, p, System.getProperty(envKey));
        setValueIfNotEmpty(k, p, System.getProperty(keyStr));
      });

      prop.put(fileName, p);
    }
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public String get(String fileName, String key) throws IOException {
    load(fileName);
    return prop.get(fileName).getProperty(key);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getInt(String fileName, String key) throws IOException {
    String val = this.get(fileName, key);
    return (!isNumeric(val)) ? null : Integer.valueOf(val);
  }

  @Override
  public Long getLong(String fileName, String key) throws IOException {
    return getInt(fileName, key).longValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getQuietly(String fileName, String key) {
    try {
      return this.get(fileName, key);
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Long> getListOfLongQuietly(String fileName, String key) {
    List<Long> rtn = new ArrayList<>();

    String val = null;
    try {
      val = this.get(fileName, key);
    } catch (IOException e) {
      return rtn;
    }

    if (StringUtils.isBlank(val)) {
      return rtn;
    }

    if (StringUtils.isNumeric(val)) {
      rtn.add(Long.valueOf(val));
      return rtn;
    }

    String[] vals = val.split(",");
    if (vals.length > 0) {
      Arrays.asList(vals).stream().filter(v -> StringUtils.isNumeric(v))
          .forEach(v -> rtn.add(Long.valueOf(v)));
    }

    return rtn;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getIntQuietly(String fileName, String key) {
    try {
      return this.getInt(fileName, key);
    } catch (IOException e) {
      return null;
    }
  }
}
