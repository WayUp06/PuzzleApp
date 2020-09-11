package com;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
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

    public static BufferedImage solveInNewWay(BufferedImage [] arr){
        ArrayList<BufferedImage> upperLeft = new ArrayList<>(2);
        ArrayList<BufferedImage> upperRight = new ArrayList<>(2);
        ArrayList<BufferedImage> lowerLeft = new ArrayList<>(2);
        ArrayList<BufferedImage> lowerRight = new ArrayList<>(2);
        LinkedList<BufferedImage> list = new LinkedList<>(Arrays.asList(arr));
        BufferedImage upperLeftImagetoCheck = null, upperRightImageToCheck = null, first = null, second = null;
        long min = Long.MAX_VALUE;
        int leftIndex = arr.length/2;
        BufferedImage [][] result = new BufferedImage[arr.length+2][arr.length+2];
        Iterator<BufferedImage> iterator = list.iterator();
        while (iterator.hasNext()){
            Iterator <BufferedImage> iterator1 = list.iterator();
            upperLeftImagetoCheck = iterator.next();
            while (iterator1.hasNext()){
                upperRightImageToCheck = iterator1.next();
                long current = checkWithoutRotation(upperLeftImagetoCheck,upperRightImageToCheck, false);
                if (current < min){
                    min = current;
                    first = upperLeftImagetoCheck;
                    second = upperRightImageToCheck;
                }
            }
        }

        list.remove(first);
        list.remove(second);

        int upperEdgeIndex = leftIndex;
        int lowerEdgeIndex = leftIndex;
        int leftEdgeIndex = leftIndex;
        int rightEdgeIndex = leftEdgeIndex + 1;
        result[leftIndex][leftIndex] = first;
        result[leftIndex][leftIndex + 1] = second;

        int dimension = (int) Math.sqrt(arr.length);


        BufferedImage bestUpperLeft = null, bestUpperRight = null, bestLowerLeft = null, bestLowerRight = null, one, two, three, four, five;
        BufferedImage lowerRightImageToCheck, lowerLeftImageToCheck;

        for (int i = 0; i < dimension - 1; i++){
            long oneMin = Long.MAX_VALUE, twoMin = Long.MAX_VALUE;

            upperLeftImagetoCheck = result[upperEdgeIndex][leftEdgeIndex];
            upperRightImageToCheck = result[upperEdgeIndex][rightEdgeIndex];
            lowerLeftImageToCheck = result[lowerEdgeIndex][leftEdgeIndex];
            lowerRightImageToCheck = result[lowerEdgeIndex][rightEdgeIndex];

            iterator = list.iterator();

            while (iterator.hasNext()){
                one = iterator.next();
                if (checkWithoutRotation(upperLeftImagetoCheck, one, true) < oneMin){
                    oneMin = checkWithoutRotation(upperLeftImagetoCheck, one, true);


                    if (upperLeft.size() < 2){
                        upperLeft.add(one);
                    }

                     else if (upperLeft.size() == 2){
                        upperLeft.set(1,upperLeft.get(0));
                        upperLeft.set(0, one);
                    }

                }
                 if (checkWithoutRotation(upperRightImageToCheck, one, true) < twoMin){
                     twoMin = checkWithoutRotation(upperRightImageToCheck, one, true);
                     if (upperRight.size() < 2){
                         upperRight.add(one);
                     }

                     else if (upperRight.size() == 2){
                         upperRight.set(1,upperRight.get(0));
                         upperRight.set(0, one);
                     }
                 }
            }


            long upperBest = 0;
            oneMin = Long.MAX_VALUE;
            for (BufferedImage a:upperLeft){
                for (BufferedImage b: upperRight){
                    if (checkWithoutRotation(a,b, false) < oneMin){
                        oneMin = checkWithoutRotation(a,b, false);
                        upperBest = checkWithoutRotation(a,b,false);
                        bestUpperLeft = a;
                        bestUpperRight = b;
                    }
                }
            }

            iterator = list.iterator();
            oneMin = Long.MAX_VALUE;
            twoMin = Long.MAX_VALUE;
            while (iterator.hasNext()){
                one = iterator.next();
                if (checkWithoutRotation(one, lowerLeftImageToCheck, true) < oneMin){
                    oneMin = checkWithoutRotation(lowerLeftImageToCheck, one, true);


                    if (lowerLeft.size() < 2){
                        lowerLeft.add(one);
                    } else if (lowerLeft.size() == 2) {
                        lowerLeft.set(1, lowerLeft.get(0));
                        lowerLeft.set(0, one);
                    }
                }
                if (checkWithoutRotation(one, lowerRightImageToCheck, true) < twoMin){
                    twoMin = checkWithoutRotation(one, lowerRightImageToCheck, true);

                    if (lowerRight.size() < 2){
                        lowerRight.add(one);
                    } else if (lowerRight.size() == 2) {
                        lowerRight.set(1, lowerRight.get(0));
                        lowerRight.set(0, one);
                    }
                }
            }


            long lowerBest = 0;
            oneMin = Long.MAX_VALUE;
            for (BufferedImage a:lowerLeft){
                for (BufferedImage b: lowerRight){
                    if (checkWithoutRotation(a,b, false) < oneMin){
                        oneMin = checkWithoutRotation(a,b, false);
                        lowerBest = checkWithoutRotation(a,b,false);
                        bestLowerLeft = a;
                        bestLowerRight = b;
                    }
                }
            }


            if (upperBest <= lowerBest) {
                result [upperEdgeIndex + 1][leftEdgeIndex] = bestUpperLeft;
                result [upperEdgeIndex + 1][rightEdgeIndex] = bestUpperRight;
                list.remove(bestUpperLeft);
                list.remove(bestUpperRight);
                upperEdgeIndex++;
            } else {
                result [lowerEdgeIndex - 1][leftEdgeIndex] = bestLowerLeft;
                result [lowerEdgeIndex - 1][rightEdgeIndex] = bestLowerRight;
                list.remove(bestLowerLeft);
                list.remove(bestLowerRight);
                lowerEdgeIndex--;
            }

        }

        for (int i = 0; i < dimension - 2; i++){

            upperLeft.clear();
            upperRight.clear();;
            lowerLeft.clear();
            lowerRight.clear();

            upperLeftImagetoCheck = result[upperEdgeIndex][leftEdgeIndex];
            upperRightImageToCheck = result[upperEdgeIndex][rightEdgeIndex];
            lowerLeftImageToCheck = result[upperEdgeIndex - 1][leftEdgeIndex];
            lowerRightImageToCheck = result[upperEdgeIndex - 1][rightEdgeIndex];

            long oneMin = Long.MAX_VALUE, twoMin = Long.MAX_VALUE;
            iterator = list.iterator();
            while (iterator.hasNext()){
                one = iterator.next();
                if (checkWithoutRotation(one, upperLeftImagetoCheck, false) < oneMin){
                    oneMin = checkWithoutRotation(one, upperLeftImagetoCheck, false);
                    if (upperLeft.size() < 2){
                        upperLeft.add(one);
                    }

                    else if (upperLeft.size() == 2){
                        upperLeft.set(1,upperLeft.get(0));
                        upperLeft.set(0, one);
                    }
                }

                if (checkWithoutRotation(one, lowerLeftImageToCheck, false) < twoMin){
                    twoMin = checkWithoutRotation(one, lowerLeftImageToCheck, false);
                    if (lowerLeft.size() < 2){
                        lowerLeft.add(one);
                    }

                    else if (lowerLeft.size() == 2){
                        lowerLeft.set(1,lowerLeft.get(0));
                        lowerLeft.set(0, one);
                    }
                }
            }

            long leftBest = 0;
            oneMin = Long.MAX_VALUE;
            for (BufferedImage a:upperLeft){
                for (BufferedImage b: lowerLeft){
                    if (checkWithoutRotation(b, a , true) < oneMin){ // найкращий результат шукається лише як різниця границі між новододаними, а можна як сума різниць між новими і ще й між старимиі новими
                        oneMin = checkWithoutRotation(b, a , true);
                        leftBest = checkWithoutRotation(b, a ,true);
                        bestUpperLeft = a;
                        bestLowerLeft = b;
                    }
                }
            }

            oneMin = Long.MAX_VALUE;
            iterator = list.iterator();
            while (iterator.hasNext()){
                one = iterator.next();
                if (checkWithoutRotation(upperRightImageToCheck, one, false) < oneMin){
                    oneMin = checkWithoutRotation(upperRightImageToCheck, one, false);
                    if (upperRight.size() < 2){
                        upperRight.add(one);
                    }

                    else if (upperRight.size() == 2){
                        upperRight.set(1,upperRight.get(0));
                        upperRight.set(0, one);
                    }
                }

                if (checkWithoutRotation(lowerRightImageToCheck, one, false) < oneMin){
                    oneMin = checkWithoutRotation(lowerRightImageToCheck, one, false);
                    if (lowerRight.size() < 2){
                        lowerRight.add(one);
                    }

                    else if (lowerRight.size() == 2){
                        lowerRight.set(1,lowerRight.get(0));
                        lowerRight.set(0, one);
                    }
                }


            }

            long rightBest = 0;
            twoMin = Long.MAX_VALUE;
            for (BufferedImage a:upperRight){
                for (BufferedImage b: lowerRight){
                    if (checkWithoutRotation(b, a , true) < twoMin){ // найкращий результат шукається лише як різниця границі між новододаними, а можна як сума різниць між новими і ще й між старимиі новими
                        twoMin = checkWithoutRotation(b, a , true);
                        rightBest = checkWithoutRotation(b, a ,true);
                        bestUpperRight = a;
                        bestLowerRight = b;
                    }
                }
            }

            boolean toLeft; // показує чи вліво ми взяли розширення
            if (leftBest <= rightBest) {
                result [upperEdgeIndex][leftEdgeIndex - 1] = bestUpperLeft;
                result [upperEdgeIndex - 1][leftEdgeIndex - 1] = bestLowerLeft;
                list.remove(bestUpperLeft);
                list.remove(bestLowerLeft);
                toLeft = true;

            } else {
                result [upperEdgeIndex][rightEdgeIndex + 1] = bestLowerLeft;
                result [upperEdgeIndex - 1][rightEdgeIndex + 1] = bestLowerRight;
                list.remove(bestLowerLeft);
                list.remove(bestLowerRight);
                toLeft = false;
            }
            //колонка ка ще заповняється, найнижчий заповнений рядок
            int column = 0, rowToCheck = upperEdgeIndex - 1;
            if (toLeft) {
                column = leftEdgeIndex - 1;
            } else {
                column = rightEdgeIndex + 1;
            }
            if (toLeft) {
                for (int z = 0; z < dimension - 2; z++) {
                    iterator = list.iterator();
                    min = Long.MAX_VALUE;
                    BufferedImage image, needImage = null;
                    while (iterator.hasNext()) {
                        image = iterator.next();

                        long c = checkWithoutRotation(image, result[rowToCheck - 1][column + 1], false) +
                            checkWithoutRotation(image, result[rowToCheck][column], true);

                        if (c < min) {
                            min = c;
                            needImage = image;
                        }
                    }

                    result[rowToCheck - 1][column] = needImage;
                    rowToCheck--;
                    list.remove(needImage);

                }
                leftEdgeIndex--;

            } else {//ми пішли вправо

                for (int z = 0; z < dimension - 2; z++){
                    iterator = list.iterator();
                    min = Long.MAX_VALUE;
                    BufferedImage image, needImage = null;

                    while (iterator.hasNext()) {
                        image = iterator.next();

                        long c = checkWithoutRotation(result[rowToCheck - 1][column - 1], image , false) + checkWithoutRotation(image, result[rowToCheck][column], true);

                        if (c < min) {
                            min = c;
                            needImage = image;
                        }
                    }

                    result[rowToCheck - 1][column] = needImage;
                    rowToCheck--;
                    list.remove(needImage);

                }
                rightEdgeIndex++;

            }
        }

        BufferedImage image;
        LinkedList<BufferedImage> rowList = new LinkedList<>();
        for (int i = upperEdgeIndex; i >= lowerEdgeIndex; i--){
            image = result[i][leftEdgeIndex];
            for (int j = leftEdgeIndex + 1; j <= rightEdgeIndex; j++) {
                image = ImageChanger.joinBufferedImage(image, result[i][j], false);
            }
            rowList.add(image);
        }

        Iterator<BufferedImage> iterator1 = rowList.iterator();
        image = iterator1.next();
        while (iterator1.hasNext()){
            image = ImageChanger.joinBufferedImage(iterator1.next(), image, true);
        }

        return image;

    }

}
