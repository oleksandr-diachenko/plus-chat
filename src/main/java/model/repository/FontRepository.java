package model.repository;

import model.entity.Font;

import java.util.Optional;
import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
public interface FontRepository {

    Set<Font> getFonts();
    Optional<Font> getFontByName(String name);
}
