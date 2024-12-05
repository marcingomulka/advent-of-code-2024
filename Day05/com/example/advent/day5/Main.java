package com.example.advent.day5;

import com.google.common.collect.HashMultimap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class Main {

    private static HashMultimap<Integer, Integer> ordering = HashMultimap.create();

    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();
        List<List<Integer>> pages = new ArrayList<>();
        for (String line : lines) {
            if (line.contains("|")) {
                String[] chunks = line.split("\\|");
                int first = Integer.parseInt(chunks[0]);
                int second = Integer.parseInt(chunks[1]);
                ordering.put(first, second);
            } else if (line.contains(",")) {
                pages.add(Arrays.stream(line.split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList()));
            }
        }
        long sumPart1 = 0L;
        long sumPart2 = 0L;
        for (List<Integer> pageSet : pages) {
            List<Integer> ordered = new ArrayList<>(pageSet);
            Collections.sort(ordered, Main::compare);
            if (ordered.equals(pageSet)) {
                sumPart1 += pageSet.get(pageSet.size()/2);
            } else {
                sumPart2 += ordered.get(ordered.size()/2);
            }
        }
        System.out.println("Part1 " + sumPart1);
        System.out.println("Part2 " + sumPart2);
    }

    private static int compare(Integer first, Integer second) {
        return ordering.get(first).contains(second) ? -1 : 1;
    }

   private static List<String> readInput() throws IOException {
        List<String> lines;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}
