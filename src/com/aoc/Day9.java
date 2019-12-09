package com.aoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day9 {

    private static List<String> inputList = new ArrayList<>();

    private static List<BigInteger> arguments = new ArrayList<>();

    private static final int MEMORY_SIZE_FACTOR = 1000;

    private static int relBase = 0;

    public static void main(String[] args) throws IOException {
        System.out.println("Please enter arguments to intcode computer: (Comma separated)");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String[] inputs = br.readLine().split(",");

        for(String arg : inputs) {
            arguments.add(new BigInteger(arg));
        }

        FileReader reader = new FileReader();
        List<String> day9Input = reader.read("input_day_9");
        inputList = new ArrayList<>(Arrays.asList(day9Input.get(0).split(",")));
        buildMemory(inputList);

        runProgram(inputList, arguments);
    }

    private static void buildMemory(List<String> inputList) {

        for(int i = 0; i < inputList.size() * MEMORY_SIZE_FACTOR; i++) {

            if(i + inputList.size() >= Integer.MAX_VALUE) {
                return;
            }

            inputList.add("0");
        }
    }

    private static void runProgram(List<String> input, List<BigInteger> arguments) {
        int n = 0;
        int arg = 0;
        while (n < input.size()) {
            String inst = input.get(n++);

            int opCode = inst.length() > 1 ? Integer.parseInt(inst.substring(inst.length() - 2)) : Integer.parseInt(inst);

            int modeParam1 = inst.length() > 2 ? Character.getNumericValue(inst.charAt(inst.length() - 3)) : 0;
            int modeParam2 = inst.length() > 3 ? Character.getNumericValue(inst.charAt(inst.length() - 4)) : 0;
            int modeParam3 = inst.length() > 4 ? Character.getNumericValue(inst.charAt(inst.length() - 5)) : 0;

            if (opCode == 1) {

                BigInteger val1 = getValue(modeParam1, n++, input);
                BigInteger val2 = getValue(modeParam2, n++, input);
                int pos = Integer.parseInt(input.get(n++));

                if(modeParam3 == 2) {
                    pos += relBase;
                }

                input.set(pos, String.valueOf(val1.add(val2)));

            } else if (opCode == 2) {

                BigInteger val1 = getValue(modeParam1, n++, input);
                BigInteger val2 = getValue(modeParam2, n++, input);
                int pos = Integer.parseInt(input.get(n++));

                if(modeParam3 == 2) {
                    pos += relBase;
                }

                input.set(pos, String.valueOf(val1.multiply(val2)));
            } else if (opCode == 3) {

                int pos = Integer.parseInt(input.get(n++));

                if(modeParam1 == 2) {
                    pos += relBase;
                }

                BigInteger val = Day9.arguments.get(arg++); // Not a parameter - Is input
                input.set(pos, String.valueOf(val));

            } else if (opCode == 4) {

                BigInteger val1 = getValue(modeParam1, n++, input);
                System.out.println(val1);

            } else if (opCode == 5) {

                BigInteger val1 = getValue(modeParam1, n++, input);
                BigInteger val2 = getValue(modeParam2, n++, input);

                n = !val1.equals(BigInteger.ZERO) ? val2.intValue() : n;

            } else if (opCode == 6) {

                BigInteger val1 = getValue(modeParam1, n++, input);
                BigInteger val2 = getValue(modeParam2, n++, input);

                n = val1.equals(BigInteger.ZERO) ? val2.intValue() : n;

            } else if (opCode == 7) {

                BigInteger val1 = getValue(modeParam1, n++, input);
                BigInteger val2 = getValue(modeParam2, n++, input);
                int pos = Integer.parseInt(input.get(n++));

                if(modeParam3 == 2) {
                    pos += relBase;
                }

                if(val1.compareTo(val2) < 0) {
                    input.set(pos, String.valueOf(1));
                } else {
                    input.set(pos, String.valueOf(0));
                }

            } else if (opCode == 8) {

                BigInteger val1 = getValue(modeParam1, n++, input);
                BigInteger val2 = getValue(modeParam2, n++, input);
                int pos = Integer.parseInt(input.get(n++));

                if(modeParam3 == 2) {
                    pos += relBase;
                }

                if(val1.equals(val2)) {
                    input.set(pos, String.valueOf(1));
                } else {
                    input.set(pos, String.valueOf(0));
                }
            } else if (opCode == 9) {
                BigInteger val1 = getValue(modeParam1, n++, input);

                relBase += val1.intValue();
            } else if (opCode == 99) {
                break;
            }
        }
    }


    public static BigInteger getValue(int mode, int n, List<String> input) {

        if(mode == 0) {
            return new BigInteger(input.get(Integer.parseInt(input.get(n))));
        } else if (mode == 1) {
            return new BigInteger(input.get(n));
        } else if (mode == 2) {
            return new BigInteger(input.get(Integer.parseInt(input.get(n)) + relBase));
        }

        throw new RuntimeException("Illegal mode!");
    }
}
