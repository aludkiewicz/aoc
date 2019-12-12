package com.aoc;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Test {

    private static Coordinate axis = new Coordinate(0,1);
    private static Coordinate station = new Coordinate(11,13);

    public static void main(String[] args) {
        Coordinate a = new Coordinate(11,12);
        Coordinate b = new Coordinate(12,1);
        Coordinate c = new Coordinate(13,2);


        System.out.println(calcRotationAngleInDegrees(station,a));
        System.out.println(calcRotationAngleInDegrees(station,b));
        System.out.println(calcRotationAngleInDegrees(station,c));


    }

    public static String calcRotationAngleInDegrees(Coordinate centerPt, Coordinate targetPt) {
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
