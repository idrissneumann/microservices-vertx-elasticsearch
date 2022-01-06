package com.bblvertx.utils.singleton;

import java.io.IOException;
import java.util.List;

public interface IPropertyReader {
    /**
     * Charger un fichier en mémoire.
     *
     * @param fileName
     * @throws IOException
     */
    void load(String fileName) throws IOException;

    /**
     * Récupérer la valeur d'une propriété.
     *
     * @param fileName
     * @param key
     * @return String
     * @throws IOException
     */
    String get(String fileName, String key) throws IOException;

    /**
     * Récupérer la valeur numérique d'une propriété.
     *
     * @param fileName
     * @param key
     * @return Integer
     * @throws IOException
     */
    Integer getInt(String fileName, String key) throws IOException;

    Long getLong(String fileName, String key) throws IOException;

    /**
     * Récupérer la valeur d'une propriété.
     *
     * @param fileName
     * @param key
     * @return String
     * @throws IOException
     */
    String getQuietly(String fileName, String key);

    /**
     * Récupérer une liste d'ids.
     *
     * @param fileName
     * @param key
     * @return
     */
    List<Long> getListOfLongQuietly(String fileName, String key);

    /**
     * Récupérer la valeur numérique d'une propriété.
     *
     * @param fileName
     * @param key
     * @return Integer
     * @throws IOException
     */
    Integer getIntQuietly(String fileName, String key);
}
