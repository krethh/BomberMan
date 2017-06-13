package com.bomberman;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

/**
 * Klasa przechowująca konfigurację gry.
 * @author Paweł Kulig, Wojciech Sobczak
 */
public class BomberConfig {

    /// Lista parametrów aplikacji

    /**
     * Mówi, jak długa w każdym kierunku jest fala uderzeniowa.
     */
    public short shockwaveLength;

    /**
     * Mówi, jak długo trwa czekanie na wybuch bomby.
     */
    public short bombWaitingTime;

    /**
     * Mówi o tym jak długo trwa fala uderzeniowa.
     */
    public short shockwaveTime;

    /**
     * Szerokość panelu gry po lewej stronie (w pikselach).
     */
    public short panelWidth;

    /**
     * Domyślna wysokość okienka gry w pikselach.
     */
    public short pixelHeight;

    /**
     * Domyślna szerokość okienka gry w pikselach.
     */
    public short pixelWidth;

    /**
     * Domyślna ilośc klatek na sekundę.
     */
    public short FPS;

    /**
     * Lista map zdefiniowanych w folderze /maps.
     */
    public ArrayList<String> mapNames;

    /**
     * Lista map pobranych z serwera.
     */
    public ArrayList<BomberMap> serverMaps;

    /**
     * Szybkość postaci gracza.
     */
    public short speed;

    /**
     * Czas trwania multibomby.
     */
    public short multibombDuration;

    /**
     * Czas nieśmiertelności po dostaniu bombą.
     */
    public short shockwaveRecoveryTime;

    /**
     * Adres IP serwera obsługującego.
     */
    public String serverIpAddress;

    /**
     * Port serwera obsługującego.
     */
    public int serverPort;

    /**
     * Tworzy obiekt konfiguracyjny.
     * @throws IOException W przypadku niemożności otwarcia pliku
     */
    public BomberConfig() throws IOException {

        Properties properties = new Properties();

        InputStream input = new FileInputStream("main.config");
        properties.load(input);

        shockwaveTime = Short.valueOf(properties.getProperty("shockwaveTime"));
        bombWaitingTime = Short.valueOf(properties.getProperty("bombWaitingTime"));
        shockwaveLength = Short.valueOf(properties.getProperty("shockwaveLength"));
        panelWidth = Short.valueOf(properties.getProperty("panelWidth"));
        pixelWidth = Short.valueOf(properties.getProperty("pixelWidth"));
        pixelHeight = Short.valueOf(properties.getProperty("pixelHeight"));
        FPS = Short.valueOf(properties.getProperty("fps"));
        speed = Short.valueOf(properties.getProperty("speed"));
        multibombDuration = Short.valueOf(properties.getProperty("multibombDuration"));
        shockwaveRecoveryTime = Short.valueOf(properties.getProperty("shockwaveRecoveryTime"));
        serverPort = Integer.valueOf(properties.getProperty("serverPort"));
        serverIpAddress = properties.getProperty("serverIpAddress");

        /// przeszukaj folder maps w celu załadowania wszystkich map do obiektu konfiguracyjnego
        ArrayList<String> mapPaths = DirectoryUtils.findFilesWithExtension("maps", ".mapconfig");
        mapNames = new ArrayList<String>();

        for(String mapPath : mapPaths)
        {
            mapNames.add(mapPath);
        }

        serverMaps = new ArrayList<>();
    }
}
