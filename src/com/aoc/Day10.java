package com.aoc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class Day10 {

    private static final char ASTEROID = '#';
    private static final char EMPTY = '.';

    private static int WIDTH = 0;
    private static int HEIGHT = 0;

    private static int bestX = 0;
    private static int bestY = 0;

    private static Map<Double, List<Coordinate>> asteroidsPerAngle = new TreeMap<>(Double::compareTo);
    private static Coordinate yAxis = new Coordinate(0,1);
    private static Coordinate station;

    public static void main(String[] args) {

        FileReader reader = new FileReader();
        List<String> day10Input = reader.read("input_day_10");

        int[][] asteroidField = new int[day10Input.get(0).length()][day10Input.size()];

        populateField(day10Input, asteroidField);
        List<Coordinate> asteroidCoords = new ArrayList<>();
        List<Coordinate> allCoordinates = new ArrayList<>();

        for(int x = 0; x < WIDTH; x++) {
            for(int y = 0; y < HEIGHT; y++) {
                Coordinate coordinate = new Coordinate(x, y);
                allCoordinates.add(coordinate);
                if (asteroidField[x][y] == 1) {
                    asteroidCoords.add(coordinate);
                }
            }
        }

        part1(asteroidField, asteroidCoords);
        part2(asteroidField, allCoordinates);
        return;

    }

    private static void part2(int[][] asteroidField, List<Coordinate> allCoordinates) {
        station = new Coordinate(bestX, bestY);
        for(int x = 0; x < WIDTH; x++) {
            for(int y = 0; y < HEIGHT; y++) {
                Coordinate coordinate = new Coordinate(x, y);
                allCoordinates.add(coordinate);
                if (asteroidField[x][y] == 1) {

                    Double angle = getAngle(station, coordinate);

                    asteroidsPerAngle.computeIfAbsent(angle, k -> new ArrayList<>());
                    asteroidsPerAngle.get(angle).add(coordinate);

                }
            }
        }
        Coordinate laser = new Coordinate(bestX, bestY);

        int nofPewPewed = 0;
        while (true) {
            for (Map.Entry<Double, List<Coordinate>> entry : asteroidsPerAngle.entrySet()) {
                List<Coordinate> asteroids = entry.getValue();
                if(asteroids.size() == 0) {
                    continue;
                }

                double minDistance = Double.MAX_VALUE;
                Coordinate tmp = null;
                for (Coordinate asteroid : asteroids) {
                    if(distanceBetween(asteroid, laser) < minDistance) {
                        minDistance = distanceBetween(asteroid, laser);
                        tmp = asteroid;
                    }
                }
                asteroids.remove(tmp);

                nofPewPewed++;
                if(nofPewPewed == 200) {
                    System.out.println("100*x + y was: " + (tmp.x * 100 + tmp.y));
                    return;
                }
            }
        }
    }

    private static void part1(int[][] asteroidField, List<Coordinate> asteroidCoords) {
        int maxVisible = 0;
        for (Coordinate coord1 : asteroidCoords) {
            Set<Coordinate> visibleAsteroids = new HashSet<>(asteroidCoords);
            visibleAsteroids.remove(coord1);
            for (Coordinate coord2 : asteroidCoords) {

                if(coord1.equals(coord2)) {
                    continue;
                }

                Set<Coordinate> asteroidsBetween = asteroidsBetween(coord1, coord2, asteroidField);

                Coordinate closestAsteroid = closestAsteroid(coord1, asteroidsBetween, asteroidField);
                asteroidsBetween.remove(closestAsteroid);
                visibleAsteroids.removeAll(asteroidsBetween); // These are out of line of sight, due to not being closest
            }
            if(visibleAsteroids.size() > maxVisible) {
                maxVisible = visibleAsteroids.size();
                bestX = coord1.x;
                bestY = coord1.y;
            }
        }
        System.out.println("Maximum visible asteroids: " + maxVisible);
    }

    public static Double getAngle(Coordinate a, Coordinate b) {
        double theta = Math.atan2(b.y - a.y, b.x - a.x);

        theta += Math.PI/2.0;

        double angle = Math.toDegrees(theta);
        if (angle < 0) {
            angle += 360;
        }

        return round(angle, 8);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private static void populateField(List<String> day10Input, int[][] asteroidField) {
        HEIGHT = day10Input.size();
        WIDTH = day10Input.get(0).length();

        for (int i = 0; i < HEIGHT; i++) {
            String row = day10Input.get(i);
            for(int j = 0; j < WIDTH; j++) {
                if(row.charAt(j) == ASTEROID) {
                    asteroidField[j][i] = 1;
                } else if (row.charAt(j) == EMPTY) {
                    asteroidField[j][i] = 0;
                }
            }
        }
    }

    private static Coordinate closestAsteroid(Coordinate coord, Set<Coordinate> points, int[][] asteroidField) {
        Coordinate tmp = null;
        double dist = Double.MAX_VALUE;
        for (Coordinate potentialAsteroid : points) {
            if(asteroidField[potentialAsteroid.x][potentialAsteroid.y] == 0) {
                continue;
            }

            if(distanceBetween(coord, potentialAsteroid) < dist) {
                tmp = potentialAsteroid; // is currently closest asteroid
            }
        }
        return tmp;
    }

    private static double distanceBetween(Coordinate coord, Coordinate point) {
        return Math.sqrt((coord.x - point.x)*(coord.x - point.x) + (coord.y - point.y)*(coord.y - point.y));
    }

    private static Set<Coordinate> asteroidsBetween(Coordinate coord1, Coordinate coord2, int[][] asteroidField) {
        int x = coord1.x - coord2.x;
        int y = coord1.y - coord2.y;

        int gcd = gcd(x, y);

        x = x / gcd;
        y = y / gcd;

        if(x < 0 && y < 0) {
            x = -x;
            y = -y;
        }

        Set<Coordinate> points = new HashSet<>();

        Coordinate tmp = new Coordinate(coord1.x + x, coord1.y + y);

        while (coordinateInBoundingBox(coord1, coord2, tmp)) {
            points.add(tmp);
            if(asteroidField[tmp.x][tmp.y] == 1) {
                points.add(tmp);
            }
            tmp = new Coordinate(tmp.x + x, tmp.y + y);
        }

        tmp = new Coordinate(coord1.x - x, coord1.y - y);
        while (coordinateInBoundingBox(coord1, coord2, tmp)) {
            if(asteroidField[tmp.x][tmp.y] == 1) {
                points.add(tmp);
            }
            tmp = new Coordinate(tmp.x - x, tmp.y - y);
        }

        points.add(coord2);

        return points;
    }

    private static boolean coordinateInBoundingBox(Coordinate coord1, Coordinate coord2, Coordinate tmp) {

        int lowX = Math.min(coord1.x, coord2.x);
        int lowy = Math.min(coord1.y, coord2.y);
        int highX = Math.max(coord1.x, coord2.x);
        int highY = Math.max(coord1.y, coord2.y);

        return tmp.x <= highX && tmp.y <= highY && tmp.x >= lowX && tmp.y >= lowy;
    }

    private static double dotProduct(Coordinate a, Coordinate b) {
        return (double) a.x * b.x + a.y * b.y;
    }


    public static int gcd(int a, int b) {
        return BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).intValue();
    }

    static class Coordinate {
        int x;
        int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
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
