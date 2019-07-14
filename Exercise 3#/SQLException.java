package cz.muni.fi.pb162.hw03;

/**
 * @author Lukas Hajda
 */

public class SQLException extends Exception{

    /**
     * Every create/select/insert/select class has SQL exception.
     * EXP: Trying create table which already exists ...
     * @param message to be printed.
     */
    public SQLException(String message) {
        System.err.println(message);
    }

}

