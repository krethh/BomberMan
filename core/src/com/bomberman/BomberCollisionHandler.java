package com.bomberman;

import com.bomberman.screens.MainGameScreen;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Klasa służąca do obsługiwania wszelakich kolizji.
 */
public class BomberCollisionHandler {

    /**
     * Ekran, na którym występują kolizje.
     */
    private MainGameScreen screen;

    /**
     * Główny konstruktor.
     * @param screen Ekran, na którym detekujemy kolizje.
     */
    public BomberCollisionHandler(MainGameScreen screen)
    {
        this.screen = screen;
    }

    /**
     * Sprawdza, czy występuje kolizja ze ścianą lub przeszkodą.
     * @param x Współrzędna x prostokąta enkapsulującego postać.
     * @param y Współrzędna y prostokąta enkapsulującego postać.
     * @return False jeżeli postac nie może iść dalej.
     */
    public boolean canPenetrate(float x, float y){
        ArrayList<BomberTile> walls = screen.mapGrid.stream().filter(t -> t.type == 'w' || t.type == 'o').collect(Collectors.toCollection(ArrayList::new));

        for(BomberTile t : walls)
        {
            if(haveCollision(x, y, t))
                return true;
        }
        return false;
    }

    public boolean picksUpSuperbomb(float x, float y)
    {
        for(BomberTile t : screen.superbombTiles)
        {
            if(haveCollision(x, y, t))
            {
                screen.superbombTiles.remove(t);
                return true;
            }
        }
        return false;
    }

    /**
     * Sprawdza, czy występuje kolizja prostokątów.
     * @param x Współrzędna x lewego dolnego rogu.
     * @param y Współrzedna y lewego dolnego rogu
     * @param t Kafelek, z którym może wystąpić kolizja.
     * @return true jeżeli występuje kolizja.
     */
    private boolean haveCollision(float x, float y, BomberTile t)
    {
        return (x < t.x + screen.TILE_WIDTH && y < t.y +
                screen.TILE_HEIGHT && x +
                screen.HERO_SCALING_FACTOR*screen.TILE_WIDTH > t.x && y +
                screen.HERO_SCALING_FACTOR*screen.TILE_HEIGHT > t.y);
    }
}
