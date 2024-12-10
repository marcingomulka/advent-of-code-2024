package com.example.advent.day9;

import org.checkerframework.checker.units.qual.A;
import org.xml.sax.helpers.AttributeListImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Main {

    static class File {

        int id;
        int len;
        Optional<File> prev = Optional.empty();
        Optional<File> next = Optional.empty();
        public File(int id, int len) {
            this.id = id;
            this.len = len;
        }
        @Override
        public String toString() {
            return "(" +
                    "" + id +
                    "," + len +
                    ')';
        }

        public Optional<File> getNext() {
            return next;
        }

        public Optional<File> getPrev() {
            return prev;
        }
    }
    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();
        String input = lines.get(0);

        List<Integer> disk = new ArrayList<>();
        File diskPart2 = null;
        File diskPart2Last = null;
        File curr = null;
        int pos = 0;
        for (char digit: input.toCharArray()) {
            int num = Integer.parseInt(String.valueOf(digit));
            int id = pos%2==0 ? pos/2 : -1;
            if (num > 0) {
                File file = new File(id, num);
                if (curr != null) {
                    curr.next = Optional.of(file);
                    file.prev = Optional.of(curr);
                }
                if (diskPart2 == null) {
                    diskPart2 = file;
                }
                curr = file;
            }
            for (int i = 0; i < num; i++) {
                if (pos % 2 == 0) {
                    disk.add(pos/2);
                } else {
                    disk.add(-1);
                }
            }
            pos++;
        }
        diskPart2Last = curr;
        //System.out.println(disk.size());
        //System.out.println(disk.subList(94776, 94796));
        int index = disk.size() -1;
        int start = 0;
        while (index >= 0 && index > start) {
            int value = disk.get(index);
            if (value >= 0) {
                while (start < disk.size() && disk.get(start) >= 0) {
                    start++;
                }
                if (start < disk.size() && disk.get(start) < 0 && index > start) {
                    disk.set(start, value);
                    disk.set(index, -1);
                    start++;
                }
            }
            index--;
        }
        long resultPart1 = 0L;
        for (int i = 0; i < disk.size(); i++) {
            if (disk.get(i) >= 0) {
                resultPart1 += disk.get(i) * i;
            }
        }
        System.out.println("Part1: " + resultPart1);
        System.out.println(disk.contains(-1));
        System.out.println(disk.size());

        //System.out.println(diskPart2);
        File files = diskPart2;
        File empties = diskPart2Last;
        while (files.prev.isPresent() && files.prev.get() != empties.next.orElse(new File(-2,-2))) {
            int prevIndex = files.previousIndex();
            File file = files.previous();
            if (file.id >= 0) {
                while (empties.hasNext() && diskPart2.get(empties.nextIndex()).id >= 0) {
                    empties.next();
                }
                if (empties.hasNext() && diskPart2.get(empties.nextIndex()).id < 0 && prevIndex > empties.nextIndex()) {
                    File empty = empties.next();
                    if (empty.len >= file.len) {
                        empty.len -= file.len;
                        empties.remove();
                        empties.add(file);
                        if (empty.len > 0) {
                            empties.add(empty);
                        }
                    }
                }
            }
        }
        System.out.println(diskPart2);
        long sumPart2 = 0L;
        long offset = 0L;
        for (File file : diskPart2) {
            for (int i = 0; i < file.len; i++) {
                sumPart2 += (offset+i) * file.id;
            }
            offset += file.len;
        }
        System.out.println("Part2: " + sumPart2);
        //System.out.println(disk.subList(disk.size()-20, disk.size()));
        //System.out.println(disk.subList(0, 20));

    }
    private static List<String> readInput() throws IOException {
        List<String> lines;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}
