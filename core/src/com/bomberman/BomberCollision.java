package com.bomberman;

import com.bomberman.interfaces.Collidable;
import com.bomberman.interfaces.CollisionObserver;
import com.bomberman.interfaces.CollisionSubject;
import com.bomberman.screens.MainGameScreen;

import java.util.ArrayList;

/**
 * Created by Paweł Kulig on 22.03.2017.
 */
public class BomberCollision implements CollisionSubject {

    public Collidable firstObject;
    public Collidable secondObject;

    public enum collisionType {HERO_SHOCKWAVE, HERO_BOMB, ENEMY_SHOCKWAVE, MULTIBOMB, SUPERBOMB, ELIMINATION };

    public collisionType type;

    private ArrayList<CollisionObserver> observers;

    public BomberCollision(Collidable firstObject, Collidable secondObject, collisionType type)
    {
        observers = new ArrayList<>();
        this.firstObject = firstObject;
        this.secondObject = secondObject;
        this.type = type;
    }

    @Override
    public void register(CollisionObserver o) {
        observers.add(o);
    }

    @Override
    public void notifyObservers() {
        for(CollisionObserver o : observers)
            o.handleCollision(this);
    }
}
