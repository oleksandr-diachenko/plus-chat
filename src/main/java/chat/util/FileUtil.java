package chat.util;

import java.io.File;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alexander Diachenko
 */
public class FileUtil {

    public static Set<File> getFilesFromFolder(final String path) {
        final File folder = new File(path);
        final File[] files = folder.listFiles();
        return Arrays.stream(files)
                .filter(File::isFile)
                .collect(Collectors.toSet());
    }
}
