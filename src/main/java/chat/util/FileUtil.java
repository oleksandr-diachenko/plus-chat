package chat.util;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexander Diachenko
 */
@Log4j2
public class FileUtil {

    private FileUtil() {
        throw new IllegalStateException("Creating object not allowed!");
    }

    public static Set<File> getFilesFromFolder(String path) throws FileNotFoundException {
        Set<File> result = new HashSet<>();
        File folder = new File(path);
        File[] files = folder.listFiles();
        if (files == null) {
            throw new FileNotFoundException("Folder " + path + " not found.");
        }
        for (File file : files) {
            if (file.isFile()) {
                result.add(file);
            }
        }
        return result;
    }

    public static String readFile(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException exception) {
            log.error(exception.getMessage(), exception);
        }
        return "";
    }
}
