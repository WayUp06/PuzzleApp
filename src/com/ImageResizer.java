package com;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class ImageResizer {
	public  static BufferedImage resizeImage(BufferedImage image, int width, int height) {
       int type;
       type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
       BufferedImage resizedImage = new BufferedImage(width, height,type);
       Graphics2D g = resizedImage.createGraphics();
       g.drawImage(image, 0, 0, width, height, null);
       g.dispose();
       return resizedImage;
    }

    public static BufferedImage rotate(BufferedImage bimg, double angle){
           int w = bimg.getWidth();
           int h = bimg.getHeight();

           BufferedImage rotated = new BufferedImage(w, h, bimg.getType());
           Graphics2D graphic = rotated.createGraphics();
           graphic.rotate(Math.toRadians(angle), w/2, h/2);
           graphic.drawImage(bimg, null, 0, 0);
           graphic.dispose();
           return rotated;
	}
}
