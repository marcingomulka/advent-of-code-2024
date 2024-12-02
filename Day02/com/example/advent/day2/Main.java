package com.example.advent.day2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class Main {

    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();
        List<List<Long>> reports = new ArrayList<>();
        for (String line: lines) {
            List<Long> report = Arrays.stream(line.split(" +"))
                    .mapToLong(Long::parseLong)
                    .boxed()
                    .toList();
            reports.add(report);
        }
        int okCount = 0;
        int okCountPart2 = 0;
        List<List<Long>> badReports = new ArrayList<>();
        for (List<Long> report: reports) {
            List<Long> diffs = getDiffsList(report);
            if (isSafe(diffs)) {
                okCount++;
                okCountPart2++;
            } else {
                badReports.add(report);
            }
        }
        for (List<Long> report: badReports) {
            for (int i = 0; i < report.size(); i++) {
                List<Long> reportCopy = new ArrayList<>(report);
                reportCopy.remove(i);
                List<Long> diffs = getDiffsList(reportCopy);
                if (isSafe(diffs)) {
                    okCountPart2++;
                    break;
                }
            }
        }
        System.out.println("Part1: " + okCount);
        System.out.println("Part2: " + okCountPart2);
    }

    private static List<Long> getDiffsList(List<Long> report) {
        List<Long> diffs = new ArrayList<>();
        for (int i = 0; i < report.size() - 1; i++) {
            long diff = report.get(i + 1) - report.get(i);
            diffs.add(diff);
        }
        return diffs;
    }

    private static boolean isSafe(List<Long> diffs) {
        return diffs.stream().allMatch(v -> abs(v) <= 3 && abs(v) >= 1)
                && (diffs.stream().allMatch(v -> v > 0) || diffs.stream().allMatch(v -> v < 0));
    }

    private static List<String> readInput() throws IOException {
        List<String> lines;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}
