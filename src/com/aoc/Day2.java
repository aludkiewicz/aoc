package com.aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day2 {

    private static List<String> inputList = new ArrayList<>();

    public static void main(String[] args) {

        FileReader reader = new FileReader();
        List<String> day2Input = reader.read("input_day_2");

        inputList = Arrays.asList(day2Input.get(0).split(","));

        part1();
        part2();

    }

    private static void part2() {
        for(int noun = 0; noun < 99; noun++) {
            for(int verb = 0; verb < 99; verb++) {
                List<Integer> ints = getOriginalInput();
                ints.set(1, noun);
                ints.set(2, verb);
                runProgram(ints);

                if(ints.get(0) == 19690720) {
                    System.out.println("Noun was " + noun);
                    System.out.println("Verb was " + verb);
                    System.out.println("100 * noun + verb = " + (100*noun + verb));
                }

            }
        }
    }

    private static void part1() {
        List<Integer> ints = getOriginalInput();

        ints.set(1, 12);
        ints.set(2, 2);

        runProgram(ints);
        System.out.println("Solution part 1 : " + ints.get(0));
    }


    private static List<Integer> getOriginalInput() {
        return inputList
                .stream()
                .mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toList());
    }


    private static void runProgram(List<Integer> ints) {
        int n = 0;
        while (n < ints.size()) {
            int opCode = ints.get(n++);
            int val1 = ints.get(ints.get(n++));
            int val2 = ints.get(ints.get(n++));
            int outputPos = ints.get(n++);

            if (opCode == 1) {
                ints.set(outputPos, val1 + val2);
            } else if (opCode == 2) {
                ints.set(outputPos, val1 * val2);
            } else if (opCode == 99) {
                break;
            }
        }
    }

}
