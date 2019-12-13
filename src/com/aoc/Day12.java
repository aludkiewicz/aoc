package com.aoc;

import javafx.util.Pair;

import java.math.BigInteger;
import java.util.*;
import java.util.Map.Entry;

public class Day12 {

    private static Map<Vector3D, Vector3D> planetMap = new HashMap<>();
    private static final int STEPS = 1000;

    private static Set<Pair<Integer, Integer>> initialXState = new HashSet<>();
    private static Set<Pair<Integer, Integer>> initialYState = new HashSet<>();
    private static Set<Pair<Integer, Integer>> initialZState = new HashSet<>();

    public static void main(String[] args) {

        FileReader reader = new FileReader();

        List<String> day12Input = reader.read("input_day_12");

        populatePlanets(day12Input);

        part1();

        prepPart2(day12Input);

        part2();

    }

    private static void part2() {
        int xRecurrence = 0;
        int yRecurrence = 0;
        int zRecurrence = 0;

        boolean xRecurred = false;
        boolean yRecurred = false;
        boolean zRecurred = false;

        while(true) {
            applyGravity();
            applyVelocities();
            if(!xRecurred) {
                xRecurrence++;
            }

            if(!yRecurred) {
                yRecurrence++;
            }

            if(!zRecurred) {
                zRecurrence++;
            }

            Set<Pair<Integer, Integer>> xState = new HashSet<>();
            Set<Pair<Integer, Integer>> yState = new HashSet<>();
            Set<Pair<Integer, Integer>> zState = new HashSet<>();

            for (Entry<Vector3D, Vector3D> entry : planetMap.entrySet()) {
                xState.add(new Pair<>(entry.getKey().getX(), entry.getValue().getX()));
                yState.add(new Pair<>(entry.getKey().getY(), entry.getValue().getY()));
                zState.add(new Pair<>(entry.getKey().getZ(), entry.getValue().getZ()));
            }

            if(initialXState.containsAll(xState)) {
                xRecurred = true;
            }

            if(initialYState.containsAll(yState)) {
                yRecurred = true;
            }

            if(initialZState.containsAll(zState)) {
                zRecurred = true;
            }

            if(xRecurred && zRecurred && yRecurred) {
                break;
            }

        }

        System.out.println("First recurrence occurs after: " + lcm(xRecurrence, yRecurrence, zRecurrence) + " steps");
    }

    private static void prepPart2(List<String> day12Input) {
        planetMap.clear();
        populatePlanets(day12Input);

        for (Entry<Vector3D, Vector3D> entry : planetMap.entrySet()) {
            initialXState.add(new Pair<>(entry.getKey().getX(), entry.getValue().getX()));
            initialYState.add(new Pair<>(entry.getKey().getY(), entry.getValue().getY()));
            initialZState.add(new Pair<>(entry.getKey().getZ(), entry.getValue().getZ()));
        }
    }

    private static void part1() {
        for(int i = 0; i < STEPS; i++) {
            applyGravity();
            applyVelocities();
        }

        int energy = getEnergyOfSystem();

        System.out.println("The energy of the system after 1000 steps is: " + energy);
    }

    private static int getEnergyOfSystem() {
        int energy = 0;
        for (Entry<Vector3D, Vector3D> entry : planetMap.entrySet()) {

            Vector3D position = entry.getKey();
            Vector3D velocity = entry.getValue();

            int kinetic = Math.abs(position.getX()) + Math.abs(position.getY()) + Math.abs(position.getZ());
            int potential = Math.abs(velocity.getX()) + Math.abs(velocity.getY()) + Math.abs(velocity.getZ());

            energy += kinetic*potential;

        }
        return energy;
    }

    private static void applyVelocities() {

        Map<Vector3D, Vector3D> tmpMap = new HashMap<>();

        for (Entry<Vector3D, Vector3D> entry : planetMap.entrySet()) {

            Vector3D currentPos = entry.getKey();
            Vector3D velocity = entry.getValue();

            Vector3D newPos = new Vector3D(0,0,0);

            newPos.setX(currentPos.getX() + velocity.getX());
            newPos.setY(currentPos.getY() + velocity.getY());
            newPos.setZ(currentPos.getZ() + velocity.getZ());

            tmpMap.put(newPos, velocity);
        }
        planetMap.clear();
        planetMap.putAll(tmpMap);

    }

    private static void applyGravity() {

        Set<Vector3D> planets = planetMap.keySet();
        Set<Pair<Vector3D, Vector3D>> seenPairs = new HashSet<>();

        for (Vector3D thisPlanet : planets) {

            Set<Vector3D> otherPlanets = new HashSet<>(planetMap.keySet());
            otherPlanets.remove(thisPlanet);

            for (Vector3D thatPlanet : otherPlanets) {

                Pair<Vector3D, Vector3D> pair = new Pair<>(thisPlanet, thatPlanet);
                Pair<Vector3D, Vector3D> inversePair = new Pair<>(thatPlanet, thisPlanet);

                if(seenPairs.contains(pair) || seenPairs.contains(inversePair)) {
                    continue;
                }

                seenPairs.add(pair);
                seenPairs.add(inversePair);

                if(thisPlanet.getX() > thatPlanet.getX()) {
                    planetMap.get(thisPlanet).decrementX(); // The bigger one slows down
                    planetMap.get(thatPlanet).incrementX();
                } else if (thisPlanet.getX() < thatPlanet.getX()) {
                    planetMap.get(thatPlanet).decrementX();
                    planetMap.get(thisPlanet).incrementX();
                }

                if(thisPlanet.getY() > thatPlanet.getY()) {
                    planetMap.get(thisPlanet).decrementY();
                    planetMap.get(thatPlanet).incrementY();
                } else if (thisPlanet.getY() < thatPlanet.getY()) {
                    planetMap.get(thatPlanet).decrementY();
                    planetMap.get(thisPlanet).incrementY();
                }

                if(thisPlanet.getZ() > thatPlanet.getZ()) {
                    planetMap.get(thisPlanet).decrementZ();
                    planetMap.get(thatPlanet).incrementZ();
                } else if (thisPlanet.getZ() < thatPlanet.getZ()) {
                    planetMap.get(thatPlanet).decrementZ();
                    planetMap.get(thisPlanet).incrementZ();
                }
            }
        }

    }

    private static void populatePlanets(List<String> day12Input) {
        day12Input.stream()
                .map(s -> s.replace("<", ""))
                .map(s -> s.replace(">", ""))
                .map(s -> s.replace("x=", ""))
                .map(s -> s.replace("y=", ""))
                .map(s -> s.replace("z=", ""))
                .forEach(s -> {
                    String[] split = s.split(", ");
                    Vector3D pos = new Vector3D(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                    planetMap.put(pos, new Vector3D(0,0,0));
                });
    }

    public static long gcd(long a, long b) {
        return BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).longValue();
    }

    public static long lcm(long... numbers) {
        return Arrays.stream(numbers).reduce(1, (x, y) -> x * (y / gcd(x, y)));
    }

}
