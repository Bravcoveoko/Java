package cz.muni.fi.pb162.hw01.impl;

import cz.muni.fi.pb162.hw01.Cell;
import cz.muni.fi.pb162.hw01.InfiniteCellSequence;
import cz.muni.fi.pb162.hw01.Sudoku;

/**
 * Sudoku builder
 * @author Lukas Hajda
 */

public class SudokuBuilder {

    private Cell[][] board;
    private Cell[] possibleValues;
    private boolean isDiagonal;

    /**
     * constructor
     * @param size dimension of board
     * @param cellType which type will values have (letter / number).
     */

    public SudokuBuilder(int size, InfiniteCellSequence cellType) {
        this.board = new Cell[size][size];
        this.possibleValues = cellType.firstNValues(size);
    }

    /**
     *
     * @param column column coordinate.
     * @param row row coordinate.
     * @param c Cell to be put.
     * @return SudokuBuilder with filled board by one cell.
     */

    public SudokuBuilder cell(int column, int row, Cell c) {
        this.board[column][row] = c;
        return this;
    }

    /**
     * If isDiagonal is true then DiagonalSudoku will be called.
     * BasicSudoku otherwise.
     * @param isDiagonal contains true or false.
     * @return SudokuBuilder
     */

    public SudokuBuilder diagonal(boolean isDiagonal) {
        this.isDiagonal = isDiagonal;
        return this;
    }

    /**
     *
     * @return new instance of diagonal sudoku or basic sudoku based on isDiagonal attribute.
     */

    public Sudoku build() {
        return isDiagonal ?
                new DiagonalSudoku(board, possibleValues)
                : new BasicSudoku(board, possibleValues);
    }


}
