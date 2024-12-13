package org.example.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    public static void createFolderPathIfNotExist(String foldername) {
        Path folderPath = Paths.get(foldername);
        try {
            if(Files.notExists(folderPath)) {
                Files.createDirectory(folderPath);
            }
        } catch (IOException exp) {
            // ignored
        }
    }

    public static boolean isFileExist(String filepath) {
        File file = new File(filepath);
        return file.exists();
    }

    public static void writeToFile(String content, String filepath) throws IOException {
        try(FileWriter fileWriter = new FileWriter(filepath)) {
            fileWriter.write(content);
            fileWriter.flush();
        }
    }

}
