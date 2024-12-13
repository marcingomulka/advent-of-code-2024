package com.example.advent.day13;

import io.vavr.Tuple2;
import io.vavr.Tuple3;
import org.checkerframework.checker.units.qual.C;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();
        Iterator<String> iter = lines.iterator();
        List<Tuple3<Coord, Coord, Coord>> games = new ArrayList<>();

        while(iter.hasNext()) {
            String lineA = iter.next();
            String [] chunkStr = lineA.replace("Button A: ", "").split(",");
            int buttunAx = Integer.parseInt(chunkStr[0].trim().replace("X+", ""));
            int buttunAy = Integer.parseInt(chunkStr[1].trim().replace("Y+", ""));

            String lineB = iter.next();
            String [] chunkStrB = lineB.replace("Button B: ", "").split(",");
            int buttunBx = Integer.parseInt(chunkStrB[0].trim().replace("X+", ""));
            int buttunBy = Integer.parseInt(chunkStrB[1].trim().replace("Y+", ""));

            String prizeLine = iter.next();
            String [] chunkStrP = prizeLine.replace("Prize: ", "").split(",");
            int targetX = Integer.parseInt(chunkStrP[0].trim().replace("X=", ""));
            int targetY = Integer.parseInt(chunkStrP[1].trim().replace("Y=", ""));

            games.add(new Tuple3<>(new Coord(buttunAx, buttunAy), new Coord(buttunBx, buttunBy), new Coord(targetX, targetY)));

            if (iter.hasNext()) {
                iter.next();
            }
        }
        int tokensSum = 0;
        for (Tuple3<Coord, Coord, Coord> game : games) {
            Map<Coord, Long> pruned = new HashMap<>();
            long tokens = playWithMinTokens(game._1, game._2, game._3, new Coord(0, 0), 0, Long.MAX_VALUE, pruned);
            if (tokens < Integer.MAX_VALUE) {
                tokensSum += tokens;
            }
        }
        System.out.println("Part 1: " + tokensSum);

        List<Tuple3<Coord, Coord, Coord>> gamesPart2 = games.stream()
                .map(g -> new Tuple3<>(g._1, g._2, new Coord(g._3.first + 10000000000000L, g._3.second + 10000000000000L)))
                .toList();
        //TODO
        long tokensSumPart2 = 0L;
        System.out.println("Part 2: " + tokensSumPart2);

    }

    private static long playWithMinTokens(Coord buttonA, Coord buttonB, Coord target, Coord pos, long cost, long minCost, Map<Coord, Long> pruned) {
        if (pos.equals(target)) {
            return cost;
        } else if (pruned.containsKey(pos) && pruned.get(pos) >= minCost) {
            return Long.MAX_VALUE;
        } else if (pos._1() > target._1() || pos._2() > target._2()) {
            return Long.MAX_VALUE;
        } else if (cost >= minCost) {
            pruned.put(pos, cost);
            return Long.MAX_VALUE;
        } else {
            long currMin = minCost;
            List<Coord> options = List.of(buttonA, buttonB);
            for (Coord c : options) {
                int unitCost = c.equals(buttonA) ? 3 : 1;
                long calcCost = playWithMinTokens(buttonA, buttonB, target, pos.plus(c), cost + unitCost, currMin, pruned);
                if (calcCost < currMin) {
                    currMin = calcCost;
                } else {
                    pruned.put(pos, calcCost);
                }
            }
            return currMin;
        }
    }

    static class Coord {
        long first;
        long second;

        public Coord(long first, long second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public String toString() {
            return "(" + first +
                    "," + second +
                    ')';
        }

        public long _1() {
            return first;
        }

        public long _2() {
            return second;
        }

        public Coord plus(Coord other) {
            return new Coord(this.first + other.first, this.second + other.second);
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
