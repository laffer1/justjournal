package com.justjournal.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Image manipulation service
 * @author Lucas Holt
 */
@Slf4j
@Service
public class ImageService {
    private static final int AVATAR_MAX_WIDTH = 100;

    public BufferedImage resizeAvatar(final byte[] data) throws IOException {
        return scaleImageAsSquare(convertByteArrayToBufferedImage(data), AVATAR_MAX_WIDTH);
    }

    public byte[] convertBufferedImageToJpeg(final BufferedImage bufferedImage) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", baos);
        return baos.toByteArray();
    }

    public BufferedImage convertByteArrayToBufferedImage(final byte[] data) throws IOException {
        final ByteArrayInputStream bis = new ByteArrayInputStream(data);
        return ImageIO.read(bis);
    }

    public BufferedImage scaleImageAsSquare(final BufferedImage bufferedImage, final int boundSize) {
        final int origWidth = bufferedImage.getWidth();
        final int origHeight = bufferedImage.getHeight();
        final double scale;

        if (origHeight > origWidth)
            scale = boundSize / (double) origHeight;
        else
            scale = boundSize / (double) origWidth;

        // Don't scale up small images.
        if (scale > 1.0)
            return bufferedImage;

        final int scaledWidth = (int) (scale * origWidth);
        final int scaledHeight = (int) (scale * origHeight);

        final Image scaledImage = bufferedImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

        final BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);

        final Graphics2D g = scaledBI.createGraphics();

        g.drawImage(scaledImage, 0, 0, null);

        g.dispose();

        return scaledBI;
    }
}
