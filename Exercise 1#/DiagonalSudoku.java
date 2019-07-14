package cz.muni.fi.pb162.hw01.impl;

import cz.muni.fi.pb162.hw01.Cell;
import cz.muni.fi.pb162.hw01.helper.ArrayUtils;

/**
 * Represents basic sudoku with diagonal rules.
 * @author Lukas Hajda
 */


public class DiagonalSudoku extends BasicSudoku{

    private DiagonalSudokuChecker diagonalSudokuChecker;

    /**
     *
     * @param board Where the cells will be put.
     * @param possibleValues which values can be put.
     */

    public DiagonalSudoku(Cell[][] board, Cell[] possibleValues) {
        super(board, possibleValues);
        diagonalSudokuChecker = new DiagonalSudokuChecker(this);
    }

    /**
     * First, getOption from BasicSudoku is called after that we get arrays full cells which are valid for
     * basic sudoku.
     * Second, array full of cells which break rules of diagonal sudoku is made. After that
     * {@link ArrayUtils#difference(Cell[], Cell[])} is called on them.
     * @param column column index
     * @param row    row index
     * @return array full of valid cells.
     */

    @Override
    public Cell[] getOptions(int column, int row) {
        Cell[] superOptions = super.getOptions(column, row);
        int index = 0;
        Cell[] notValid = new Cell[superOptions.length];
        for(int i = 0; i < superOptions.length; i++) {
            if (!diagonalSudokuChecker.isDiagonalValid(column, row, superOptions[i])) {
                notValid[index] = superOptions[i];
                index++;
            }
        }
        return ArrayUtils.difference(superOptions, notValid);
    }

    /**
     * Check rule set for diagonal sudoku, if true putElement from Basic Sudoku is called.
     * @param column column index
     * @param row    row index
     * @param c      element to be put
     * @return True for sucessful insert , false otherwise.
     */

    @Override
    public boolean putElement(int column, int row, Cell c) {
        if (diagonalSudokuChecker.isDiagonalValid(column, row, c)) {
            return super.putElement(column, row, c);

        }
        return false;
    }
}
