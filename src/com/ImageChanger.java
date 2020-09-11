package com;

import java.awt.*;
import java.awt.image.BufferedImage;


public class ImageChanger {
	public  static BufferedImage resizeImage(BufferedImage image, int width, int height) {
       int type;
       type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
       BufferedImage resizedImage = new BufferedImage(width, height,type);
       Graphics2D g = resizedImage.createGraphics();
       g.drawImage(image, 0, 0, width, height, null);
       g.dispose();
       return resizedImage;
    }

    public static BufferedImage resizeBetter(BufferedImage img,
                                             int targetWidth,
                                             int targetHeight,
                                             Object hint,
                                             boolean higherQuality){
           int type = (img.getTransparency() == Transparency.OPAQUE) ?
               BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
           BufferedImage ret = img;
           int w, h;
           if (higherQuality) {
                  w = img.getWidth();
                  h = img.getHeight();
           } else {
                  w = targetWidth;
                  h = targetHeight;
           }

           do {
                  if (higherQuality && w > targetWidth) {
                         w /= 2;
                         if (w < targetWidth) {
                                w = targetWidth;
                         }
                  }

                  if (higherQuality && h > targetHeight) {
                         h /= 2;
                         if (h < targetHeight) {
                                h = targetHeight;
                         }
                  }

                  BufferedImage tmp = new BufferedImage(w, h, type);
                  Graphics2D g2 = tmp.createGraphics();
                  g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
                  g2.drawImage(ret, 0, 0, w, h, null);
                  g2.dispose();

                  ret = tmp;
           } while (w != targetWidth || h != targetHeight);

           return ret;


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

	   //зліва 1, справа 2
       //знизу 1, зверху 2
       public static BufferedImage joinBufferedImage(BufferedImage img1, BufferedImage img2, boolean vertically) {

              int offset  = 0; // border between images
              int width, height;
              if (vertically) {
                     width = img1.getWidth();
                     height = img1.getHeight() + img2.getHeight();
              }
              else {
                     width = img1.getWidth()+img2.getWidth()+offset;
                     height = Math.max(img1.getHeight(),img2.getHeight())+offset;
              }

              BufferedImage newImage = new BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB);
              Graphics2D g2 = newImage.createGraphics();
              Color oldColor = g2.getColor();
              g2.setPaint(Color.WHITE);
              g2.fillRect(0, 0, width, height);
              g2.setColor(oldColor);
              g2.drawImage(img1, null, 0, 0);
              if (vertically) {g2.drawImage(img2, null, 0, img1.getHeight());}
              else {g2.drawImage(img2, null, img1.getWidth()+offset, 0);}

              g2.dispose();
              return newImage;
       }

}
