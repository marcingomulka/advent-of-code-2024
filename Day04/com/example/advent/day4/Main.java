package com.example.advent.day4;

import io.vavr.Tuple2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Main {

    public static final String XMAS = "XMAS";
    public static final List<Tuple2<Integer, Integer>> DIRECTIONS = List.of(
            new Tuple2<>(-1, -1),
            new Tuple2<>(-1, 0),
            new Tuple2<>(-1, 1),
            new Tuple2<>(0, -1),
            new Tuple2<>(1, -1),
            new Tuple2<>(1, 0),
            new Tuple2<>(1, 1),
            new Tuple2<>(0, 1)
    );

    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();
        int maxX = lines.get(0).length();
        int maxY = lines.size();
        char[][] board = new char[maxX][maxY];
        for (int j = 0; j < maxY; j++) {
            String line = lines.get(j);
            char[] chars = line.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                board[i][j] = chars[i];
            }
        }
        int part1WordCount = IntStream.range(0, maxY)
                .flatMap(j -> IntStream.range(0, maxX)
                                .map(i -> checkXmas(i, j, board))
                ).sum();

        System.out.println("Part1: " + part1WordCount);

        long part2WordCount = IntStream.range(1, maxY - 1)
                .flatMap(j -> IntStream.range(1, maxX - 1)
                        .filter(i -> board[i][j] == 'A' && checkMas(i, j, board))
                ).count();

        System.out.println("Part2: " + part2WordCount);
    }

    private static boolean checkMas(int i, int j, char[][] board) {
        char topLeft = board[i - 1][j - 1];
        char bottomLeft = board[i + 1][j - 1];
        char topRight = board[i - 1][j + 1];
        char bottomRight = board[i + 1][j + 1];
        char[] sequence = new char[] {topLeft, topRight, bottomLeft, bottomRight};
        return Arrays.equals(sequence, "MMSS".toCharArray())
                || Arrays.equals(sequence, "MSMS".toCharArray())
                || Arrays.equals(sequence, "SMSM".toCharArray())
                || Arrays.equals(sequence, "SSMM".toCharArray());
    }

    private static int checkXmas(int i, int j, char[][] board) {
        int sumDirs = 0;
        for (Tuple2<Integer, Integer> direction : DIRECTIONS) {
            if (board[i][j] == XMAS.charAt(0) && checkDirection(i, j, board, direction, 1)) {
                sumDirs++;
            }
        }
        return sumDirs;
    }

    private static boolean checkDirection(int i, int j, char[][] board, Tuple2<Integer, Integer> direction, int pos) {
        i += direction._1();;
        j += direction._2();
        if (pos == XMAS.length()) {
            return true;
        }
        if (i >= 0 && i < board[0].length && j >= 0 && j < board.length) {
            if (pos < XMAS.length() && board[i][j] == XMAS.charAt(pos) ) {
                return checkDirection(i, j, board, direction, pos + 1);
            }
        }
        return false;
    }

    private static void printBoard(int maxX, int maxY, char[][] board) {
        IntStream.range(0, maxY)
                .forEach(j -> {
                    String row = IntStream.range(0, maxX)
                            .mapToObj(i -> String.valueOf(board[i][j]))
                            .collect(Collectors.joining());
                    System.out.println(row);
                });
    }

    private static List<String> readInput() throws IOException {
        List<String> lines;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}
