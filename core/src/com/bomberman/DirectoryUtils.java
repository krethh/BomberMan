package com.bomberman;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Zawiera metody służące do znajdywania i manipulowania plikami w folderach z grą.
 */
public class DirectoryUtils {

    /**
     * Przeszukuje folder w poszukiwaniu plików z danym rozszerzeniem.
     * @param folder Folder, który należy przeszukać, relatywny do folderu głównego projektu.
     * @param extension Rozszerzenie plików np. .txt
     * @return Lista ścieżek do plików z danym rozszerzeniem
     */
    public static ArrayList<String> findFilesWithExtension(String folder, String extension)
    {
        // znajduje ścieżkę do folderu projektu
        String absolutePath = new File("").getAbsolutePath();

        ArrayList<String> files = new ArrayList<String>();
        File dir = new File(absolutePath + File.separator + folder);

        for (File file : dir.listFiles())
        {
            //TODO: posortować pliki
            if(file.getName().endsWith(extension))
                files.add(absolutePath + File.separator + "maps" + File.separator + file.getName());
        }
        return files;
    }
}
