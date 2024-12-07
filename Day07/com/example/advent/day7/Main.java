package com.example.advent.day7;

import com.google.common.base.Splitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Main {

    private static final List<String> operators = List.of("+", "*");

    private static final List<String> operatorsPart2 = List.of("+", "*", "||");

    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();

        List<Long> results = new ArrayList<>();
        List<List<Long>> argList = new ArrayList<>();
        for (String line: lines) {
            String [] eqStr = line.split(":");
            long result = Long.parseLong(eqStr[0]);
            results.add(result);
            argList.add(Arrays.stream(eqStr[1].trim().split(" +"))
                    .mapToLong(Long::parseLong)
                    .boxed()
                    .toList());
        }

        long sumPart1 = 0L;
        long sumPart2 = 0L;
        for (int i = 0; i < results.size(); i++) {
            long result = results.get(i);
            List<Long> arg = argList.get(i);
            if (trySolve(result, arg, 1, arg.get(0), operators)) {
                sumPart1 += result;
            }
            if (trySolve(result, arg, 1, arg.get(0), operatorsPart2)) {
                sumPart2 += result;
            }
        }
        System.out.println("Part1: " + sumPart1);
        System.out.println("Part2: " + sumPart2);
    }

    private static boolean trySolve(long result, List<Long> argList, int pos, long currResult, List<String> operators) {
        if (pos == argList.size()) {
            return currResult == result;
        }
        long arg = argList.get(pos);
        for (String operator: operators) {
            long prevCurrResult = currResult;
            currResult = calc(currResult, arg, operator);
            if (trySolve(result, argList, pos+1, currResult, operators)) {
                return true;
            }
            currResult = prevCurrResult;
        }
        return false;
    }

    private static long calc(long currResult, long arg, String operator) {
        switch (operator) {
            case "+":
                return currResult + arg;
            case "*":
                return currResult * arg;
            case "||":
                return Long.parseLong(String.valueOf(currResult) + String.valueOf(arg));
        }
        throw new IllegalArgumentException();
    }

    private static List<String> readInput() throws IOException {
        List<String> lines;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}
