package com.justjournal.utility;

import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Create thumbnails of full images as PNG
 *
 * @author Lucas Holt
 */
public class Thumbnail {
    private int thumbWidth;  /* Suggested thumbnail max width */
    private int thumbHeight; /* Suggested thumbnail max height */

    private org.slf4j.Logger log = LoggerFactory.getLogger(Thumbnail.class);

    /**
     * Create Thumbnail with default width & height 100
     */
    public Thumbnail() {
        thumbWidth = 100;
        thumbHeight = 100;
    }

    public Thumbnail(int width, int height) {
        thumbWidth = width;
        thumbHeight = height;
    }

    protected double calcThumbRatio() {
        return (double) thumbWidth / (double) thumbHeight;
    }

    protected double calcImageRatio(Image image) {
        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);
        return (double) imageWidth / (double) imageHeight;
    }

    public int getThumbWidth() {
        return thumbWidth;
    }

    public void setThumbWidth(int thumbWidth) {
        this.thumbWidth = thumbWidth;
    }

    public int getThumbHeight() {
        return thumbHeight;
    }

    public void setThumbHeight(int thumbHeight) {
        this.thumbHeight = thumbHeight;
    }

    public void create(String inputfile, String outputfile) throws Exception {
        File f = new File(inputfile);
        BufferedImage bi = ImageIO.read(f);

        int height = thumbHeight;
        int width = thumbWidth;
        double imageRatio = calcImageRatio(bi);
        if (calcThumbRatio() < imageRatio) {
            height = (int) (thumbWidth / imageRatio);
        } else {
            width = (int) (thumbHeight * imageRatio);
        }
        // draw original image to thumbnail image object and
        // scale it to the new size on-the-fly
        BufferedImage thumbImage = new BufferedImage(width,
                height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = thumbImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(bi, 0, 0, width, height, null);
        // save thumbnail image to OUTFILE
        try {
            File outfile = new File(outputfile);
            ImageIO.write(thumbImage, "png", outfile);
        } catch (final FileNotFoundException fe) {
            log.error("create(): File not found", fe);
            throw new Exception("Could not create thumbnail");
        } catch (IOException ioe) {
           log.error("create(): IO error", ioe);
            throw new Exception("Could not create thumbnail");
        }
    }

}
