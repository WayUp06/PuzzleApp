package com;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;


public class Solution {

    public static long checkWithoutRotation(BufferedImage img1, BufferedImage img2, boolean vertically) {
        int width1 = img1.getWidth();
        int height1 = img1.getHeight();
        int edgeLength = vertically? width1: height1;
        double weightOfNeighbours = 0.2;
        double weightOfCurrent = 0.6;

        int stableCoordinate2 = 0;
        int stableCoordinate1 = vertically? (height1 - 1) : (width1 - 1);
        long difference = 0;

        int[] first = new int[edgeLength];
        int[] second = new int[edgeLength];

        if (vertically) {
            for (int x = 0; x < edgeLength; x++) {
                first[x] = img1.getRGB(x, stableCoordinate1);
                second[x] = img2.getRGB(x, stableCoordinate2);
            }
        } else {
            for (int y = 0; y < edgeLength; y++) {
                first[y] = img1.getRGB(stableCoordinate1, y);
                second[y] = img2.getRGB(stableCoordinate2, y);
            }
        }

        for (int i = 1; i < edgeLength - 1; i++) {
            double redA = (((first[i - 1] >> 16) & 0xff) * weightOfNeighbours) + (((first[i + 1] >> 16) & 0xff) * weightOfNeighbours) +
                (((first[i] >> 16) & 0xff) * weightOfCurrent);
            double redB = (((second[i - 1] >> 16) & 0xff) * weightOfNeighbours) + (((second[i + 1] >> 16) & 0xff) * weightOfNeighbours) +
                (((second[i] >> 16) & 0xff) * weightOfCurrent);

            double greenA = (((first[i - 1] >> 8) & 0xff) * weightOfNeighbours) + (((first[i + 1] >> 8) & 0xff) * weightOfNeighbours) +
                (((first[i] >> 8) & 0xff) * weightOfCurrent);
            double greenB = (((second[i - 1] >> 8) & 0xff) * weightOfNeighbours) + (((second[i + 1] >> 8) & 0xff) * weightOfNeighbours) +
                (((second[i] >> 8) & 0xff) * weightOfCurrent);

            double blueA =
                (((first[i - 1]) & 0xff) * weightOfNeighbours) + (((first[i + 1]) & 0xff) * weightOfNeighbours) + (((first[i]) & 0xff) * weightOfCurrent);
            double blueB =
                (((second[i - 1]) & 0xff) * weightOfNeighbours) + (((second[i + 1]) & 0xff) * weightOfNeighbours) + (((second[i]) & 0xff) * weightOfCurrent);

        /*for (int i = 0; i < edgeLength; i++){

            int redA = (first[i] >> 16) & 0xff;
            int greenA = (first[i] >> 8) & 0xff;
            int blueA = (first[i]) & 0xff;
            int redB = (second[i] >> 16) & 0xff;
            int greenB = (second[i] >> 8) & 0xff;
            int blueB = (second[i]) & 0xff;*/


            difference += Math.abs(redA - redB);
            difference += Math.abs(greenA - greenB);
            difference += Math.abs(blueA - blueB);

        }

        return difference;
    }


    public static BufferedImage solve(BufferedImage[] arr) {
        LinkedList<BufferedImage> list = new LinkedList<>(Arrays.asList(arr));
        LinkedList<BufferedImage> rowList = new LinkedList<>();
        BufferedImage image1, image2;
        long min;
        byte position;
        int index;
        BufferedImage result;
        int dimension = (int) Math.sqrt(arr.length);
        for (int j = 0; j < dimension; j++) {
            for (int i = 0; i < dimension - 1; i++) {
                Iterator<BufferedImage> iterator = list.iterator();
                image1 = iterator.next();

                iterator.remove();
                min = Long.MAX_VALUE;
                position = 0;
                index = 0;
                while (iterator.hasNext()) {
                    image2 = iterator.next();
                    if (checkWithoutRotation(image1, image2, false) < min) {
                        min = checkWithoutRotation(image1, image2, false);
                        index = list.indexOf(image2);
                        position = 0;
                    }
                    if (checkWithoutRotation(image2, image1, false) < min) {
                        min = checkWithoutRotation(image2, image1, false);
                        index = list.indexOf(image2);
                        position = 1;
                    }
                }
                image2 = list.remove(index);
                if (position == 0) {
                    result = ImageChanger.joinBufferedImage(image1, image2, false);
                } else {
                    result = ImageChanger.joinBufferedImage(image2, image1, false);
                }

                list.add(0, result);
                System.out.println(min);

            }
            rowList.add(list.poll());
        }

        for (int i = 0; i < dimension - 1; i++) {
            Iterator<BufferedImage> iterator = rowList.iterator();
            image1 = iterator.next();
            iterator.remove();
            min = Long.MAX_VALUE;
            position = 0;
            index = 0;

            while (iterator.hasNext()) {
                image2 = iterator.next();
                if (checkWithoutRotation(image1, image2, true) < min) {
                    min = checkWithoutRotation(image1, image2, true);
                    index = rowList.indexOf(image2);
                    position = 0;
                }
                if (checkWithoutRotation(image2, image1, true) < min) {
                    min = checkWithoutRotation(image2, image1, true);
                    index = rowList.indexOf(image2);
                    position = 1;
                }
            }

            image2 = rowList.remove(index);

            if (position == 0) {
                result = ImageChanger.joinBufferedImage(image1, image2, true);
            } else {
                result = ImageChanger.joinBufferedImage(image2, image1, true);
            }

            rowList.add(0, result);
        }

        result = rowList.poll();
        return result;
    }

}
