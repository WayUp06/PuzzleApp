package com;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;


public class Solution { // крутити зображення в солв методі і передавати їх в чек, бо чек не верне всі результати

    public static long checkWithoutRotation(BufferedImage img1, BufferedImage img2) {
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

                //System.out.println("result: " + difference);
            }
        }
        return difference;
    }


    public static BufferedImage solve(BufferedImage[] arr) { // without rotations
        LinkedList<BufferedImage> list = new LinkedList<>(Arrays.asList(arr));
        LinkedList<BufferedImage> rowList = new LinkedList<>();
        BufferedImage result = null;
        int dimention = (int) Math.sqrt(arr.length);
        for (int j = 0; j < dimention; j++) {
            for (int i = 0; i < dimention - 1;
                 i++) { // count of merges in row = (count for row elements) - 1; i = rowcount - 1
                Iterator<BufferedImage> iterator = list.iterator();
                BufferedImage image1 = iterator.next();

                BufferedImage image2;
                result = image1;
                iterator.remove();
                long min = Long.MAX_VALUE;
                byte position = 0;
                int index = 0; // of element that we take, real_index = index/2, side = index%2, if 0 noooooooooooooo, check index in another way
                while (iterator.hasNext()){
                    image2 = iterator.next();
                    if (checkWithoutRotation(image1, image2) < min) {
                        min = checkWithoutRotation(image1, image2);
                        index = list.indexOf(image2);
                        position = 0;
                    }

                    if (checkWithoutRotation(image2, image1) < min){
                        min = checkWithoutRotation(image2, image1);
                        index = list.indexOf(image2);
                        position = 1;
                    }

                }
                System.out.println(min);

                image2 = list.get(index);

                if (position == 0){
                    result = ImageResizer.joinBufferedImageHorizontally(image1, image2);
                } else { result = ImageResizer.joinBufferedImageHorizontally(image2, image1);}

                list.add(0, result);
                list.remove(image2);









                /*while (iterator.hasNext()) {
                    image2 = iterator.next();
                    if (checkWithoutRotation(image1, image2) < 6500) {
                        System.out.println(checkWithoutRotation(image1, image2) + "     11111111111111111");
                        iterator.remove();
                        result = ImageResizer.joinBufferedImageHorizontally(image1, image2);
                        list.add(0, result);
                        System.out.println("Can be merged");
                        break;

                    } //connect, remove,add at first position, break
                    else if (checkWithoutRotation(image2, image1) < 6500) {
                        System.out.println(checkWithoutRotation(image2, image1) + "         1111111111111111111111111111");
                        iterator.remove();
                        result = ImageResizer.joinBufferedImageHorizontally(image2, image1);
                        list.add(0, result);
                        System.out.println("Can be merged");
                        break;
                    } //connect, remove,add at first position, break
                }*/
            }
            rowList.add(list.poll()); // list with rows
        }
        while(rowList.size() > 1) {
            rowList.add(0, ImageResizer.joinBufferedImageVertically(rowList.poll(), rowList.poll()));
        }
        result = rowList.poll();

        System.out.println("Solving");
        return result;

    }
}
