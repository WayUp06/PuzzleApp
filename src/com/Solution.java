package com;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import javax.swing.*;

public class Solution {
    public static long check(BufferedImage img1, BufferedImage img2) {
        int width1 = img1.getWidth();
        int height1 = img1.getHeight();
        int x2 = 0;
        int x1 = width1 - 1;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {

                BufferedImage a = ImageResizer.rotate(img1, 90*i);
                BufferedImage b = ImageResizer.rotate(img2, 90*j);


                long difference = 0;

                for (int y = 0; y < height1; y++) {
                    int rgbA = a.getRGB(x1, y);
                    int rgbB = b.getRGB(x2, y);
                    int redA = (rgbA >> 16) & 0xff;
                    int greenA = (rgbA >> 8) & 0xff;
                    int blueA = (rgbA) & 0xff;
                    int redB = (rgbB >> 16) & 0xff;
                    int greenB = (rgbB >> 8) & 0xff;
                    int blueB = (rgbB) & 0xff;
                    difference += Math.abs(redA - redB);
                    difference += Math.abs(greenA - greenB);
                    difference += Math.abs(blueA - blueB);
                }

                System.out.println("result: " + difference);
            }
        }
        return 1000000000;
    }
}
