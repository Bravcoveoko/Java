package cz.muni.fi.pb162.hw01.impl;

import cz.muni.fi.pb162.hw01.Cell;
import cz.muni.fi.pb162.hw01.Sudoku;
import cz.muni.fi.pb162.hw01.helper.ArrayUtils;
import cz.muni.fi.pb162.hw01.helper.SudokuChecker;

import static cz.muni.fi.pb162.hw01.helper.ArrayUtils.EMPTY_ARRAY;

/**
 * Represents basic sudoku.
 *
 * @author Lukas Hajda
 */

public class BasicSudoku implements Sudoku {

    private Cell[][] board;
    private  Cell[] possibleValues;
    private SudokuChecker sudokuChecker;

    /**
     *
     * @param board Where the cells will be put.
     * @param possibleValues which values can be put.
     */

    public BasicSudoku(Cell[][] board, Cell[] possibleValues) {
        this.board = board;
        this.possibleValues = possibleValues;
        this.sudokuChecker = new SudokuChecker(this);
    }

    /**
     *
     * @return dimension of sudoku board.
     */

    @Override
    public int getSize() {
        return board.length;
    }

    /**
     * Dimension of block.
     * @return Root from {@link BasicSudoku#getSize()}
     */

    @Override
    public int getBlockSize() {
        return (int)Math.sqrt(this.getSize());
    }

    /**
     * "Getter" of attribute named possibleValues which contains all values that can
     * be put in sudoku based on its value type which can be {@link LetterCell} or {@link NumberCell}.
     * @return array of values.
     */

    @Override
    public Cell[] availableValues() {
        return possibleValues;
    }

    /**
     * Return cell from board which is represented as matrix.
     * @param column cell column index
     * @param row    cell row index
     * @return specific cell from given coordinates.
     */

    @Override
    public Cell getCell(int column, int row) {
        return board[column][row];
    }

    /**
     *
     * @param row row index
     * @return array full of cells which are in same row from board.
     */

    @Override
    public Cell[] getRow(int row) {
        Cell[] result = new Cell[board.length];
        for(int i = 0; i < board.length; i++) {
            result[i] = board[i][row];
        }
        return result;
    }

    /**
     * Like {@link BasicSudoku#getRow(int)} but it returns column
     * @param column column index
     * @return array full of cells which are in same column from board.
     */

    @Override
    public Cell[] getColumn(int column) {
        Cell[] result = new Cell[getSize()];
        for(int i = 0; i < getSize(); i++) {
            result[i] = board[column][i];
        }
        return result;
    }

    /**
     * Variable 'result' has length equals to {@link BasicSudoku#getSize()}
     * Cells which are put to the result stars at the column position
     * globalC * getBlockSize() and row position globalR * getBlockSize().
     *
     * @param result will be filled with cells from given block coordinates.
     * @param globalC block column coordinate
     * @param globalR block row coordinate
     * @param board sudoku board
     */

    private void getCellsFromBlock(Cell[] result, int globalC, int globalR, Cell[][] board) {
        int index = 0;
        int columnPos = globalC * getBlockSize();
        int rowPos = globalR * getBlockSize();
        int blockSize = getBlockSize();
        for(int i = rowPos; i < rowPos + blockSize; i++) {
            for(int j = columnPos; j < columnPos + blockSize; j++) {
                result[index] = board[j][i];
                index += 1;
            }
        }

    }

    /**
     *
     * @param globalColumn block column index
     * @param globalRow    block rox index
     * @return array full of cells which are in same block.
     */

    @Override
    public Cell[] getBlock(int globalColumn, int globalRow) {
        Cell[] result = new Cell[getSize()];
        getCellsFromBlock(result, globalColumn, globalRow, board);
        return result;
    }

    /**
     * If cell on given column and row position si not empty array is returned.
     *
     * Otherwise every value from possibleValues is checked in {@link SudokuChecker#canInsert(int, int, Cell)}.
     * if value is valid it is put in to the result array variable.
     * @param column column index
     * @param row    row index
     * @return array full of valid cells that can be put into given column and row position.
     */

    @Override
    public Cell[] getOptions(int column, int row) {
        if (board[column][row] != null){
            return EMPTY_ARRAY;
        }
        int index = 0;
        Cell[] result = new Cell[getSize()];
        for(int i = 0; i < getSize(); i++) {
            if (sudokuChecker.canInsert(column, row, possibleValues[i])) {
                result[index] =  possibleValues[i];
                index += 1;
            }
        }
        return result;
    }

    /**
     * Used already implemented methods in {@link ArrayUtils#singleElement(Cell[])}
     * Will Reduce result of getOption to a single Cell , null otherwise.
     * @param column column index
     * @param row    row index
     * @return cell or null from reducing result from getOptions.
     */

    @Override
    public Cell getHint(int column, int row) {
        return ArrayUtils.singleElement(getOptions(column, row));
    }

    /**
     *
     * @return matrix full of {@link BasicSudoku#getHint(int, int)} results.
     */

    @Override
    public Cell[][] getAllHints() {
        Cell[][] result = new Cell[this.getSize()][this.getSize()];
        for(int i = 0; i < this.getSize(); i++) {
            for(int j = 0; j < this.getSize(); j++) {
                result[j][i] = this.getHint(j, i);
            }
        }
        return result;
    }

    /**
     * Column , row and given cell are put to the {@link SudokuChecker#canInsert(int, int, Cell)}.
     * If it is True cell 'c' is put to the board and return true for successful insert , False otherwise.
     * @param column column index
     * @param row    row index
     * @param c      element to be put
     * @return True for successful insert , False otherwise.
     */

    @Override
    public boolean putElement(int column, int row, Cell c) {
        if (sudokuChecker.canInsert(column, row, c)) {
            board[column][row] = c;
            return true;
        }
        return false;
    }


}
