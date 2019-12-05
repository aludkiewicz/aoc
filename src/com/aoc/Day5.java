package com.aoc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day5 {

    private static List<String> inputList = new ArrayList<>();

    private static int inputId;

    public static void main(String[] args) throws Exception {
        System.out.println("Please enter ID to use (pt.1 ID = 1, pt.2 = 5)");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        inputId = Integer.parseInt(br.readLine());

        FileReader reader = new FileReader();

        List<String> day5Input = reader.read("input_day_5");

        inputList = Arrays.asList(day5Input.get(0).split(","));

        runProgram(inputList);

    }

    private static void runProgram(List<String> input) {
        int n = 0;
        while (n < input.size()) {

            String inst = input.get(n++);

            int opCode = inst.length() > 1 ? Integer.parseInt(inst.substring(inst.length() - 2)) : Integer.parseInt(inst);

            int modeParam1 = inst.length() > 2 ? Character.getNumericValue(inst.charAt(inst.length() - 3)) : 0;
            int modeParam2 = inst.length() > 3 ? Character.getNumericValue(inst.charAt(inst.length() - 4)) : 0;

            if (opCode == 1) {

                int val1 = getValue(modeParam1, n++, input);
                int val2 = getValue(modeParam2, n++, input);
                int pos = Integer.parseInt(input.get(n++));

                input.set(pos, String.valueOf(val1 + val2));

            } else if (opCode == 2) {

                int val1 = getValue(modeParam1, n++, input);
                int val2 = getValue(modeParam2, n++, input);
                int pos = Integer.parseInt(input.get(n++));

                input.set(pos, String.valueOf(val1 * val2));
            } else if (opCode == 3) {

                int pos = Integer.parseInt(input.get(n++));
                int val = inputId; // Not a parameter - Is input
                input.set(pos, String.valueOf(val));

            } else if (opCode == 4) {

                int val1 = getValue(modeParam1, n++, input);
                System.out.println(val1);

            } else if (opCode == 5) {

                int val1 = getValue(modeParam1, n++, input);
                int val2 = getValue(modeParam2, n++, input);

                n = val1 != 0 ? val2 : n;

            } else if (opCode == 6) {

                int val1 = getValue(modeParam1, n++, input);
                int val2 = getValue(modeParam2, n++, input);

                n = val1 == 0 ? val2 : n;

            } else if (opCode == 7) {

                int val1 = getValue(modeParam1, n++, input);
                int val2 = getValue(modeParam2, n++, input);
                int pos = Integer.parseInt(input.get(n++));

                if(val1 < val2) {
                    input.set(pos, String.valueOf(1));
                } else {
                    input.set(pos, String.valueOf(0));
                }

            } else if (opCode == 8) {

                int val1 = getValue(modeParam1, n++, input);
                int val2 = getValue(modeParam2, n++, input);
                int pos = Integer.parseInt(input.get(n++));

                if(val1 == val2) {
                    input.set(pos, String.valueOf(1));
                } else {
                    input.set(pos, String.valueOf(0));
                }
            } else if (opCode == 99) {
                break;
            }
        }
    }


    public static int getValue(int mode, int n, List<String> input) {

        if(mode == 0) {
            return Integer.parseInt(input.get(Integer.parseInt(input.get(n))));
        } else if (mode == 1) {
            return Integer.parseInt(input.get(n));
        }

        throw new RuntimeException("Illegal mode!");
    }

}
