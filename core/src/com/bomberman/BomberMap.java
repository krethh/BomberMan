package com.bomberman;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Klasa zawierająca parametry mapy.
 * @author Paweł Kulig, Wojciech Sobczak
 */
public class BomberMap {

    /**
     * Ilość bloków ekranu w poziomie.
     */
    public short screenWidth;

    /**
     * Ilość bloków ekranu w pionie.
     */
    public short screenHeight;

    /**
     * Koduje pozycje przeszkód na ekranie.
     * Każda litera koduje jeden blok, p-przejście, w-sciana (wall), o-przeszkoda (obstacle).
     * Bloki numerowane są od lewej do prawej, od dołu do góry.
     */
    public String mapObjects;

    /**
     * Koduje pozycje przeciwniki na ekranie.
     * Zawiera liczby oddzielone spacją; każda liczba to blok ekranu, który zawiera przejście.
     * Bloki numerowane są od lewej do prawej, od dołu do góry.
     */
    public String enemyPositions;

    /**
     * Koduje początkową pozycję gracza.
     * Zawiera liczbę; liczba to blok ekranu, w którym znajduje się gracz.
     * Bloki numerowane są od lewej do prawej, od dołu do góry.
     */
    public short heroPosition;

    /**
     * Określa pozycje superbomb.
     * Zawiera liczby; każda liczba to blok ekranu, w którym znajduje się superbomba.
     * Bloki numerowane są od lewej do prawej, od dołu do góry.
     */
    public String superbombPositions;

    /**
     * Określa pozycję eliminacji.
     * Zawiera liczbę; liczba to blok ekranu, w którym znajduje się eliminacja.
     * Bloki numerowane są od lewej do prawej, od dołu do góry.
     */
    public short elimnationPosition;

    /**
     * Określa pozycje multibomb.
     * Zawiera liczby; każda liczba to blok ekranu, w którym znajduje się superbomba.
     * Bloki numerowane są od lewej do prawej, od dołu do góry.
     */
    public String multibombPositions;

    /**
     * Określa pozycję wisienki.
     * Zawiera liczbę; liczba to blok ekranu, w którym znajduje się wisienka.
     * Bloki numerowane są od lewej do prawej, od dołu do góry.
     */
    public short cherryPosition;

    /**
     * Tworzy nową mapę za pomocą pliku konfiguracyjnego.
     * @param path Ścieżka do pliku konfiguracyjnego mapy.
     */
    public BomberMap(String path) throws IOException
    {
        Properties properties = new Properties();

        InputStream input = new FileInputStream(path);
        properties.load(input);

        heroPosition = Short.valueOf(properties.getProperty("heroPosition"));
        enemyPositions = properties.getProperty("enemyPositions");
        mapObjects = properties.getProperty("mapObjects");
        screenHeight = Short.valueOf(properties.getProperty("screenHeight"));
        screenWidth = Short.valueOf(properties.getProperty("screenWidth"));
        superbombPositions = properties.getProperty("superbombPositions");
        elimnationPosition = Short.valueOf(properties.getProperty("eliminationPosition"));
        multibombPositions = properties.getProperty("multibombPositions");
        cherryPosition = Short.valueOf(properties.getProperty("cherryPosition"));
    }

}
