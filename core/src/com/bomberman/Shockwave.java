package com.bomberman;

import java.util.ArrayList;

/**
 * Klasa reprezentująca falę uderzeniową.
 */
public class Shockwave {

    /**
     * Odniesienie do głównego obiektu gry.
     */
    BomberMan game;

    /**
     * Lista kafelków planszy.
     */
    private ArrayList<BomberTile> mapGrid;

    /**
     * Mapa, na której zachodzi wybuch.
     */
    private BomberMap currentMap;

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
     * Lista kafelków, w których będzie fala uderzeniowa.
     */
    public ArrayList<BomberTile> shockwavePath;

    /**
     * Konstruktor fali uderzeniowej.
     * @param bomb Bomba, po któej zaszła eksplozja.
     * @param explosionTime Czas eksplozji.
     */
    public Shockwave(Bomb bomb, long explosionTime, BomberTile tileOfExplosion, ArrayList<BomberTile> mapGrid, BomberMan game, BomberMap currentMap)
    {
        this.bomb = bomb;
        this.explosionTime = explosionTime;
        this.tileOfExplosion = tileOfExplosion;
        this.mapGrid = mapGrid;
        this.game = game;
        this.currentMap = currentMap;

        generateShockwavePath();
    }

    private void generateShockwavePath()
    {
        int factor = bomb.isSuperbomb ? 2 : 1;
        shockwavePath = new ArrayList<>();
        BomberTile start = tileOfExplosion;

        shockwavePath.add(start);

        //znajdz pozycje w liscie kafelków
        int index = 0;
        for (; index < mapGrid.size(); index++)
        {
            if(mapGrid.get(index) == start)
                break;
        }

        // w prawo
        for (int i = 0, j = index; i < factor*game.bomberConfig.shockwaveLength && (j + 1) % currentMap.screenWidth != 0; i++, j++)
        {
            if(mapGrid.get(j).type == 'w')
                break;
            if(mapGrid.get(j).type == 'o')
            {
                shockwavePath.add(mapGrid.get(j));
                if(factor == 1)
                    break;
                else
                    factor = 1;
            }
            if(mapGrid.get(j).type == 'p')
                shockwavePath.add(mapGrid.get(j));
        }

        // w lewo
        for (int i = 0, j = index; i < factor*game.bomberConfig.shockwaveLength && (j) % currentMap.screenWidth != 0; i++, j--)
        {
            if(mapGrid.get(j).type == 'w')
                break;
            if(mapGrid.get(j).type == 'o')
            {
                shockwavePath.add(mapGrid.get(j));
                if(factor == 1)
                    break;
                else
                    factor = 1;
            }
            if(mapGrid.get(j).type == 'p')
                shockwavePath.add(mapGrid.get(j));
        }

        // w górę
        for (int i = 0, j = index; i < factor*game.bomberConfig.shockwaveLength && (j + 16) < currentMap.screenWidth * currentMap.screenHeight; i++, j+=16)
        {
            if(mapGrid.get(j).type == 'w')
                break;
            if(mapGrid.get(j).type == 'o')
            {
                shockwavePath.add(mapGrid.get(j));
                if(factor == 1)
                    break;
                else
                    factor = 1;
            }
            if(mapGrid.get(j).type == 'p')
                shockwavePath.add(mapGrid.get(j));
        }

        // w dół
        for (int i = 0, j = index; i < factor*game.bomberConfig.shockwaveLength && (j - 16) > 0; i++, j-=16) {
            if (mapGrid.get(j).type == 'w')
                break;
            if (mapGrid.get(j).type == 'o') {
                shockwavePath.add(mapGrid.get(j));
                if(factor == 1)
                    break;
                else
                    factor = 1;
            }
            if (mapGrid.get(j).type == 'p')
                shockwavePath.add(mapGrid.get(j));
        }
    }



}


