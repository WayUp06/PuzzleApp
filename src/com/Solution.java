package com;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;


public class Solution {

    public static long checkWithoutRotation(BufferedImage img1, BufferedImage img2){
        int width1 = img1.getWidth();
        int height1 = img1.getHeight();
        int x2 = 0;
        int x1 = width1 - 1;


        long difference = 0;

        for (int y = 0; y < height1; y++) {
            int rgbA = img1.getRGB(x1, y);
            int rgbB = img2.getRGB(x2, y);
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
        System.out.println(difference);
        return difference;
    }


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

                //System.out.println("result: " + difference);
            }
        }
        return 1000000000;
    }


    public static BufferedImage solve(BufferedImage [] arr){ // without rotations
        LinkedList<BufferedImage> list = new LinkedList<>(Arrays.asList(arr));
        BufferedImage result = null;

        for (int i = 0; i < 2; i++ ){ // count of merges in row = (count for row elements) - 1; i = rowcount - 1
        Iterator<BufferedImage> iterator = list.iterator();
        BufferedImage image1 = iterator.next();

        BufferedImage image2;
        result = image1;
        iterator.remove();
        while (iterator.hasNext()){
            image2 = iterator.next();
            if (checkWithoutRotation(image1, image2) < 9_000) {
                iterator.remove();
                result = ImageResizer.joinBufferedImage(image1, image2);
                list.add(0, result);
                System.out.println("Can be merged");
                break;

            } //connect, remove,add at first position, break
            else if (checkWithoutRotation(image2, image1) < 9_000) {
                iterator.remove();
                result = ImageResizer.joinBufferedImage(image2, image1);
                list.add(0, result);
                System.out.println("Can be merged");
                break;
            } //connect, remove,add at first position, break
        } }

        System.out.println("Solving");
        return result;

    }
}
