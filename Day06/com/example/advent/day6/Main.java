package com.example.advent.day6;

import io.vavr.Tuple2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Main {

    private static final Map<Character, Coord> directions = Map.of(
            '^', new Coord(-1, 0),
            '>', new Coord(0, 1),
            'v', new Coord(1, 0),
            '<', new Coord(0, -1)
    );
    private static final Map<Character, Character> trace = Map.of(
            '^', '|',
            '>', '-',
            'v', '|',
            '<', '-'
    );
    private static final Map<Character, Character> nextOf = Map.of(
            '^', '>',
            '>', 'v',
            'v', '<',
            '<', '^'
    );

    private static final Set<Coord> recorded = new LinkedHashSet<>();
    private static boolean recordPath = true;

    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();
        int maxX = lines.get(0).length();
        int maxY = lines.size();
        char[][] board = new char[maxY][maxX];

        Coord guard = null;
        for (int i = 0; i < maxY; i++) {
            String line = lines.get(i);
            for (int j = 0; j < maxX; j++) {
                char c = line.charAt(j);
                board[i][j] = c;
                if (c == '^') {
                    guard = new Coord(i, j);
                }
            }
        }
        char[][] copy = Arrays.stream(board).map(char[]::clone).toArray(char[][]::new);
        char guardChar = '^';
        trip(copy, guard, guardChar);
        System.out.println("Part1 " + recorded.size());
        recordPath = false;

        int part2Count = 0;
        for (Coord element : recorded) {
            copy = Arrays.stream(board).map(char[]::clone).toArray(char[][]::new);
            copy[element.x()][element.y()] = '#';
            if (trip(copy, guard, guardChar)) {
                part2Count++;
            }
        }
        System.out.println("Part2 "+ part2Count);
    }

    private static boolean trip(char[][] board, Coord guard, char guardChar) {
        Coord dir = directions.get(guardChar);
        char dirChar = trace.get(guardChar);
        board[guard.x()][guard.y()] = dirChar;

        Set<Tuple2<Coord, Coord>> turns = new HashSet<>();
        while (true) {
            while(!inFrontOfEnd(board, guard, dir) && !inFrontOfObstacle(board, guard, dir)) {
                guard = step(board, guard, dir, dirChar);
            }
            if(inFrontOfEnd(board, guard, dir)) {
                board[guard.x()][guard.y()] = dirChar;
                if (recordPath) {
                    recorded.add(guard);
                }
                return false;

            } else if (inFrontOfObstacle(board, guard, dir)) {
                if (isLooped(guard, dir, turns)) {
                    return true;
                }
                board[guard.x()][guard.y()] = '+';
                turns.add(new Tuple2<>(guard.plus(dir), dir));
                guardChar = nextOf.get(guardChar);
                dir = directions.get(guardChar);
                dirChar = trace.get(guardChar);
            }
        }
    }

    private static Coord step(char[][] board, Coord guard, Coord dir, char dirChar) {
        guard = guard.plus(dir);
        board[guard.x()][guard.y()] = dirChar;
        if (recordPath) {
            recorded.add(guard);
        }
        return guard;
    }
    private static boolean isLooped(Coord guard, Coord dir, Set<Tuple2<Coord, Coord>> turns) {
        return turns.contains(new Tuple2<>(guard.plus(dir), dir));
    }
    private static boolean inFrontOfObstacle(char[][] board, Coord guard, Coord dir) {
        guard = guard.plus(dir);
        return board[guard.x()][guard.y()] == '#';
    }

    private static boolean inFrontOfEnd(char[][] board, Coord guard, Coord dir) {
        guard = guard.plus(dir);
        return guard.x() < 0
                || guard.x() >= board.length
                || guard.y() < 0
                || guard.y() >= board[0].length;
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

   private static List<String> readInput() throws IOException {
        List<String> lines;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }

    static class Coord {
        int first;
        int second;

        public Coord(int first, int second) {
            this.first = first;
            this.second = second;
        }

        public int x() {
            return first;
        }

        public int y() {
            return second;
        }

        public Coord plus(Coord c) {
            return new Coord(first + c.first, second + c.second);
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
    }
}
