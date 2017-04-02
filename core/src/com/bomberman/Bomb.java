package com.bomberman;


import com.bomberman.interfaces.Collidable;

/**
 * Klasa reprezentująca bombę na planszy.
 * @author Paweł Kulig, Wojciech Sobczak
 */
public class Bomb implements Collidable {

    /**
     * Mówi, czy dana bomba jest superbombą czy normalną bombą.
     */
    public boolean isSuperbomb;

    /**
     * Współrzędna x bomby.
     */
    public float x;

    /**
     * Współrzędna y bomby.
     */
    public float y;

    /**
     * Czas podłożenia bomby.
     */
    public long plantingTime;

    /**
     * Mówi, czy bomba została podłożona przez użytkownika, czy przez potwora.
     */
    public boolean isPlantedByUser;

    /**
     * Główny konstruktor.
     * @param isSuperbomb Czy dana bomba jest superbombą.
     * @param x Współrzędna x.
     * @param y Współrzędna y.
     * @param plantingTime Czas podłożenia bomby.
     */
    public Bomb(boolean isSuperbomb, float x, float y, long plantingTime, boolean isPlantedByUser)
    {
        this.isSuperbomb = isSuperbomb;
        this.x = x;
        this.y = y;
        this.plantingTime = plantingTime;
        this.isPlantedByUser = isPlantedByUser;
    }

    @Override
    public boolean hasCollisionWith(Collidable other) {
        return false;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }
}
