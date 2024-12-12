package com.example.advent.day12;

import com.google.common.collect.Sets;
import io.vavr.Tuple2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();
        int maxY = lines.size();
        int maxX = lines.get(0).length();
        char [][] board = new char[maxY][maxX];

        for (int i = 0; i < maxY; i++) {
            String line = lines.get(i);
            for (int j = 0; j < maxX; j++) {
                board[i][j] = line.charAt(j);
            }
        }
        Set<Coord> visited = new HashSet<>();
        long sumPart1 = 0L;
        long sumPart2 = 0L;
        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                int area = 0;
                int perimeter = 0;
                Coord start = new Coord(i,j);
                Set<Tuple2<Coord, Coord>> boundaries = new HashSet<>();
                Queue<Coord> queue = new LinkedList<>();

                if (!visited.contains(start)) {
                    queue.add(start);
                }
                while (!queue.isEmpty()) {
                    Coord current = queue.poll();
                    if (visited.contains(current)) {
                        continue;
                    }
                    visited.add(current);
                    Set<Coord> neighbors = getNeighbors(current, board);
                    area++;
                    perimeter += 4 - neighbors.size();
                    Sets.difference(getAdjacent(current), neighbors).stream()
                            .map(f -> new Tuple2<>(current, f))
                            .forEach(boundaries::add);

                    neighbors.stream()
                            .filter(n -> !visited.contains(n))
                            .forEach(queue::add);
                }
                int fenceClustersLength = countClusterLengths(boundaries);
                int sides = perimeter - fenceClustersLength;
                sumPart1 += (long) area * perimeter;
                sumPart2 += (long) area * sides;
            }
        }
        System.out.println("Part1 " + sumPart1);
        System.out.println("Part2 " + sumPart2);
    }

    private static int countClusterLengths(Collection<Tuple2<Coord, Coord>> perimeterPairs) {
        int result = 0;
        for (Tuple2<Coord, Coord> current: perimeterPairs) {
            for (Tuple2<Coord, Coord> next: perimeterPairs) {
                if (current.equals(next)) {
                    continue;
                }
                if (getAdjacent(current._1).contains(next._1)
                        && getAdjacent(current._2).contains(next._2)) {
                    result += 1;
                }
            }
        }
        return result/2;
    }

    private static Set<Coord> getNeighbors(Coord current, char[][] board) {
        char value = board[current._1()][current._2()];
        Set<Coord> results = new HashSet<>();
        for (Coord c : getAdjacent(current)) {
            if (c._1() >=0 && c._1() < board.length && c._2() >=0 && c._2() < board[0].length) {
                if (board[c._1()][c._2()] == value) {
                    results.add(c);
                }
            }
        }
        return results;
    }

    private static Set<Coord> getAdjacent(Coord curr) {
        Coord left = new Coord(curr._1(), curr._2() - 1);
        Coord right = new Coord(curr._1(), curr._2() + 1);
        Coord up = new Coord(curr._1() - 1, curr._2());
        Coord down = new Coord(curr._1() + 1, curr._2());
        return Arrays.stream(new Coord[]{left, right, up, down})
                .collect(Collectors.toSet());
    }

    static class Coord {
        int first;
        int second;

        public Coord(int first, int second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public String toString() {
            return "(" + first +
                    "," + second +
                    ')';
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
    }

    private static List<String> readInput() throws IOException {
        List<String> lines;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}
