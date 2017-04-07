package com.bomberman;

import com.bomberman.interfaces.Collidable;
import com.bomberman.interfaces.ExplosionObserver;
import com.bomberman.screens.MainGameScreen;

/**
 * Reprezentuje jeden kafelek na mapie gry.
 * @author Paweł Kulig
 */
public class BomberTile implements Collidable, ExplosionObserver {


    private MainGameScreen screen;

    /**
     * Współrzędna x lewego dolnego rogu kafelka.
     */
    public float x;

    /**
     * Współrzędna y lewego dolnego rogu kafelka.
     */
    public float y;

    /**
     * Typ kafelka; p - przejście, o - przeszkoda, w - ściana
     */
    public char type;

    /**
     * Konstuktor kafelka.
     * @param x Wspołrzędna x lewego dolnego rogu prostokąta.
     * @param y Wspołrzędna y lewego dolnego rogu prostokąta.
     * @param type Typ kafelka.
     * @param screen Ekran, na którym jest kafelek.
     */
    public BomberTile(float x, float y, char type, MainGameScreen screen){
        this.x = x;
        this.y = y;
        this.type = type;
        this.screen = screen;
    }

    /**
     * Sprawdza, czy ma kolizję z innym obiektem.
     * @param other Inny obiekt.
     * @return True, jeżeli ma kolizję.
     */
    @Override
    public boolean hasCollisionWith(Collidable other) {
        return (x < other.getX() + screen.TILE_WIDTH && y < other.getY() +
                screen.TILE_HEIGHT && x +
                screen.TILE_WIDTH > other.getX() && y +
                screen.TILE_HEIGHT > other.getY());
    }

    /**
     * Zwraca współrzędną x.
     * @return Współrzędna x.
     */
    @Override
    public float getX() {
        return x;
    }

    /**
     * Zwraca współrzędną y.
     * @return Współrzędna y.
     */
    @Override
    public float getY() {
        return y;
    }

    /**
     * Handler eksplozji.
     * @param e Eksplozja
     */
    @Override
    public void handleExplosion(BomberExplosion e) {

        /// jeżeli jesteś na drodze eksplozji, zmień się z przeszkody na przejście.
        e.shockwave.shockwavePath.stream().forEach(t ->{
            if(t == this) {
                type = 'p';
                if(e.shockwave.bomb.isPlantedByUser)
                    screen.game.points += 5;
            }
        });
    }
}
