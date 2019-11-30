package com.aoc;

import java.util.List;

public class Hello {

    public static void main(String[] args) {
        FileReader reader = new FileReader();

        List<String> lines = reader.read("hello.txt");

        lines.forEach(line -> {

            String[] lineParts = line.split(",");

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lineParts.length; i++) {
                sb.append(lineParts[i]).append(" ");
            }
            sb.setLength(sb.length() - 1);

            System.out.println(sb.toString());
        });
    }
}
