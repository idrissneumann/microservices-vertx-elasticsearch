package com.bblvertx.tests;

import static org.junit.Assert.fail;

import java.io.File;

/**
 * Classe abstraite pour tout les types de tests JUnit
 * 
 * @author Idriss Neumann <neumann.idriss@gmail.com>
 *
 */
public abstract class AbstractTest {
    /**
     * Faire echouer un test en cas d'exception (avec affichage de la stack
     * trace)
     * 
     * @param Exception
     *            e
     */
    public void failWithException(Exception e) {
        e.printStackTrace();
        fail("Exception inattendue : " + e.getMessage());
    }

    /**
     * Récupère le chemin du répertoire data relatif au TI en cours ATTENTION :
     * avec un un / à la fin
     * 
     * @return String
     */
    public String getTestDataDir() {
        return "src" //
                + File.separator //
                + "test" //
                + File.separator //
                + "resources" //
                + File.separator //
                + "data" //
                + File.separator //
                + this.getClass().getSimpleName() //
                + File.separator;
    }

    /**
     * Récupère le chemin du répertoire data relatif au TI en cours ATTENTION :
     * avec un un / à la fin
     * 
     * @return String
     */
    public String getTestCommonDataDir() {
        return "src" //
                + File.separator //
                + "test" //
                + File.separator //
                + "resources" //
                + File.separator //
                + "data" //
                + File.separator //
                + "Common" //
                + File.separator;
    }

    /**
     * Récupère le chemin du répertoire resources ATTENTION : avec un un / à la
     * fin
     * 
     * @return String
     */
    public String getTestResourcesDir() {
        return "src" //
                + File.separator //
                + "test" //
                + File.separator //
                + "resources" //
                + File.separator;
    }

    /**
     * Récupère le chemin du répertoire data relatif au traitement en cours
     * ATTENTION : avec un un / à la fin
     * 
     * @return String
     */
    public String getDataDir() {
        return "src" //
                + File.separator //
                + "main" //
                + File.separator //
                + "resources" //
                + File.separator;
    }
}
