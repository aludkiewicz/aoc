package com.aoc;

import java.util.*;
import java.util.stream.Collectors;

public class Day3 {

    public static void main(String[] args) {

        FileReader reader = new FileReader();
        List<String> day3Input = reader.read("input_day_3");

        Set<Coordinate> wireOneCoordinates = new HashSet<>();
        Set<Coordinate> wireTwoCoordinates = new HashSet<>();

        String wireOnePathString = day3Input.get(0);
        String wireTwoPathString = day3Input.get(1);

        String[] wireOnePath = wireOnePathString.split(",");
        String[] wireTwoPath = wireTwoPathString.split(",");

        traceAndFillMatrix(wireOnePath, wireOneCoordinates);
        traceAndFillMatrix(wireTwoPath, wireTwoCoordinates);

        wireOneCoordinates.retainAll(wireTwoCoordinates); // WireOneCoordinates now only has elements from both sets, i.e. cross-over points

        int distance = Integer.MAX_VALUE;
        for(Coordinate coord : wireOneCoordinates) {
            if(Math.abs(coord.getX()) + Math.abs(coord.getY()) < distance) {
                distance = Math.abs(coord.getX()) + Math.abs(coord.getY()); // Manhattan distance (x,y) -> origin = |x| + |y|
            }
        }


        int stepSum = Integer.MAX_VALUE;
        for (Coordinate coordinate : wireOneCoordinates) {
            for(Coordinate coordinate2 : wireTwoCoordinates) {
                if(coordinate2.equals(coordinate)) {
                    if(coordinate.getNumberOfStepsTaken() + coordinate2.getNumberOfStepsTaken() < stepSum) {
                        stepSum = coordinate.getNumberOfStepsTaken() + coordinate2.getNumberOfStepsTaken();
                    }
                }
            }
        }

        System.out.println("Part 1 answer: " + distance);
        System.out.println("Part 2 answer: " + stepSum);

    }

    private static void traceAndFillMatrix(String[] wirePath, Set<Coordinate> wireCoordinates) {

        int x = 0;
        int y = 0;
        int numberOfStepsTaken = 0;
        for (int i = 0; i < wirePath.length; i++) {

            String direction = wirePath[i].substring(0,1);
            int distance = Integer.parseInt(wirePath[i].substring(1));
            switch (direction.toUpperCase()) {
                case "U":
                    for(int n = 0; n < distance; n++) {
                        y++;
                        numberOfStepsTaken++;
                        Coordinate coord = new Coordinate(x, y, numberOfStepsTaken);
                        if(!wireCoordinates.contains(coord)) { // A little hackish but equality is by x,y alone... Not including number of steps taken
                            wireCoordinates.add(coord);
                        }
                    }
                    break;
                case "R":
                    for(int n = 0; n < distance; n++) {
                        x++;
                        numberOfStepsTaken++;
                        Coordinate coord = new Coordinate(x, y, numberOfStepsTaken);
                        if(!wireCoordinates.contains(coord)) {
                            wireCoordinates.add(coord);
                        }
                    }
                    break;
                case "D":
                    for(int n = 0; n < distance; n++) {
                        y--;
                        numberOfStepsTaken++;
                        Coordinate coord = new Coordinate(x, y, numberOfStepsTaken);
                        if(!wireCoordinates.contains(coord)) {
                            wireCoordinates.add(coord);
                        }
                    }
                    break;
                case "L":
                    for(int n = 0; n < distance; n++) {
                        x--;
                        numberOfStepsTaken++;
                        Coordinate coord = new Coordinate(x, y, numberOfStepsTaken);
                        if(!wireCoordinates.contains(coord)) {
                            wireCoordinates.add(coord);
                        }
                    }
                    break;
                default:
                    break;
            }

        }


    }

    static class Coordinate {
        private int x;
        private int y;
        private int numberOfStepsTaken;

        public Coordinate(int x, int y, int numberOfStepsTaken) {
            this.x = x;
            this.y = y;
            this.numberOfStepsTaken = numberOfStepsTaken;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getNumberOfStepsTaken() {
            return numberOfStepsTaken;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Coordinate)) return false;
            Coordinate that = (Coordinate) o;
            return getX() == that.getX() &&
                    getY() == that.getY();
        }

        @Override
        public int hashCode() {
            return Objects.hash(getX(), getY());
        }
    }

}
