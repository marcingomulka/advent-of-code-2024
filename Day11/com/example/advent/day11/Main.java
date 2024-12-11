package com.example.advent.day11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();
        String line = lines.get(0);

        List<Long> numbers = Arrays.stream(line.split(" +"))
                .map((Long::parseLong))
                .toList();

        long resultPart1 = blinkTimes(numbers, 25).values()
                .stream()
                .mapToLong(Long::longValue)
                .sum();
        System.out.println("Part1: " + resultPart1);

        long resultPart2 = blinkTimes(numbers, 75).values()
                .stream()
                .mapToLong(Long::longValue)
                .sum();
        System.out.println("Part2: " + resultPart2);
    }

    private static Map<Long, Long> blinkTimes(List<Long> numbers, int times) {
        Map<Long, Long> numCounts = numbers.stream().collect(Collectors.toMap(Function.identity(), v -> 1L));
        for (int i = 0; i < times; i++) {
            Map<Long, Long> newCounts = new HashMap<>();
            for (Map.Entry<Long, Long> entry : numCounts.entrySet()) {
                long num = entry.getKey();
                long count = entry.getValue();
                String numStr = String.valueOf(num);
                if (num == 0L) {
                    newCounts.compute(1L, (k, v) -> v != null ? v + count : count);

                } else if (numStr.length() % 2 == 0) {
                    long first = Long.parseLong(numStr.substring(0, numStr.length() / 2));
                    long second = Long.parseLong(numStr.substring(numStr.length() / 2));
                    newCounts.compute(first, (k, v) -> v != null ? v + count : count);
                    newCounts.compute(second, (k, v) -> v != null ? v + count : count);

                } else {
                    newCounts.compute(2024L * num, (k, v) -> v != null ? v + count : count);
                }
            }
            numCounts = newCounts;
        }
        return numCounts;
    }

    private static List<String> readInput() throws IOException {
        List<String> lines;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}
