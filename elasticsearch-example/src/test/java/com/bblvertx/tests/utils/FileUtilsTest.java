package com.bblvertx.tests.utils;

import static com.bblvertx.utils.FileUtils.deleteFileQuietly;
import static com.bblvertx.utils.FileUtils.existFile;
import static com.bblvertx.utils.FileUtils.file2stringQuietly;
import static com.bblvertx.utils.FileUtils.fileToByteQuietly;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.bblvertx.tests.AbstractTest;

/**
 * Tests sur la classe FileUtils.
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 */

public class FileUtilsTest extends AbstractTest {
  private String pathFile;

  /**
   * Initialiser un fichier de test
   */
  @Before
  public final void initFile() {
    pathFile = getTestDataDir() + "test.txt";
    try {
      writeStringToFile(new File(pathFile), "test");
    } catch (IOException e) {
      failWithException(e);
    }
  }

  /**
   * TU : tester l'existance d'un fichier
   */
  @Test
  public final void testExistFile() {
    assertTrue(existFile(pathFile));
  }

  /**
   * TU : suppression d'un fichier
   */
  @Test
  public final void testDeleteFileQuietly() {
    assertTrue(deleteFileQuietly(pathFile));

    // On vérifie aussi que le fichier n'existe plus
    assertFalse(existFile(pathFile));
  }

  /**
   * TU : récupérer le contenu en byte
   */
  @Test
  public final void testFileToByteQuietly() {
    assertNotNull(fileToByteQuietly(pathFile));
  }

  /**
   * TU : récupérer le contenu en string
   */
  @Test
  public final void testFile2stringQuietly() {
    assertEquals("test", file2stringQuietly(pathFile));
  }

  /**
   * TU : récupérer le contenu en string avec un fichier inexistant
   */
  @Test
  public final void testFile2stringQuietlyFileNotExist() {
    assertNull(file2stringQuietly(getTestDataDir() + "inexistant.txt"));
  }
}
