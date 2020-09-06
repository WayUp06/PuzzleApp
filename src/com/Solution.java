package com;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;


public class Solution { // крутити зображення в солв методі і передавати їх в чек, бо чек не верне всі результати

    public static long checkWithoutRotationHorizontally(BufferedImage img1, BufferedImage img2) {
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
        //System.out.println(difference);
        return difference;
    }

    public static long checkWithoutRotationVertically(BufferedImage img1, BufferedImage img2) {
        int width1 = img1.getWidth();
        int height1 = img1.getHeight();
        int y2 = 0;
        int y1 = height1 - 1;


        long difference = 0;

        for (int x = 0; x < height1; x++) {
            int rgbA = img1.getRGB(x, y1);
            int rgbB = img2.getRGB(x, y2);
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
        //System.out.println(difference);
        return difference;
    }




    public static long check(BufferedImage img1, BufferedImage img2) {
        int width1 = img1.getWidth();
        int height1 = img1.getHeight();
        int x2 = 0;
        int x1 = width1 - 1;
        long difference = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {

                BufferedImage a = ImageResizer.rotate(img1, 90 * i);
                BufferedImage b = ImageResizer.rotate(img2, 90 * j);


                difference = 0;

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

            }
        }
        return difference;
    }


    public static BufferedImage solve(BufferedImage[] arr) { // without rotations
        LinkedList<BufferedImage> list = new LinkedList<>(Arrays.asList(arr));
        LinkedList<BufferedImage> rowList = new LinkedList<>();
        BufferedImage image1, image2;
        long min;
        byte position;
        int index;
        BufferedImage result = null;
        int dimention = (int) Math.sqrt(arr.length);
        for (int j = 0; j < dimention; j++) {
            for (int i = 0; i < dimention - 1;
                 i++) { // count of merges in row = (count for row elements) - 1; i = rowcount - 1
                Iterator<BufferedImage> iterator = list.iterator();
                image1 = iterator.next();

                iterator.remove();
                min = Long.MAX_VALUE;
                position = 0;
                index = 0;
                while (iterator.hasNext()){
                    image2 = iterator.next();
                    if (checkWithoutRotationHorizontally(image1, image2) < min) {
                        min = checkWithoutRotationHorizontally(image1, image2);
                        index = list.indexOf(image2);
                        position = 0;
                    }

                    if (checkWithoutRotationHorizontally(image2, image1) < min){
                        min = checkWithoutRotationHorizontally(image2, image1);
                        index = list.indexOf(image2);
                        position = 1;
                    }

                }
                System.out.println(min);
                // to this point
                image2 = list.get(index);

                if (position == 0){
                    result = ImageResizer.joinBufferedImageHorizontally(image1, image2, false);
                } else { result = ImageResizer.joinBufferedImageHorizontally(image2, image1, false);}

                list.add(0, result);
                list.remove(image2);


            }
            rowList.add(list.poll()); // list with rows
        }

        for (int i = 0; i < dimention - 1; i++){
            Iterator<BufferedImage> iterator = rowList.iterator();
            image1 = iterator.next();
            iterator.remove();
            min = Long.MAX_VALUE;
            position = 0;
            index = 0;

            while (iterator.hasNext()){
                image2 = iterator.next();
                if (checkWithoutRotationVertically(image1, image2) < min) {
                    min = checkWithoutRotationVertically(image1, image2);
                    index = rowList.indexOf(image2);
                    position = 0;
                }

                if (checkWithoutRotationVertically(image2, image1) < min){
                    min = checkWithoutRotationHorizontally(image2, image1);
                    index = rowList.indexOf(image2);
                    position = 1;
                }
            }

            image2 = rowList.get(index);

            if (position == 0){
                result = ImageResizer.joinBufferedImageHorizontally(image1, image2, true);
            } else { result = ImageResizer.joinBufferedImageHorizontally(image2, image1, true);}

            rowList.add(0, result);
            rowList.remove(image2);

        }


        result = rowList.poll();

        System.out.println("Solving");
        return result;

    }
}
