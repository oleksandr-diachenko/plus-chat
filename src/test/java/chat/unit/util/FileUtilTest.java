package chat.unit.util;

import chat.util.FileUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.Assert.assertFalse;

/**
 * @author Oleksandr_Diachenko
 */
public class FileUtilTest {

    private Path temp;

    @Before
    public void setup() throws IOException {
        temp = Files.createTempDirectory("temp_");
        Files.createTempFile(temp, "pref_", "_suffix");
    }


    @Test
    public void getFilesFromFolder() throws FileNotFoundException {
        Set<File> files = FileUtil.getFilesFromFolder(temp.toAbsolutePath().toString());
        assertFalse(files.isEmpty());
    }
}