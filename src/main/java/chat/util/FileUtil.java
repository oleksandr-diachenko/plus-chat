package chat.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexander Diachenko
 */
public class FileUtil {

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
}
