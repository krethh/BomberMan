package com.bomberman;

import com.bomberman.interfaces.Collidable;
import com.bomberman.interfaces.ExplosionObserver;
import com.bomberman.screens.BomberExplosion;
import com.bomberman.screens.MainGameScreen;
import javafx.geometry.Point2D;

/**
 * Reprezentuje jeden kafelek na mapie gry.
 * @author Paweł Kulig
 */
public class BomberTile implements Collidable, ExplosionObserver {


    private MainGameScreen screen;

    /**
     * Współrzędna x lewego dolnego rogu kafelka.
     */
    private final float x;

    /**
     * Współrzędna y lewego dolnego rogu kafelka.
     */
    private final float y;

    /**
     * Typ kafelka; p - przejście, o - przeszkoda, w - ściana
     */
    public char type;

    public BomberTile(float x, float y, char type, MainGameScreen screen){
        this.x = x;
        this.y = y;
        this.type = type;
        this.screen = screen;
    }

    @Override
    public boolean hasCollisionWith(Collidable other) {
        return (x < other.getX() + screen.TILE_WIDTH && y < other.getY() +
                screen.TILE_HEIGHT && x +
                screen.TILE_WIDTH > other.getX() && y +
                screen.TILE_HEIGHT > other.getY());
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void handleExplosion(BomberExplosion e) {
        e.shockwave.shockwavePath.stream().forEach(t ->{
            if(t == this)
                type = 'p';
        });
    }
}
