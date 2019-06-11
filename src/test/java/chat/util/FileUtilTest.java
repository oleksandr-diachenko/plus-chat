package chat.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;

import static org.junit.Assert.assertEquals;
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
    public void shouldReturnJsonStringWhenFilePathCorrect() throws URISyntaxException {
        String jsonString = FileUtil.readFile(getResource("\\json\\simpleJsonOneObject.json"));
        assertEquals("" +
                "{\r\n" +
                "  \"name\": \"Alex\",\r\n" +
                "  \"nick\": \"POSITIV\",\r\n" +
                "  \"birthday\": 1989\r\n" +
                "}", jsonString);
    }

    @Test
    public void shouldReturnEmptyStringWhenFilePathIncorrect() {
        String jsonString = FileUtil.readFile("qwe.json");
        assertEquals("", jsonString);
    }

    @Test
    public void shouldReturnFilesWhenFilePathCorrect() throws FileNotFoundException {
        Set<File> files = FileUtil.getFilesFromFolder(temp.toAbsolutePath().toString());
        assertFalse(files.isEmpty());
    }

    @Test(expected = FileNotFoundException.class)
    public void shouldThrowExceptionWhenFilePathIncorrect() throws FileNotFoundException {
        FileUtil.getFilesFromFolder("qwe");
    }

    private String getResource(final String path) throws URISyntaxException {
        URI uri = ClassLoader.getSystemResource(path).toURI();
        return Paths.get(uri).toString();
    }

    @After
    public void cleanup() throws IOException {
        Files.walkFileTree(temp, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}