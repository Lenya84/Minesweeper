package minesweeper;

import java.util.*;

class Cell {

    private char symbol;
    private boolean marked;
    private boolean isVisible;

    Cell(char symbol) {
        marked = false;
        isVisible = false;
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public void setVisible() {
        isVisible = true;
    }

    public boolean isMarked() {
        return marked;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void mark () {
        marked = !marked;
    }

    public String toString() {
        return String.valueOf(symbol);
    }
}

public class Main {

    public static final int boardHeight = 9;
    public static final int boardWidth = 9;

    static Cell[][] field;
    static Random random;
    static Scanner scanner;
    static Set<List<Integer>> mines;
    static Set<List<Integer>> playersMarks;

    static {
        mines = new HashSet<>();
        playersMarks = new HashSet<>();
        field = new Cell[boardHeight][boardWidth];
        random = new Random(11111);
        scanner = new Scanner(System.in);

        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                field[i][j] = new Cell('.');
            }
        }
    }

    public static void createMines(int minesCount) {
        int currentMinesCount = 0;

        while (currentMinesCount != minesCount) {
            int i = random.nextInt(field.length);
            int j = random.nextInt(field[i].length);

            if (field[i][j].getSymbol() == '.') {
                field[i][j].setSymbol('X');
                ++currentMinesCount;
            }
        }
    }

    public static void setOrDeleteMinesMarks(int i, int j) {
        if ((field[i][j].getSymbol() < 60 || field[i][j].getSymbol() > 71) && field[i][j].getSymbol() != '*') {
            field[i][j].mark();
            playersMarks.add(List.of(i , j));
        } else if (field[i][j].isMarked()) {
            field[i][j].mark();
            playersMarks.remove(List.of(i, j));
        } else {
            System.out.println("There is a number here!");
        }
    }

    public static boolean free(int y, int x) {
        if (field[y][x].isMarked()) {
            field[y][x].mark();
        }

        field[y][x].setVisible();

        if (field[y][x].getSymbol() == 'X') {
            for (List<Integer> coordinates : mines) {
                int i = coordinates.get(0);
                int j = coordinates.get(1);
                field[i][j].setVisible();
            }
            return false;
        }

        if (field[y][x].getSymbol() == '/') {
            for (int i = y - 1; i < y + 2; i++) {
                if (i >= 0 && i < boardHeight) {
                    for (int j = x - 1; j < x + 2; j++) {
                        if (j >= 0 && j < boardWidth) {
                            if (field[i][j].isMarked()) {
                                field[i][j].mark();
                            }

                            if (field[i][j].getSymbol() == '/' && !field[i][j].isVisible()) {
                                free(i, j);
                            }
                            field[i][j].setVisible();
                        }
                    }
                }
            }
        }

        return true;
    }

    public static int getNumNeighbors(int y, int x) {
        int numNeighbors = 0;

        for (int i = y - 1; i < y + 2; i++) {
            if (i >= 0 && i < boardHeight) {
                for (int j = x - 1; j < x + 2; j++) {
                    if (j >= 0 && j < boardWidth) {
                        if (field[i][j].getSymbol() == 'X') {
                            mines.add(List.of(i, j));
                            numNeighbors++;
                        }
                    }
                }
            }
        }

        return numNeighbors;
    }

    public static void updateAllNeighbors() {
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                if (field[i][j].getSymbol() != 'X') {
                    int numNeighbors = getNumNeighbors(i, j);
                    if (numNeighbors != 0) {
                        field[i][j].setSymbol((char) (numNeighbors + '0'));
                    } else {
                        field[i][j].setSymbol('/');
                    }
                }
            }
        }
    }

    public static void printField() {
        System.out.println(" │123456789│\n" +
                "—│—————————│");

        for (int i = 0; i < boardHeight; i++) {
            System.out.print(i + 1 + "|");
            for (Cell cell : field[i]) {
                if (cell.isMarked()) {
                    System.out.print('*');
                } else if (cell.isVisible()) {
                    System.out.print(cell);
                } else {
                    System.out.print('.');
                }
            }
            System.out.print("|\n");
        }

        System.out.println("—│—————————│");
    }

    public static void main(String[] args) {
        boolean isFree = true;

        System.out.println("How many mines do you want on the field?");
        int minesCount = scanner.nextInt();

        createMines(minesCount);
        updateAllNeighbors();
        printField();

        while (!mines.equals(playersMarks)) {
            System.out.print("Set/unset mines marks or claim a cell as free: ");
            int j = scanner.nextInt() - 1;
            int i = scanner.nextInt() - 1;
            String command = scanner.next();

            if ("mine".equals(command)) {
                setOrDeleteMinesMarks(i, j);
            } else if ("free".equals(command)) {
                isFree = free(i, j);
            }

            printField();

            if (!isFree) {
                System.out.println("You stepped on a mine and failed!");
                return;
            }

        }

        System.out.println("Congratulations! You found all mines!");



    }
}
