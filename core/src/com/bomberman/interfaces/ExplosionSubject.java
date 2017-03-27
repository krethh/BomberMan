package com.bomberman.interfaces;

/**
 * Przedmiot eksplozji.
 */
public interface ExplosionSubject {

    /**
     * Rejestrowanie obserwatora eksplozji.
     * @param o Obserwator do zarejestrowania.
     */
    void register(ExplosionObserver o);

    /**
     * Powiadomienie o zaistnieniu eksplozji.
     */
    void notifyObservers();

}
