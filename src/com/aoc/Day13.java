package com.aoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day13 {

    private static List<String> inputList = new ArrayList<>();

    private static List<BigInteger> arguments = new ArrayList<>();
    private static List<BigInteger> outputs = new ArrayList<>();
    private static final int MEMORY_SIZE_FACTOR = 1000;

    private static int relBase = 0;
    private static final int WIDTH = 10_000;
    private static final int HEIGHT = 10_000;

    private static int[][] field = new int[WIDTH][HEIGHT];

    private static boolean part1 = false;

    private static int ballX = 0;
    private static int ballY = 0;

    private static int paddleX = 0;
    private static int paddleY = 0;

    private static boolean isPart1 = false;
    private static boolean pendingInput = false;
    private static boolean haltReached = false;
    private static int score = 0;
    private static int nofBlocksLeft = Integer.MAX_VALUE;

    public static void main(String[] args) {
        FileReader reader = new FileReader();
        List<String> day13Input = reader.read("input_day_13");
        inputList = new ArrayList<>(Arrays.asList(day13Input.get(0).split(",")));
        buildMemory(inputList);

        if(isPart1) {
            runProgram(inputList);
            int nofBlocks = countBlocks();
            System.out.println("Number of blocks is: " + nofBlocks);
        } else {
            inputList.set(0, "2");
            haltReached = false;
            while (!haltReached) {
                runProgram(inputList);
            }
            System.out.println("Score after blocks are gone is: " + score);
        }
    }

    private static void buildMemory(List<String> inputList) {

        for(int i = 0; i < inputList.size() * MEMORY_SIZE_FACTOR; i++) {

            if(i + inputList.size() >= Integer.MAX_VALUE) {
                return;
            }

            inputList.add("0");
        }
    }

    private static void runProgram(List<String> input) {
        int n = 0;
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

                countBlocks();

                if(nofBlocksLeft == 0) {
                    return;
                }

                int pos = Integer.parseInt(input.get(n++));

                if(modeParam1 == 2) {
                    pos += relBase;
                }

                BigInteger val = BigInteger.ZERO;
                if(paddleX < ballX) {
                    val = BigInteger.valueOf(1);
                } else if (paddleX > ballX) {
                    val = BigInteger.valueOf(-1);
                }

                input.set(pos, String.valueOf(val));

            } else if (opCode == 4) {

                BigInteger val1 = getValue(modeParam1, n++, input);
                outputs.add(val1);

                if(outputs.size() == 3) {
                    drawBoard();
                    outputs.clear();
                }

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
                haltReached = true;
                break;
            }
        }
    }

    private static int countBlocks() {
        int nofBlocks = 0;
        for(int i = 0; i < WIDTH; i++) {
            for(int j = 0; j < HEIGHT; j++) {
                if(field[i][j] == 2) {
                    nofBlocks++;
                }
            }
        }
        nofBlocksLeft = nofBlocks;
        return 0;
    }

    private static int drawBoard() {
        int i = 0;
        int nofBlocks = 0;
        while(i < outputs.size()) {
            int x = outputs.get(i++).intValue();
            int y = outputs.get(i++).intValue();
            int tileId = outputs.get(i++).intValue();


            if(x == -1 && y == 0) {
                score = tileId;
                continue;
            }

            field[x][y] = tileId;
            if(tileId == 2) {
                nofBlocks++;
            }

            if(tileId == 4) {
                ballX = x;
                ballY = y;
            }

            if(tileId == 3) {
                paddleX = x;
                paddleY = y;
            }

        }

        return nofBlocks;
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
