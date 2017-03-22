package com.bomberman.interfaces;

/**
 * Created by Paweł Kulig on 22.03.2017.
 */
public interface ExplosionSubject {

    public void register(ExplosionObserver o);
    public void notifyObservers();

}
