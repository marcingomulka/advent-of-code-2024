package com.example.advent.day9;

import javax.swing.text.html.Option;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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

        Optional<File> files = Optional.of(diskPart2Last);
        while (files.isPresent()) {
            File file = files.get();
            boolean moved = false;
            if (file.id >= 0) {
                Optional<File> empties = Optional.of(diskPart2);
                while (empties.isPresent() && !emptyFits(empties.get(), file) && empties.get().next.isPresent()) {
                    if (empties.get() == file) {
                        break;
                    }
                    empties = empties.get().next;
                }
                if (empties.isPresent() && emptyFits(empties.get(), file) && empties.get() != file) {
                    File oldFile = new File(-1, file.len);
                    oldFile.next = file.next;
                    oldFile.prev = file.prev;
                    file.prev.ifPresent(p -> p.next = Optional.of(oldFile));
                    file.next.ifPresent(p -> p.prev = Optional.of(oldFile));

                    File empty = empties.get();

                    empty.len -= file.len;

                    file.prev = empty.prev;
                    empty.prev.ifPresent(p -> p.next = Optional.of(file));
                    if (empty.len > 0) {
                        file.next = Optional.of(empty);
                        empty.prev = Optional.of(file);
                    } else {
                        file.next = empty.next;
                        empty.next.ifPresent(p -> p.prev = Optional.of(file));
                    }
                    files = oldFile.prev;
                    moved = true;
                }
            }
            if (!moved) {
                files = files.get().prev;
            }
            compactEmpties(diskPart2);
        }
        System.out.println();
        long sumPart2 = 0L;
        long offset = 0L;
        Optional<File> file = Optional.of(diskPart2);
        while (file.isPresent()) {
            File current = file.get();
            for (int i = 0; i < current.len; i++) {
                if (current.id >= 0) {
                    sumPart2 += (offset + i) * current.id;
                }
            }
            offset += current.len;
            file = current.next;
        }
        System.out.println("Part2: " + sumPart2);
    }

    private static void compactEmpties(File diskPart2) {
        Optional<File> curr = Optional.of(diskPart2);
        while (curr.isPresent()) {
            File file = curr.get();
            if (file.id == -1 && file.next.isPresent() && file.next.get().id == -1) {
                file.len += file.next.get().len;
                file.next.get().next.ifPresent(p -> p.prev = Optional.of(file));
                file.next = file.next.get().next;

            }
            curr = file.next;
        }
    }

    private static boolean emptyFits(File empty, File file) {
        return empty.id == -1 && empty.len >= file.len;
    }

    private static void printList(File diskPart2) {
        System.out.print(diskPart2 + ", ");
        while (diskPart2.next.isPresent()) {
            diskPart2 = diskPart2.next.get();
            System.out.print(diskPart2 + ", ");
        }
        System.out.println();
    }

    private static List<String> readInput() throws IOException {
        List<String> lines;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}
