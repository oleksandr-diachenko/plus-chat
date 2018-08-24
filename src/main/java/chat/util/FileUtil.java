package chat.util;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexander Diachenko
 */
public class FileUtil {

    public static Set<File> getFilesFromFolder(final String path) {
        final Set<File> result = new HashSet<>();
        final File folder = new File(path);
        final File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                result.add(file);
            }
        }
        return result;
    }
}
