package com.bblvertx.utils;

import static org.apache.commons.io.FileUtils.writeStringToFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * File utils class.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public class FileUtils {
  private static final Logger LOGGER = LogManager.getLogger(FileUtils.class);

  /**
   * Checking if a file exists.
   * 
   * @param path
   * @return true if the file exists, otherwise false
   */
  public static boolean existFile(String path) {
    File f = new File(path);
    return f.exists();
  }

  /**
   * Deleting file quietly.
   * 
   * @param path
   * @return true if the file have been deleted, otherwise false
   */
  public static boolean deleteFileQuietly(String path) {
    if (existFile(path)) {
      File f = new File(path);
      boolean rtn = f.delete();

      if (existFile(path) || !rtn) {
        try {
          Path p = FileSystems.getDefault().getPath(path, new String[0]);
          Files.delete(p);
          return true;
        } catch (IOException e) {
          LOGGER.error("Erreur à la lecture du fichier ", e);
          return false;
        }
      }

      return rtn;
    }

    return false;
  }

  /**
   * Getting the content of a file into a byte array.
   * 
   * @param path
   * @return the content
   */
  public static byte[] fileToByteQuietly(String path) {
    Path p = Paths.get(path);
    try {
      return Files.readAllBytes(p);
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * Getting absolute path.
   * 
   * @param relatifPath
   * @return the path
   */
  public static String getAbsolutePath(String relatifPath) {
    File file = new File(relatifPath);
    return file.getAbsolutePath();
  }

  /**
   * Getting the file content into a String.
   * 
   * @param pathFile
   * @return the content
   */
  public static String file2stringQuietly(String pathFile) {
    if (!existFile(pathFile)) {
      LOGGER.warn("[file2string] Le fichier " + pathFile + " n'existe pas !");
      return null;
    }

    try {
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(new FileInputStream(pathFile), "UTF8"));
      StringBuilder builder = new StringBuilder();
      String line;

      // For every line in the file, append it to the string builder
      while (null != (line = reader.readLine())) {
        builder.append(line);
      }

      reader.close();
      return builder.toString();
    } catch (IOException e) {
      LOGGER.error("Erreur à la lecture du fichier ", e);
      return null;
    }
  }

  /**
   * Writing into a file.
   * 
   * @param pathFile
   * @param content
   * @throws IOException
   */
  public static void string2fileQuietly(String pathFile, String content) {
    try {
      writeStringToFile(new File(pathFile), content);
    } catch (IOException e) {
      LOGGER.error("Erreur à la lecture du fichier ", e);
    }
  }

  /**
   * Private constructor => utils class.
   */
  private FileUtils() {}
}
