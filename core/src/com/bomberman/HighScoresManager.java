package com.bomberman;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Zarządza najwyższymi wynikami.
 */
public class HighScoresManager {

    /**
     * Przechowuje najwyższe wyniki.
     */
    private Properties scores;

    /**
     * Główny konstruktor.
     */
    public HighScoresManager() throws IOException {

        scores = new Properties();

        InputStream input = new FileInputStream("highscores.config");
        scores.load(input);
    }

    /**
     * Zwraca najwyższy wynik danego gracza.
     * @param nick Nick gracza.
     * @return Najwyższy wynik gracza. Jeżeli nie ma gracza, zwraca -1.
     */
    public int getScore(String nick)
    {
        if(scores.containsKey(nick))
            return Integer.valueOf(scores.getProperty(nick));

        else return -1;
    }

    /**
     * Zwraca listę wszystkich nicków, które są w pierwszej 10.
     * @return Haszmapa wszystkich nicków, które są w pierwszej 10, z korespondującym numerem na liście.
     */
    public HashMap<Integer, String> getHighScores()
    {
        HashMap<Integer, String> map = new HashMap<>();
        HashMap<Integer, List<String>> hasBigger = new HashMap<>();

        Set<String> nicks = scores.stringPropertyNames();

        int offset = 0;

        for(String nick : nicks)
        {
            int val = getScore(nick);
            int biggerCount = offset;

            for(String key : nicks)
            {
                if(getScore(key) > val)
                    biggerCount++;
            }

            if(!hasBigger.containsKey(biggerCount))
            {
                List<String> list = new ArrayList<>();
                list.add(nick);
                hasBigger.put(biggerCount, list);
            }

            else{
                hasBigger.get(biggerCount).add(nick);
            }
        }

        int added = 0;
        for(int i = 0; i < 10 && added < 10; i++)
        {
            if(!hasBigger.containsKey(i))
                continue;

            List<String> nicksList = hasBigger.get(i);
            for(String nick : nicksList)
            {
                map.put(added, nick);
                if(++added == 10)
                    break;
            }
        }
        return map;
    }

    /**
     * Ustawia najwyższy wynik gracza, jeżeli jest on wyższy niż poprzedni najwyższy wynik
     * @param nick Nick gracza.
     * @param value Wynik do ustawienia.
     */
    public void setScoreIfBetter(String nick, int value){
        if(getScore(nick) > value)
            return;

        scores.setProperty(nick, String.valueOf(value));
        try {
            scores.store(new FileOutputStream("highscores.config"), "");
        } catch (IOException e) {
            System.exit(100);
        }
    }


}
