package com.bomberman;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Klasa przechowująca konfigurację gry.
 * @author Paweł Kulig, Wojciech Sobczak
 */
public class BomberConfig {

    /// Lista parametrów aplikacji

    /**
     * Mówi, jak długa w każdym kierunku jest fala uderzeniowa.
     */
    public final short shockwaveLength;

    /**
     * Mówi, jak długo trwa czekanie na wybuch bomby.
     */
    public final short bombWaitingTime;

    /**
     * Mówi o tym jak długo trwa fala uderzeniowa.
     */
    public final short shockwaveTime;

    /**
     * Szerokość panelu gry po lewej stronie (w pikselach).
     */
    public final short panelWidth;

    /**
     * Domyślna wysokość okienka gry w pikselach.
     */
    public final short pixelHeight;

    /**
     * Domyślna szerokość okienka gry w pikselach.
     */
    public final short pixelWidth;

    /**
     * Domyślna ilośc klatek na sekundę.
     */
    public final short FPS;

    /**
     * Lista map zdefiniowanych w folderze /maps.
     */
    public final ArrayList<BomberMap> maps;

    /**
     * Szybkość postaci gracza.
     */
    public final short speed;

    /**
     * Czas trwania multibomby.
     */
    public final short multibombDuration;


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

        /// przeszukaj folder maps w celu załadowania wszystkich map do obiektu konfiguracyjnego
        ArrayList<String> mapPaths = DirectoryUtils.findFilesWithExtension("maps", ".mapconfig");
        maps = new ArrayList<BomberMap>();

        for(String mapPath : mapPaths)
        {
            maps.add(new BomberMap(mapPath));
        }

    }
}
