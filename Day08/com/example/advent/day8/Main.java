package com.example.advent.day8;

import com.google.common.collect.ArrayListMultimap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Main {
    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();
        int maxY = lines.size();
        int maxX = lines.get(0).length();
        char[][] board = new char[maxY][maxX];

        ArrayListMultimap<Character, Coord> antennaGroups = ArrayListMultimap.create();
        for (int i =0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                char cell = lines.get(i).charAt(j);
                board[i][j] = cell;
                if (cell != '.') {
                    antennaGroups.put(cell, new Coord(i,j));
                }
            }
        }
        Set<Coord> resultPart1 = new HashSet<>();
        Set<Coord> resultPart2 = new HashSet<>();
        for (Character antenna: antennaGroups.keySet()) {
            List<Coord> group = antennaGroups.get(antenna);
            for (Coord pos1 : group) {
                for (Coord pos2 : group) {
                    if (pos1.equals(pos2)) {
                        continue;
                    }
                    resultPart2.add(pos1);
                    resultPart2.add(pos2);

                    Coord vector = pos2.vector(pos1);
                    Coord antinode = pos2.plus(vector);

                    boolean firstRun = true;
                    while (isOnBoard(antinode, board)) {
                        if (firstRun) {
                            resultPart1.add(antinode);
                            firstRun = false;
                        }
                        resultPart2.add(antinode);
                        antinode = antinode.plus(vector);
                    }
                }
            }
        }
        System.out.println("Part1: " + resultPart1.size());
        System.out.println("Part2: " + resultPart2.size());
    }
    private static boolean isOnBoard(Coord node, char[][] board) {
        return node._1() >= 0 && node._1() < board.length && node._2() >=0 && node._2() < board[0].length;
    }

    private static void printBoard(int maxX, int maxY, char[][] board) {
        IntStream.range(0, maxY)
                .forEach(i -> {
                    String row = IntStream.range(0, maxX)
                            .mapToObj(j -> String.valueOf(board[i][j]))
                            .collect(Collectors.joining());
                    System.out.println(row);
                });
    }

    static class Coord {
        int first;
        int second;

        public Coord(int first, int second) {
            this.first = first;
            this.second = second;
        }

        public int _1() {
            return first;
        }

        public int _2() {
            return second;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coord coord = (Coord) o;
            return first == coord.first && second == coord.second;
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }

        public Coord vector(Coord that) {
            return new Coord(this.first - that.first, this.second - that.second);
        }

        public Coord plus(Coord that) {
            return new Coord(this.first + that.first, this.second + that.second);
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
