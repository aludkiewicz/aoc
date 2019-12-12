package com.aoc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.*;
import java.util.List;

public class Day11 {

    private static List<String> program = new ArrayList<>();
    private static List<BigInteger> arguments = new ArrayList<>();
    private static Map<Coordinate, MetaInfo> coordinates = new HashMap<>();
    private static Coordinate currentCoord = new Coordinate(0, 0);
    private static Direction currentDir = Direction.NORTH;

    private static final String PATH = "/home/alud/code/aoc/src/resources/day11_img.png";

    private static final int MEMORY_SIZE_FACTOR = 1000;

    private static int relBase = 0;
    private static boolean haltReached = false;

    public static void main(String[] args) throws IOException {
        System.out.println("Please enter arguments to intcode computer: (Comma separated)");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String[] inputs = br.readLine().split(",");

        for(String arg : inputs) {
            arguments.add(new BigInteger(arg));
        }
        FileReader reader = new FileReader();
        List<String> day11Input = reader.read("input_day_11");
        program = new ArrayList<>(Arrays.asList(day11Input.get(0).split(",")));
        buildMemory(program);

        while (!haltReached) {
            runProgram(program);
        }

        System.out.println("Number of panels painted once or more: " + coordinates.keySet().size());

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Coordinate coordinate : coordinates.keySet()) {
            int x = coordinate.x;
            int y = coordinate.y;

            if(x < minX) {
                minX = x;
            }

            if(x > maxX) {
                maxX = x;
            }

            if(y < minY) {
                minY = y;
            }

            if(y > maxY) {
                maxY = y;
            }
        }

        int height = maxY - minY + 1; // minY is negative
        int width = maxX - minX + 1;  // minX too

        Integer[][] reconstructedImg = new Integer[width][height];

        fill(reconstructedImg, width, height);

        for (Map.Entry<Coordinate, MetaInfo> entry : coordinates.entrySet()) {

            int x = entry.getKey().x + Math.abs(minX); // Adjust so minX = 0;
            int y = entry.getKey().y + Math.abs(minY); // Same for minY

            reconstructedImg[x][y] = entry.getValue().color;
        }

        makeImg(reconstructedImg, width, height);
    }

    private static void makeImg(Integer[][] reconstructedImg, int width, int height) throws IOException {
        BufferedImage img= new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB);
        for(int x=0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                int pixelVal = reconstructedImg[x][y] == 0 ? 0 : 128;
                Color newColor = new Color(pixelVal,pixelVal,pixelVal);
                img.setRGB(x, y, newColor.getRGB());
            }
        }
        File output = new File(PATH);
        System.out.println("Image path is " + PATH);
        ImageIO.write(img, "png", output);
    }

    private static void fill(Integer[][] ints, int width, int height) {
        for(int x=0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                ints[x][y] = 0;
            }
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
        int arg = 0;
        int nofOutputs = 0;
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

                BigInteger val = arguments.get(arg++); // Not a parameter - Is input
                input.set(pos, String.valueOf(val));

            } else if (opCode == 4) {

                BigInteger val1 = getValue(modeParam1, n++, input);

                if(nofOutputs == 0) {

                    MetaInfo metaInfo = coordinates.get(currentCoord);

                    if(metaInfo == null) {
                        metaInfo = new MetaInfo(1, val1.intValue());
                    } else {
                        metaInfo.setColor(val1.intValue());
                        metaInfo.setTimesVisited(metaInfo.getTimesVisited() + 1 );
                    }
                    coordinates.put(currentCoord, metaInfo);
                    nofOutputs++;
                } else {
                    currentDir = Direction.transitionFrom(currentDir, val1.intValue());
                    moveRobot(currentDir);
                    refreshArguments();
                    arg = 0;
                    nofOutputs = 0;
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

    private static void refreshArguments() {
        arguments.clear();
        MetaInfo metaInfo = coordinates.get(currentCoord);
        if(metaInfo == null) {
            arguments.add(BigInteger.ZERO);
        } else {
            arguments.add(BigInteger.valueOf(metaInfo.color));
        }
    }

    private static void moveRobot(Direction currentDir) {

        int x = currentCoord.x;
        int y = currentCoord.y;

        switch (currentDir) {

            case NORTH:
                currentCoord = new Coordinate(x, y - 1);
                return;
            case WEST:
                currentCoord = new Coordinate(x - 1, y);
                return;
            case SOUTH:
                currentCoord = new Coordinate(x, y + 1);
                return;
            case EAST:
                currentCoord = new Coordinate(x + 1, y);
                return;
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

    static class MetaInfo {

        private int timesVisited;
        private int color;

        public MetaInfo(int timesVisited, int color) {
            this.timesVisited = timesVisited;
            this.color = color;
        }

        public int getTimesVisited() {
            return timesVisited;
        }

        public void setTimesVisited(int timesVisited) {
            this.timesVisited = timesVisited;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MetaInfo)) return false;
            MetaInfo meta = (MetaInfo) o;
            return getTimesVisited() == meta.getTimesVisited() &&
                    getColor() == meta.getColor();
        }

        @Override
        public int hashCode() {
            return Objects.hash(getTimesVisited(), getColor());
        }
    }

}
