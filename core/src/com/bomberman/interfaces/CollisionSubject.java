package com.bomberman.interfaces;

/**
 * Created by Paweł Kulig on 22.03.2017.
 */
public interface CollisionSubject {

    public void register(CollisionObserver o);

    public void notifyObservers();
}
