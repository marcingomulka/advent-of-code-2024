package com.example.advent.day4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class Main {
    public static final String XMAS = "XMAS";
    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();
        int maxX = lines.get(0).length();
        int maxY = lines.size();
        //System.out.println(maxX + "  " + maxY);
        char[][] board = new char[maxX][maxY];
        for (int j = 0; j < maxY; j++) {
            String line = lines.get(j);
            char[] chars = line.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                board[i][j] = chars[i];
            }
        }
        //printBoard(maxX, maxY, board);
        int wordCount = 0;
        for (int j = 0; j < maxY; j++) {
            for (int i = 0; i < maxX; i++) {
                wordCount += checkAllDirections(i, j, board);
            }
        }
        System.out.println("Part1: " + wordCount);

        int masCount = 0;
        for (int j = 1; j < maxY - 1; j++) {
            for (int i = 1; i < maxX - 1; i++) {
                if (board[i][j] == 'A') {
                    masCount += checkMas(i, j, board);
                }
            }
        }
        System.out.println("Part2: " + masCount);
    }

    private static int checkMas(int i, int j, char[][] board) {
        if(board[i - 1][j - 1] == 'M'
                && board [i + 1][j - 1] == 'M'
                && board [i - 1][j + 1] == 'S'
                && board [i + 1][j + 1] == 'S')
            return 1;
        if(board[i - 1][j - 1] == 'M'
                && board [i + 1][j - 1] == 'S'
                && board [i - 1][j + 1] == 'M'
                && board [i + 1][j + 1] == 'S')
            return 1;
        if(board[i - 1][j - 1] == 'S'
                && board [i + 1][j - 1] == 'M'
                && board [i - 1][j + 1] == 'S'
                && board [i + 1][j + 1] == 'M')
            return 1;
        if(board[i - 1][j - 1] == 'S'
                && board [i + 1][j - 1] == 'S'
                && board [i - 1][j + 1] == 'M'
                && board [i + 1][j + 1] == 'M')
            return 1;

        return  0;
    }

    private static int checkAllDirections(int i, int j, char[][] board) {
        List<Pair<Integer>> directions = List.of(
                new Pair(-1, -1),
                new Pair(-1, 0),
                new Pair(-1, 1),
                new Pair(0, -1),
                new Pair(1, -1),
                new Pair(1, 0),
                new Pair(1, 1),
                new Pair(0, 1)
        );
        int sumDirs = 0;
        for (Pair<Integer> direction : directions) {
            if (board[i][j] == XMAS.charAt(0)) {
                int count = checkXmas(i, j, board, direction, 1);
                sumDirs += count;
            }
        }
        return sumDirs;
    }

    private static int checkXmas(int i, int j, char[][] board, Pair<Integer> direction, int pos) {
        int x = direction.first;
        int y = direction.second;
        if (pos == XMAS.length()) {
            //System.out.println(i + ", " + j);
            return 1;
        }
        if (i + x >= 0 && i + x < board[0].length && j + y >= 0 && j + y < board.length) {
            if (pos < XMAS.length() && board[i + x][j + y] == XMAS.charAt(pos) ) {
                return checkXmas(i + x, j + y, board, direction, pos + 1);
            }
        }
        return 0;
    }

    private static void printBoard(int maxX, int maxY, char[][] board) {
        for (int j = 0; j < maxY; j++) {
            for (int i = 0; i < maxX; i++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    private static List<String> readInput() throws IOException {
        List<String> lines;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}
