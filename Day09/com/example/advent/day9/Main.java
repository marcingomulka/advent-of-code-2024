package com.example.advent.day9;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Main {

    static class Block {
        int id;
        int len;
        Optional<Block> prev = Optional.empty();
        Optional<Block> next = Optional.empty();
        public Block(int id, int len) {
            this.id = id;
            this.len = len;
        }
        public void replaceWith(Block newBlock) {
            newBlock.next = this.next;
            newBlock.prev = this.prev;
            this.prev.ifPresent(p -> p.next = Optional.of(newBlock));
            this.next.ifPresent(p -> p.prev = Optional.of(newBlock));
        }

        public void insertBefore(Block newBlock) {
            newBlock.prev = this.prev;
            this.prev.ifPresent(p -> p.next = Optional.of(newBlock));
            newBlock.next = Optional.of(this);
            this.prev = Optional.of(newBlock);
        }

        public void removeNext() {
            if (this.next.isPresent()) {
                this.next.get().next.ifPresent(p -> p.prev = Optional.of(this));
                this.next = this.next.get().next;
            }
        }

        @Override
        public String toString() {
            return "(" +
                    "" + id +
                    "," + len +
                    ')';
        }
    }
    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();
        String input = lines.get(0);

        List<Integer> diskPart1 = new ArrayList<>();
        Block diskPart2 = null;
        Block diskPart2Last;
        Block pointer = null;
        int pos = 0;
        for (char digit: input.toCharArray()) {
            int num = Integer.parseInt(String.valueOf(digit));
            int id = pos % 2 == 0 ? pos / 2 : -1;
            Block block = new Block(id, num);
            if (pointer != null) {
                pointer.next = Optional.of(block);
                block.prev = Optional.of(pointer);
            } else {
                diskPart2 = block;
            }
            pointer = block;
            IntStream.range(0, num).forEach(i -> diskPart1.add(id));
            pos++;
        }
        diskPart2Last = pointer;

        defragmentPart1(diskPart1);
        long resultPart1 = IntStream.range(0, diskPart1.size())
                .filter(i -> diskPart1.get(i) >= 0)
                .mapToLong(i -> diskPart1.get(i) * i)
                .sum();
        System.out.println("Part1: " + resultPart1);

        defragmentPart2(diskPart2, diskPart2Last);
        long sumPart2 = 0L;
        long offset = 0L;
        Optional<Block> filePointer = Optional.of(diskPart2);
        while (filePointer.isPresent()) {
            long offsetVar = offset;
            Block current = filePointer.get();
            if (current.id >= 0) {
                sumPart2 += IntStream.range(0, current.len)
                        .mapToLong(i -> current.id * (offsetVar + i))
                        .sum();
            }
            offset += current.len;
            filePointer = current.next;
        }
        System.out.println("Part2: " + sumPart2);
    }

    private static void defragmentPart1(List<Integer> disk) {
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
    }

    private static void defragmentPart2(Block begin, Block end) {
        Optional<Block> filesPointer = Optional.of(end);
        while (filesPointer.isPresent()) {
            Block block = filesPointer.get();
            if (block.id >= 0) {
                Optional<Block> emptyPointer = Optional.of(begin);
                while (emptyPointer.isPresent() && !fitsToEmpty(emptyPointer.get(), block) && emptyPointer.get().next.isPresent()) {
                    if (emptyPointer.get() == block) {
                        //if file to defragment has passed last empty space
                        break;
                    }
                    emptyPointer = emptyPointer.get().next;
                }
                if (emptyPointer.isPresent() && fitsToEmpty(emptyPointer.get(), block) && emptyPointer.get() != block) {
                    Block oldBlock = new Block(-1, block.len);
                    block.replaceWith(oldBlock);

                    Block empty = emptyPointer.get();
                    empty.len -= block.len;
                    if (empty.len > 0) {
                        empty.insertBefore(block);
                    } else {
                        empty.replaceWith(block);
                    }
                    compactEmptyBlocks(block);
                    block = oldBlock;
                }
            }
            filesPointer = block.prev;
        }
    }


    private static boolean fitsToEmpty(Block empty, Block block) {
        return empty.id == -1 && empty.len >= block.len;
    }

    private static void compactEmptyBlocks(Block disk) {
        Optional<Block> curr = Optional.of(disk);
        while (curr.isPresent()) {
            Block block = curr.get();
            if (block.id == -1 && block.next.isPresent() && block.next.get().id == -1) {
                block.len += block.next.get().len;
                block.removeNext();
            }
            curr = block.next;
        }
    }
    private static void printList(Block disk) {
        System.out.print(disk + ", ");
        while (disk.next.isPresent()) {
            disk = disk.next.get();
            System.out.print(disk + ", ");
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
