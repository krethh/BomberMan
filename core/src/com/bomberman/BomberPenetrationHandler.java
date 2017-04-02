package com.bomberman;

import com.bomberman.screens.MainGameScreen;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Klasa służąca do obsługiwania wchodzenia w ściany.
 */
public class BomberPenetrationHandler {

    /**
     * Ekran, na którym występują kolizje.
     */
    private MainGameScreen screen;

    /**
     * Główny konstruktor.
     * @param screen Ekran, na którym detekujemy wchodzenie w ściany.
     */
    public BomberPenetrationHandler(MainGameScreen screen)
    {
        this.screen = screen;
    }

    /**
     * Sprawdza, czy występuje kolizja ze ścianą lub przeszkodą.
     * @param x Współrzędna x prostokąta enkapsulującego postać.
     * @param y Współrzędna y prostokąta enkapsulującego postać.
     * @return False jeżeli postac nie może iść dalej.
     */
    public BomberTile cannotPenetrate(float x, float y){
        ArrayList<BomberTile> walls = screen.mapGrid.stream().filter(t -> t.type == 'w' || t.type == 'o').collect(Collectors.toCollection(ArrayList::new));

        for(BomberTile t : walls)
        {
            if(haveCollision(x, y, t))
                return t;
        }
        return null;
    }

    /**
     * Sprawdza, czy występuje kolizja prostokątów.
     * @param x Współrzędna x lewego dolnego rogu.
     * @param y Współrzedna y lewego dolnego rogu
     * @param t Kafelek, z którym może wystąpić kolizja.
     * @return true jeżeli występuje kolizja.
     */
    public boolean haveCollision(float x, float y, BomberTile t)
    {
        return (x < t.getX() + screen.TILE_WIDTH && y < t.getY() +
                screen.TILE_HEIGHT && x +
                screen.HERO_SCALING_FACTOR*screen.TILE_WIDTH > t.getX() && y +
                screen.HERO_SCALING_FACTOR*screen.TILE_HEIGHT > t.getY());
    }
}
