package cz.muni.fi.pb162.hw01.impl;

import cz.muni.fi.pb162.hw01.InfiniteCellSequence;

/**
 * Class for cells which contains letter value.
 * @author Lukas Hajda
 */

public class LetterCell extends InfiniteCellSequence {

    private char value;
    private static final LetterCell MAINLETTERCELL = new LetterCell('A');

    /**
     *
     * @param value to be put.
     */

    public LetterCell(char value) {
        this.value = value;
    }

    /**
     *
     * @return return letter in String form. Char -> String.
     */

    @Override
    public String getValue() {
        return "" + value;
    }

    /**
     * If this method is called multiple times it has to return reference.
     * So there is {@link LetterCell(char)#mainLetterCell}
     * @return {@link LetterCell(char)#mainLetterCell}
     */

    @Override
    public LetterCell startingValue() {
        return MAINLETTERCELL;
    }

    /**
     *
     * @return new object with next value from previous object.
     */

    @Override
    public LetterCell nextValue() {

        return new LetterCell((char)(this.value + 1));
    }


}
