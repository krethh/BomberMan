package com.bomberman;

import com.bomberman.interfaces.Collidable;
import com.bomberman.interfaces.CollisionObserver;
import com.bomberman.interfaces.CollisionSubject;
import com.bomberman.screens.MainGameScreen;

import java.util.ArrayList;

/**
 * Klasa reprezentująca kolizję czegoś z czymś.
 */
public class BomberCollision implements CollisionSubject {

    /**
     * Pierwszy obiekt kolizji.
     */
    public Collidable firstObject;

    /**
     * Drugi obiekt kolizji.
     */
    public Collidable secondObject;

    /**
     * Wyliczenie możliwych rodzajów kolizji.
     */
    public enum collisionType {HERO_SHOCKWAVE, HERO_BOMB, ENEMY_SHOCKWAVE, MULTIBOMB, SUPERBOMB, ELIMINATION, HERO_CHERRY };

    /**
     * Typ tej konkretnej kolizji.
     */
    public collisionType type;

    /**
     * Obserwatorzy kolizji.
     */
    private ArrayList<CollisionObserver> observers;

    /**
     * Konstruktor kolizji.
     * @param firstObject Pierwszy obiekt kolidujący
     * @param secondObject Drugi obiekt kolidujący
     * @param type Rodzaj kolizji
     */
    public BomberCollision(Collidable firstObject, Collidable secondObject, collisionType type)
    {
        observers = new ArrayList<>();
        this.firstObject = firstObject;
        this.secondObject = secondObject;
        this.type = type;
    }

    /**
     * Rejestracja obserwatorów kolizji.
     * @param o Obserwator do zarejestrowania.
     */
    @Override
    public void register(CollisionObserver o) {
        observers.add(o);
    }

    /**
     * Powiadomienie obserwatorów o kolizji.
     */
    @Override
    public void notifyObservers() {
        for(CollisionObserver o : observers)
            o.handleCollision(this);
    }
}
