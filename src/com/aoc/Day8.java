package com.aoc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Day8 {

    private static final int WIDTH = 25;
    private static final int HEIGHT = 6;
    private static final String PATH = "/home/alud/code/aoc/src/resources/day8_img.jpg";

    public static void main(String[] args) throws IOException {

        FileReader reader = new FileReader();
        List<String> day8Input = reader.read("input_day_8");

        Map<Integer, Integer[][]> layers = new HashMap<>();

        List<Integer> ints = day8Input.get(0)
                .chars()
                .mapToObj(Character::getNumericValue)
                .collect(Collectors.toList());

        populateLayers(layers, ints);

        findMin(layers);

        Integer[][] reconstructedImg = new Integer[WIDTH][HEIGHT];
        reconstruct(layers, reconstructedImg);
        makeImg(reconstructedImg);

    }

    private static void makeImg(Integer[][] reconstructedImg) throws IOException {
        BufferedImage img= new BufferedImage( WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        for(int x=0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                int pixelVal = reconstructedImg[x][y] * 128;
                Color newColor = new Color(pixelVal,pixelVal,pixelVal);
                img.setRGB(x, y, newColor.getRGB());
            }
        }
        File output = new File(PATH);
        System.out.println("Image path is " + PATH);
        ImageIO.write(img, "jpg", output);
    }

    private static void reconstruct(Map<Integer, Integer[][]> layers, Integer[][] reconstructedImg) {
        for(int i = 0; i < WIDTH; i++) {
            for(int j = 0; j < HEIGHT; j++) {
                for(int layer = 1; layer <= layers.keySet().size(); layer++) {

                    int pixelVal = layers.get(layer)[i][j];
                    if(pixelVal == 0) {
                        reconstructedImg[i][j] = 0;
                        break;
                    } else if (pixelVal == 1) {
                        reconstructedImg[i][j] = 1;
                        break;
                    }
                }
            }
        }
    }

    private static void findMin(Map<Integer, Integer[][]> layers) {
        int minZeroes = Integer.MAX_VALUE;
        int minLayer = Integer.MAX_VALUE;
        for (Entry<Integer, Integer[][]> entry : layers.entrySet()) {
            Integer[][] values = entry.getValue();
            int nofZeroes = 0;
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    if (values[i][j] == 0) {
                        nofZeroes++;
                    }
                }
            }
            if (nofZeroes < minZeroes) {
                minZeroes = nofZeroes;
                minLayer = entry.getKey();
            }
        }

        Integer[][] minImg = layers.get(minLayer);
        int nofOnes = 0;
        int nofTwos = 0;
        for(int i = 0; i < WIDTH; i++) {
            for(int j = 0; j < HEIGHT; j++) {
                int val = minImg[i][j];
                if(val == 1) {
                    nofOnes++;
                } else if (val == 2) {
                    nofTwos++;
                }
            }
        }

        System.out.println("Number of ones times number of twos is: " + nofOnes * nofTwos);
    }

    private static void populateLayers(Map<Integer, Integer[][]> layers, List<Integer> ints) {
        int x = 0;
        int y = 0;

        int layer = 1;
        int i = 0;
        while (i < ints.size()) {
            layers.computeIfAbsent(layer, k -> new Integer[WIDTH][HEIGHT]);

            layers.get(layer)[x % WIDTH][y % HEIGHT] = ints.get(i);

            if(x % WIDTH == WIDTH - 1) {
                if(y % HEIGHT == HEIGHT - 1) {
                    layer++;
                }
                y++;
            }
            x++;
            i++;
        }
    }


}
