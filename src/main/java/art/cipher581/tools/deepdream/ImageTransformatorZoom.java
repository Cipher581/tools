package art.cipher581.tools.deepdream;


import java.awt.image.BufferedImage;


public class ImageTransformatorZoom implements IImageTransformator {

    private double zoomFactor = 0.01;


    @Override
    public BufferedImage transform(BufferedImage image, int frameNr) {
        System.out.println("transforming image (zoom)");

        int xOffset = (int) ((double) image.getWidth() * zoomFactor);
        int yOffset = (int) ((double) image.getHeight() * zoomFactor);

        return image.getSubimage(xOffset, yOffset, image.getWidth() - 2 * xOffset, image.getHeight() - 2 * yOffset);
    }
    
    public ImageTransformatorZoom withZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;

        return this;
    }

}
