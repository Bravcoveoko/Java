package cz.muni.fi.pb162.hw01.impl;

import cz.muni.fi.pb162.hw01.InfiniteCellSequence;

/**
 * Class for cells which contains number value (String).
 * @author Lukas Hajda
 */

public class NumberCell extends InfiniteCellSequence {

    private int value;
    private static final NumberCell MAINNUMBERCELL = new NumberCell(1);

    /**
     *
     * @param value to be put.
     */

    public NumberCell(int value) {
        this.value = value;
    }

    /**
     *
     * @return return letter in String form. int -> String.
     */

    @Override
    public String getValue() {
        return "" + value;
    }

    /**
     * If this method is called multiple times it has to return reference.
     * So there is {@link NumberCell(char)#mainNumberCell}
     * @return {@link NumberCell(char)#mainNumberCell}
     */

    @Override
    public NumberCell startingValue() {
        return MAINNUMBERCELL;
    }

    /**
     *
     * @return new object with next value from previous object.
     */

    @Override
    public NumberCell nextValue() {
        return new NumberCell(this.value + 1);
    }
}
