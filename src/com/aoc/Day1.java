package com.aoc;

import java.util.List;

public class Day1 {

    public static void main(String[] args) {

        FileReader reader = new FileReader();

        List<String> day1Input = reader.read("input_day_1");

        int sumNonRecursive = day1Input.stream()
                .mapToInt(Integer::parseInt)
                .map(i -> i/3 - 2)
                .sum();

        int sumRecursive = day1Input.stream()
                .mapToInt(Integer::parseInt)
                .map(Day1::getFuelReqModule)
                .sum();

        System.out.println(sumNonRecursive);
        System.out.println(sumRecursive);

    }


    public static int getFuelReqModule(int n) {
        if(n <= 8) { // floor(8/3) - 2 = 0
            return 0;
        } else {
            return n/3 - 2 + getFuelReqModule(n/3 - 2);
        }
    }

}
