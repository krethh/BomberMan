package com.bomberman.interfaces;

import com.bomberman.screens.BomberExplosion;

/**
 * Created by Paweł Kulig on 22.03.2017.
 */
public interface ExplosionObserver {

    public void handleExplosion(BomberExplosion e);
}
