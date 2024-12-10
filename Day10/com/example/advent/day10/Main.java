package com.example.advent.day10;

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
        int [][] board = new int[maxY][maxX];

        List<Coord> starts = new ArrayList<>();
        for (int i = 0; i < maxY; i++) {
            String line = lines.get(i);
            for (int j = 0; j < maxX; j++) {
                int num = Integer.parseInt(String.valueOf(line.charAt(j)));
                board[i][j] = num;
                if (num == 0) {
                    starts.add(new Coord(i,j));
                }
            }
        }
        long sumPart1 = 0L;
        long sumPart2 = 0L;
        for (Coord start: starts) {
            Map<Coord, Integer> ratings = findTrailheads(start, board);
            sumPart1 += ratings.size();
            sumPart2 += ratings.values().stream().mapToLong(Long::valueOf).sum();
        }
        System.out.println("Part1: " + sumPart1);
        System.out.println("Part2: " + sumPart2);
    }

    private static Map<Coord, Integer> findTrailheads(Coord start, int[][] board) {
        Queue<Coord> queue = new LinkedList<>();
        queue.add(start);
        Map<Coord, Integer> peeks = new HashMap<>();
        while (!queue.isEmpty()) {
            Coord curr = queue.poll();
            if (board[curr._1()][curr._2()] == 9) {
                int rating = peeks.getOrDefault(curr, 0);
                peeks.put(curr, rating + 1);
            } else {
                List<Coord> neighbors = getNeighbors(curr, board);
                queue.addAll(neighbors);
            }
        }
        return peeks;
    }
    private static List<Coord> getNeighbors(Coord curr, int[][] board) {
        int num = board[curr._1()][curr._2()];
        Coord left = new Coord(curr._1(), curr._2() - 1);
        Coord right = new Coord(curr._1(), curr._2() + 1);
        Coord up = new Coord(curr._1() - 1, curr._2());
        Coord down = new Coord(curr._1() + 1, curr._2());

        List<Coord> results = new ArrayList<>();
        for (Coord c : new Coord[]{left, right, up, down}) {
            if (c._1() >=0 && c._1() < board.length && c._2() >=0 && c._2() < board[0].length) {
                if (board[c._1()][c._2()] == num + 1) {
                    results.add(c);
                }
            }
        }
        return results;
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
    }

    private static List<String> readInput() throws IOException {
        List<String> lines;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}
