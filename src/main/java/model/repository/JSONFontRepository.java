package model.repository;

import model.entity.Font;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import util.JSONParser;

import java.io.IOException;
import java.util.*;

/**
 * @author Alexander Diachenko.
 */
public class JSONFontRepository implements FontRepository {

    private final static Logger logger = Logger.getLogger(JSONFontRepository.class);
    private ObjectMapper mapper = new ObjectMapper();
    private Set<Font> fonts;

    public JSONFontRepository() {
        fonts = getFonts();
    }

    @Override
    public Set<Font> getFonts() {
        try {
            return new HashSet<>(mapper.readValue(JSONParser.readFile("./data/fonts.json"), new TypeReference<List<Font>>() {
            }));
        } catch (IOException exception) {
            logger.error(exception.getMessage(), exception);
        }
        return new HashSet<>();
    }

    @Override
    public Optional<Font> getFontByName(String name) {
        for (Font font : fonts) {
            if (font.getName().equalsIgnoreCase(name)) {
                return Optional.of(font);
            }
        }
        return Optional.empty();
    }
}
