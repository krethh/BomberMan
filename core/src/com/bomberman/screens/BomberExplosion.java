package com.bomberman.screens;

import com.bomberman.Shockwave;
import com.bomberman.interfaces.ExplosionObserver;
import com.bomberman.interfaces.ExplosionSubject;

import java.util.ArrayList;

/**
 * Created by Paweł Kulig on 22.03.2017.
 */
public class BomberExplosion implements ExplosionSubject {

    private ArrayList<ExplosionObserver> observers;
    public Shockwave shockwave;

    public BomberExplosion(Shockwave shockwave) {
        observers = new ArrayList<>();
        this.shockwave = shockwave;
    }

    @Override
    public void register(ExplosionObserver o) {
        observers.add(o);
    }

    @Override
    public void notifyObservers() {

        for(ExplosionObserver o : observers)
            o.handleExplosion(this);
    }
}
