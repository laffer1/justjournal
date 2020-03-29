package com.justjournal.utility;

import com.justjournal.exception.ThumbnailException;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class Thumbnail {
    private int thumbWidth;  /* Suggested thumbnail max width */
    private int thumbHeight; /* Suggested thumbnail max height */

    /**
     * Create Thumbnail with default width & height 100
     */
    public Thumbnail() {
        this(100, 100);
    }

    public Thumbnail(final int width, final int height) {
        super();
        thumbWidth = width;
        thumbHeight = height;
    }

    protected double calcThumbRatio() {
        return (double) thumbWidth / (double) thumbHeight;
    }

    protected double calcImageRatio(final Image image) {
        final int imageWidth = image.getWidth(null);
        final int imageHeight = image.getHeight(null);
        return (double) imageWidth / (double) imageHeight;
    }

    public int getThumbWidth() {
        return thumbWidth;
    }

    public void setThumbWidth(final int thumbWidth) {
        this.thumbWidth = thumbWidth;
    }

    public int getThumbHeight() {
        return thumbHeight;
    }

    public void setThumbHeight(final int thumbHeight) {
        this.thumbHeight = thumbHeight;
    }

    public void create(final String inputfile, final String outputfile) {
        try {
            File f = new File(inputfile);
            BufferedImage bi = ImageIO.read(f);

            int height = thumbHeight;
            int width = thumbWidth;
            final double imageRatio = calcImageRatio(bi);

            if (calcThumbRatio() < imageRatio) {
                height = (int) (thumbWidth / imageRatio);
            } else {
                width = (int) (thumbHeight * imageRatio);
            }
            // draw original image to thumbnail image object and
            // scale it to the new size on-the-fly
            BufferedImage thumbImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = thumbImage.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.drawImage(bi, 0, 0, width, height, null);

            // save thumbnail image to OUTFILE
            final File outfile = new File(outputfile);
            ImageIO.write(thumbImage, "png", outfile);
        } catch (final FileNotFoundException fe) {
            log.error("create(): File not found", fe);
            throw new ThumbnailException();
        } catch (final IOException ioe) {
            log.error("create(): IO error", ioe);
            throw new ThumbnailException();
        }
    }

}
