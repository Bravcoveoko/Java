package cz.muni.fi.pb162.hw03;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Lukas Hajda
 */

public class Trim {

    /**
     * Items can contain redundant whitespaces so this methode delete them
     * Helps a lot in future work with these items.
     * @param list list of arguments
     * @return list of arguments without whitespaces.
     */

    public static List<String> removeWhiteSpaces(List<String> list) {
        List<String> result = new ArrayList<>();

        list.stream().map(String::trim).forEach(result::add);

        return Collections.unmodifiableList(result);
    }
}
