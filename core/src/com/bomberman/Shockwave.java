package com.bomberman;

/**
 * Klasa reprezentująca falę uderzeniową.
 */
public class Shockwave {

    /**
     * Bomba, która spowodowała falę uderzeniową.
     */
    public Bomb bomb;

    /**
     * Czas, kiedy zaszła eksplozja.
     */
    public long explosionTime;

    /**
     * Kafelek, w którym zaszła eksplozja.
     */
    public BomberTile tileOfExplosion;

    /**
     * Konstruktor fali uderzeniowej.
     * @param bomb Bomba, po któej zaszła eksplozja.
     * @param explosionTime Czas eksplozji.
     */
    public Shockwave(Bomb bomb, long explosionTime, BomberTile tileOfExplosion)
    {
        this.bomb = bomb;
        this.explosionTime = explosionTime;
        this.tileOfExplosion = tileOfExplosion;
    }



}


