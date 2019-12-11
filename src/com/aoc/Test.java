package com.aoc;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Test {

    private static Day10.Coordinate axis = new Day10.Coordinate(0,1);
    private static Day10.Coordinate station = new Day10.Coordinate(11,13);

    public static void main(String[] args) {
        Day10.Coordinate a = new Day10.Coordinate(11,12);
        Day10.Coordinate b = new Day10.Coordinate(12,1);
        Day10.Coordinate c = new Day10.Coordinate(13,2);


        System.out.println(calcRotationAngleInDegrees(station,a));
        System.out.println(calcRotationAngleInDegrees(station,b));
        System.out.println(calcRotationAngleInDegrees(station,c));


    }

    public static String calcRotationAngleInDegrees(Day10.Coordinate centerPt, Day10.Coordinate targetPt) {
        double theta = Math.atan2(targetPt.y - centerPt.y, targetPt.x - centerPt.x);

        theta += Math.PI/2.0;

        double angle = Math.toDegrees(theta);
        if (angle < 0) {
            angle += 360;
        }

        DecimalFormat df = new DecimalFormat("#.#####");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(angle);
    }

}
