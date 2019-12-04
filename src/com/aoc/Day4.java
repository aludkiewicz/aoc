package com.aoc;

import java.util.List;

public class Day4 {

    public static void main(String[] args) {

        FileReader reader = new FileReader();

        List<String> day4Input = reader.read("input_day_4");

        String[] day1InputElements = day4Input.get(0).split("-");

        int low = Integer.parseInt(day1InputElements[0]);
        int high = Integer.parseInt(day1InputElements[1]);

        int nofPotentialPwds = 0;
        while (low < high) {
            String potentialPassword  = String.valueOf(low);

            if(ruleOneMet(potentialPassword) && ruleTwoMet(potentialPassword)) {
                nofPotentialPwds++;
            }
            low++;
        }


        low = Integer.parseInt(day1InputElements[0]);
        high = Integer.parseInt(day1InputElements[1]);

        int nofPotentialPwdsPt2 = 0;
        while (low < high) {
            String potentialPassword  = String.valueOf(low);

            if(ruleOnePt2Met(potentialPassword) && ruleTwoMet(potentialPassword)) {
                nofPotentialPwdsPt2++;
            }
            low++;
        }

        System.out.println(nofPotentialPwds);
        System.out.println(nofPotentialPwdsPt2);


    }

    public static boolean ruleOnePt2Met(String s) {

        for(int i = 0; i < s.length() - 1; i++) {
            int first = Character.getNumericValue(s.charAt(i));
            int second = Character.getNumericValue(s.charAt(i + 1));

            if(first == second) {
                if(s.chars().filter(ch -> Character.getNumericValue(ch) == first).count() == 2) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean ruleOneMet(String s) {

        for(int i = 0; i < s.length() - 1; i++) {
            int first = Character.getNumericValue(s.charAt(i));
            int second = Character.getNumericValue(s.charAt(i + 1));

            if(first == second) {
                return true;
            }
        }
        return false;
    }

    public static boolean ruleTwoMet(String s) {

        for(int i = 0; i < s.length() - 1; i++) {
            int first = Character.getNumericValue(s.charAt(i));
            int second = Character.getNumericValue(s.charAt(i + 1));

            if(first > second) {
                return false;
            }
        }
        return true;
    }


}
