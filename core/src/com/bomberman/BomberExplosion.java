package com.bomberman;

import com.bomberman.Shockwave;
import com.bomberman.interfaces.ExplosionObserver;
import com.bomberman.interfaces.ExplosionSubject;

import java.util.ArrayList;

/**
 * Klasa zdarzenia eksplozji.
 */
public class BomberExplosion implements ExplosionSubject {

    /**
     * Obserwatorzy eksplozji.
     */
    private ArrayList<ExplosionObserver> observers;

    /**
     * Fala uderzeniowa, związana z tą eksplozją.
     */
    public Shockwave shockwave;

    /**
     * Konstruktor eksplozji.
     * @param shockwave Fala, która wystąpi po tej eksplozji.
     */
    public BomberExplosion(Shockwave shockwave) {
        observers = new ArrayList<>();
        this.shockwave = shockwave;
    }

    /**
     * Rejestrowanie nowej eksplozji.
     * @param o Obserwator do zarejestrowania.
     */
    @Override
    public void register(ExplosionObserver o) {
        observers.add(o);
    }

    /**
     * Powiadomienie obserwatorów o eksplozji.
     */
    @Override
    public void notifyObservers() {

        for(ExplosionObserver o : observers)
            o.handleExplosion(this);
    }
}
