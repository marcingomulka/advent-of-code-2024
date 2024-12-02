package com.example.advent.day1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Math.abs;
import static java.lang.Math.toIntExact;

public class Main {

    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        for (String line: lines) {
            int num1 = Integer.parseInt(line.split(" +")[0]);
            int num2 = Integer.parseInt(line.split(" +")[1]);
            list1.add(num1);
            list2.add(num2);
        }
        Collections.sort(list1);
        Collections.sort(list2);

        int distance = 0;
        for(int i =0; i < list1.size(); i++) {
            distance += abs(list1.get(i) - list2.get(i));
        }
        System.out.println("Part1: " + distance);

        Map<Integer, Long> counts = list2.stream().collect(
                Collectors.groupingBy(Function.identity(), Collectors.counting()));
        long similarity = 0L;
        for (Integer num : list1) {
            long count = counts.getOrDefault(num, 0L);
            similarity += num.longValue() * count;
        }
        System.out.println("Part2 " + similarity);
    }

    private static List<String> readInput() throws IOException {
        List<String> lines;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}
