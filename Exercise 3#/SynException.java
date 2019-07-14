package cz.muni.fi.pb162.hw03;

/**
 * @author Lukas Hajda
 */
public class SynException extends Exception{

    /**
     * called if there si syntax error : missing semicolon, missing comma, missing whitespace etc.
     */

    public SynException() {
        System.err.println("Syntax error");
    }
}

