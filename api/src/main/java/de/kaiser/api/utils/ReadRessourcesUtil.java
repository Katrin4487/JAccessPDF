package de.kaiser.api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ReadRessourcesUtil {


    /**
     * Privater Konstruktor, um die Instanziierung zu verhindern.
     */
    private ReadRessourcesUtil() {}

    /**
     * Versucht, eine Ressourcendatei als InputStream zu öffnen.
     *
     * Die Methode sucht in einer festen, robusten Reihenfolge:
     * 1. Zuerst wird der Classpath durchsucht. Dies ist der Standardfall für
     * Ressourcen, die mit der Anwendung gebündelt sind (z.B. in src/main/resources).
     * 2. Wenn im Classpath nichts gefunden wird, wird der Pfad als
     * Dateisystempfad interpretiert (sowohl absolut als auch relativ zum Arbeitsverzeichnis).
     *
     * @param filePath Der Pfad zur Ressourcendatei (z.B. "fonts/MyFont.ttf" oder "C:/Users/Test/font.ttf").
     * @return Einen {@link InputStream} zur Ressource, oder {@code null}, wenn die Ressource
     * an keinem der Orte gefunden werden konnte.
     */
    public static InputStream getResourceFileAsStream(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            System.err.println("WARNUNG: Der angegebene Dateipfad ist null oder leer.");
            return null;
        }

        // --- SCHRITT 1: Versuch, aus dem Classpath zu laden. ---
        // Dies ist der wichtigste Weg für portable Anwendungen.
        // ClassLoader erwarten Pfade ohne führenden Slash. Wir normalisieren den Pfad für den Fall,
        // dass er fälschlicherweise mit einem Slash beginnt.
        String classpathPath = filePath.startsWith("/") ? filePath.substring(1) : filePath;
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(classpathPath);

        if (stream != null) {
            System.out.println("INFO: Ressource '" + filePath + "' im Classpath gefunden.");
            return stream;
        }

        // --- SCHRITT 2: Wenn im Classpath nicht gefunden, versuche es im Dateisystem. ---
        // Dies ermöglicht das Laden von Dateien von beliebigen Orten auf dem Computer.
        File file = new File(filePath);
        if (file.exists() && file.canRead()) {
            try {
                System.out.println("INFO: Ressource '" + filePath + "' im Dateisystem gefunden.");
                return new FileInputStream(file);
            } catch (IOException e) {
                // Dies sollte selten passieren, da wir file.exists() und file.canRead() geprüft haben.
                System.err.println("FEHLER: Konnte Datei '" + filePath + "' nicht lesen, obwohl sie existiert: " + e.getMessage());
                return null;
            }
        }

        // --- SCHRITT 3: Ressource wurde nirgends gefunden. ---
        System.err.println("WARNUNG: Ressource '" + filePath + "' konnte weder im Classpath noch im Dateisystem gefunden werden.");
        return null;
    }


    public static URL getResourceFileUrl(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            System.err.println("WARNUNG: Der angegebene Dateipfad ist null oder leer.");
            return null;
        }

        // --- SCHRITT 1: Versuch, aus dem Classpath zu laden. ---
        // Dies ist der wichtigste Weg für portable Anwendungen.
        // ClassLoader erwarten Pfade ohne führenden Slash. Wir normalisieren den Pfad für den Fall,
        // dass er fälschlicherweise mit einem Slash beginnt.
        String classpathPath = filePath.startsWith("/") ? filePath.substring(1) : filePath;
        URL url = Thread.currentThread().getContextClassLoader().getResource(classpathPath);;

        if (url != null) {
            System.out.println("INFO: Ressource '" + filePath + "' im Classpath gefunden.");
            return url;
        }

        // --- SCHRITT 2: Wenn im Classpath nicht gefunden, versuche es im Dateisystem. ---
        // Dies ermöglicht das Laden von Dateien von beliebigen Orten auf dem Computer.
        File file = new File(filePath);
        if (file.exists() && file.canRead()) {
            try {
                System.out.println("INFO: Ressource '" + filePath + "' im Dateisystem gefunden.");
                return file.toURI().toURL();
            } catch (IOException e) {
                // Dies sollte selten passieren, da wir file.exists() und file.canRead() geprüft haben.
                System.err.println("FEHLER: Konnte Datei '" + filePath + "' nicht lesen, obwohl sie existiert: " + e.getMessage());
                return null;
            }
        }

        // --- SCHRITT 3: Ressource wurde nirgends gefunden. ---
        System.err.println("WARNUNG: Ressource '" + filePath + "' konnte weder im Classpath noch im Dateisystem gefunden werden.");
        return null;
    }
}
