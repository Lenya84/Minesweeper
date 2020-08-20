package minesweeper;

import java.util.*;

public class Main {

    public static final int boardHeight = 9;
    public static final int boardWidth = 9;

    static char[][] field;
    static Random random;
    static Scanner scanner;
    static Set<List<Integer>> mines;
    static Set<List<Integer>> playersMarks;

    static {
        mines = new HashSet<>();
        playersMarks = new HashSet<>();
        field = new char[boardHeight][boardWidth];
        random = new Random(11111);
        scanner = new Scanner(System.in);

        for (char[] chars : field) {
            Arrays.fill(chars, '.');
        }
    }

    public static void createMines(int minesCount) {
        int currentMinesCount = 0;

        while (currentMinesCount != minesCount) {
            int i = random.nextInt(field.length);
            int j = random.nextInt(field[i].length);

            if (field[i][j] == '.') {
                field[i][j] = 'X';
                ++currentMinesCount;
            }
        }
    }

    public static void setOrDeleteMinesMarks(int i, int j) {
        if ((field[i][j] < 60 || field[i][j] > 71) && field[i][j] != '*') {
            field[i][j] = '*';
            playersMarks.add(List.of(i , j));
        } else if (field[i][j] == '*') {
            field[i][j] = '.';
            playersMarks.remove(List.of(i, j));
        } else {
            System.out.println("There is a number here!");
        }
    }

    public static int getNumNeighbors(int y, int x) {
        int numNeighbors = 0;

        for (int i = y - 1; i < y + 2; i++) {
            if (i >= 0 && i < boardHeight) {
                for (int j = x - 1; j < x + 2; j++) {
                    if (j >= 0 && j < boardWidth) {
                        if (field[i][j] == 'X') {
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
                if (field[i][j] != 'X') {
                    int numNeighbors = getNumNeighbors(i, j);
                    if (numNeighbors != 0) {
                        field[i][j] = (char) (numNeighbors + '0');
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
            for (char ch : field[i]) {
                System.out.print(ch == 'X' ? '.' : ch);
            }
            System.out.print("|\n");
        }

        System.out.println("—│—————————│");
    }

    public static void main(String[] args) {

        System.out.println("How many mines do you want on the field?");
        int minesCount = scanner.nextInt();

        createMines(minesCount);
        updateAllNeighbors();
        printField();

        while (!mines.equals(playersMarks)) {
            System.out.print("Set/delete mines marks (x and y coordinates): ");
            int j = scanner.nextInt() - 1;
            int i = scanner.nextInt() - 1;

            setOrDeleteMinesMarks(i, j);
            printField();
        }

        System.out.println("Congratulations! You found all mines!");



    }
}
