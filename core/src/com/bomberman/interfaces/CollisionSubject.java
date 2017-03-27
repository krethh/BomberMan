package com.bomberman.interfaces;

/**
 * Przedmiot kolizji.
 */
public interface CollisionSubject {

    /**
     * Rejestrowanie obserwatorów.
     * @param o Obserwator do zarejestrowania.
     */
    public void register(CollisionObserver o);

    /**
     * Powiadomienie obserwatorów o wystąpieniu kolizji.
     */
    public void notifyObservers();
}
