package com.bomberman;

import javafx.geometry.Point2D;

/**
 * Reprezentuje jeden kafelek na mapie gry.
 * @author Paweł Kulig
 */
public class BomberTile {

    /**
     * Współrzędna x lewego dolnego rogu kafelka.
     */
    public final float x;

    /**
     * Współrzędna y lewego dolnego rogu kafelka.
     */
    public final float y;

    /**
     * Typ kafelka; p - przejście, o - przeszkoda, w - ściana
     */
    public char type;

    public BomberTile(float x, float y, char type){
        this.x = x;
        this.y = y;
        this.type = type;
    }

}
