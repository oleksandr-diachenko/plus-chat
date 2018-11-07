package chat.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alexander Diachenko
 */
public class FileUtil {

    public static Set<File> getFilesFromFolder(final String path) throws FileNotFoundException {
        final File folder = new File(path);
        final File[] files = folder.listFiles();
        if (files == null) {
            throw new FileNotFoundException("Folder " + path + " not found.");
        }
        return Arrays.stream(files)
                .filter(File::isFile)
                .collect(Collectors.toSet());
    }
}
