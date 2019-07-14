package cz.muni.fi.pb162.hw01.impl;

import cz.muni.fi.pb162.hw01.Cell;
import cz.muni.fi.pb162.hw01.Sudoku;
import cz.muni.fi.pb162.hw01.helper.ArrayUtils;

/**
 * Like {@link cz.muni.fi.pb162.hw01.helper.SudokuChecker} but it checks diagonal rules.
 * @author Lukas Hajda
 */

public class DiagonalSudokuChecker {

    private Sudoku sudoku;

    /**
     *
     * @param sudoku has to be checked.
     */

    public DiagonalSudokuChecker(Sudoku sudoku){
        this.sudoku = sudoku;
    }

    /**
     * In left diagonal column and row coordinates have to be same.
     * @param column coordinate.
     * @param row coordinate.
     * @return True if coordinates are same.
     */

    public boolean isDiagonal1(int column, int row) {
        return column == row;
    }

    /**
     * {[0,0][1,0][2,0][3,0*]
     *  [0,1][1,1][2,1*][3,1]
     *  [0,2][1,2*][2,2][3,2]
     *  [0,3*][1,3][2,3][3,3]}
     *  * - represents right diagonal row position get: size - 1 - column
     *
     * @param column coordinate.
     * @param row coordinate.
     * @return True if given column and row coordinates are in right diagonale.
     */

    public boolean isDiagonal2(int column, int row) {
        return row == sudoku.getSize() - 1 - column;
    }

    /**
     * leftDiagonal contains all cells in left diagonal.
     * If c is in leftDiagonal it means no other same c cant be put.
     * @param column coordinate.
     * @param row coordinate.
     * @param c cell to be checked.
     * @return True if 'c' is not in leftDiagonal, false otherwise.
     */

    public boolean isDiagonal1Valid(int column, int row, Cell c) {
        Cell[] leftDiagonal = diagonal1Cells();
        return !ArrayUtils.contains(leftDiagonal, c);
    }

    /**
     * Same as in {@link DiagonalSudokuChecker#isDiagonal1Valid(int, int, Cell)}
     * @param column coordinate.
     * @param row coordinate.
     * @param c cell to be checked.
     * @return True if 'c' is not in leftDiagonal, false otherwise.
     */

    public boolean isDiagonal2Valid(int column, int row, Cell c) {
        Cell[] rightDiagonal = diagonal2Cells();
        return !ArrayUtils.contains(rightDiagonal, c);
    }

    /**
     * First 'if' -  if length of board is odd there si common position for both of diagonals.
     * So conjunction of these two diagonals have to be checked.
     * Second 'if' - {@link DiagonalSudokuChecker#isDiagonal1Valid(int, int, Cell)} is called.
     * Third 'if' - {@link DiagonalSudokuChecker#isDiagonal2Valid(int, int, Cell)} is called.
     * else True because it is out of both diagonale.
     * @param column coordinate.
     * @param row coordinate.
     * @param c cell to be checked.
     * @return If no rule is broken.
     */

    public boolean isDiagonalValid(int column, int row, Cell c) {
        if(isDiagonal1(column, row) && isDiagonal2(column, row)) {
            return isDiagonal1Valid(column, row, c) ||
                    isDiagonal2Valid(column, row, c);
        }
        if (isDiagonal1(column, row)) return isDiagonal1Valid(column, row, c);
        if (isDiagonal2Valid(column, row, c)) return  isDiagonal2Valid(column, row, c);

        return true;
    }

    /**
     *
     * @return array full of cells in left diagonal.
     */

    public Cell[] diagonal1Cells() {
        Cell[] result = new Cell[sudoku.getSize()];
        for(int i = 0; i < sudoku.getSize(); i++) {
            result[i] = sudoku.getCell(i, i);
        }
        return result;
    }

    /**
     *
     * @return array full of cells in right digonal.
     */

    public Cell[] diagonal2Cells() {
        Cell[] result = new Cell[sudoku.getSize()];
        for(int i = 0; i < sudoku.getSize(); i++){
            result[i] = sudoku.getCell(sudoku.getSize() - 1 - i, i);
        }
        return result;
    }
}
