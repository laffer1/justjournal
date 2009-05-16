package com.justjournal.utility;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Create thumbnails of full images as JPEGs.
 *
 * @author Lucas Holt
 * @version $Id: Thumbnail.java,v 1.2 2009/05/16 02:31:02 laffer1 Exp $
 * @since 1.0
 * 
 * User: laffer1
 * Date: Jul 24, 2008
 * Time: 7:57:00 PM
 */
public class Thumbnail {

    private int quality;     /* JPEG quality */
    private int thumbWidth;  /* Suggested thumbnail max width */
    private int thumbHeight; /* Suggested thumbnail max height */

    private static final Logger log = Logger.getLogger(Thumbnail.class);

    /**
     * Create Thumbnail with default quality 75, width & height 100
     */
    public Thumbnail() {
        quality = 75;
        thumbWidth = 100;
        thumbHeight = 100;
    }

    public Thumbnail(int width, int height, int qual)
    {
        quality = qual;
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

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
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
        Image image = Toolkit.getDefaultToolkit().getImage(inputfile);
        MediaTracker mediaTracker = new MediaTracker(new Container());
        mediaTracker.addImage(image, 0);

        try {
            mediaTracker.waitForID(0);
        } catch (InterruptedException ie) {
            log.error("create(): Media Tracker interrupted, " + ie.getMessage() );
            throw new Exception("Could not create thumbnail");
        }

        int height = thumbHeight;
        int width = thumbWidth;
        double imageRatio = calcImageRatio(image);
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
        graphics2D.drawImage(image, 0, 0, width, height, null);
        // save thumbnail image to OUTFILE
        try {
        BufferedOutputStream out = new BufferedOutputStream(new
                FileOutputStream(outputfile));
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        JPEGEncodeParam param = encoder.
                getDefaultJPEGEncodeParam(thumbImage);

        quality = Math.max(0, Math.min(quality, 100));
        param.setQuality((float) quality / 100.0f, false);
        encoder.setJPEGEncodeParam(param);
        encoder.encode(thumbImage);
        out.close();
        } catch (FileNotFoundException fe) {
            log.error("create(): File not found, " + fe.getMessage() );
            throw new Exception("Could not create thumbnail");
        } catch (IOException ioe) {
           log.error("create(): IO error, " + ioe.getMessage() );
            throw new Exception("Could not create thumbnail");
        }
    }

}
