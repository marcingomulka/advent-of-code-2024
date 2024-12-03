package com.example.advent.day3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.LongBinaryOperator;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class Main {

    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();

        long sumPart1 = 0L;
        long sumPart2 = 0L;
        boolean enabled = true;

        for (String line : lines) {
            Matcher matcher = Pattern.compile("(do\\(\\))|(don't\\(\\))|(mul\\([0-9]{1,3},[0-9]{1,3}\\))")
                    .matcher(line);
            sumPart1 += matcher.results()
                    .map(MatchResult::group)
                    .map(Main::getProduct)
                    .mapToLong(Long::longValue)
                    .sum();
            matcher.reset();
            for (MatchResult result: matcher.results().toList()) {
                String match = result.group();
                if (match.startsWith("do(")) {
                    enabled  = true;
                } else if (match.startsWith("don")) {
                    enabled = false;
                } else if (match.startsWith("mul") && enabled){
                    sumPart2 += getProduct(match);
                }
            }
        }
        System.out.println("Part1: " + sumPart1);
        System.out.println("Part2: " + sumPart2);
    }

    private static Long getProduct(String match) {
        if (match.startsWith("mul")) {
            String argsStr = match.substring(4, match.length() - 1);
            return Arrays.stream(argsStr.split(","))
                    .mapToLong(Long::parseLong)
                    .reduce(1L, (x, y) -> x * y);
        }
        return 0L;
    }

    private static List<String> readInput() throws IOException {
        List<String> lines;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}
