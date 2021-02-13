package art.cipher581.tools.pixelart.ui;


import java.awt.Color;
import java.awt.image.BufferedImage;
import art.cipher581.commons.util.img.IImageModifier;
import art.cipher581.commons.util.img.ImageUtils;


public class ImageModifier implements IImageModifier {

    private int zoom = 1;

    
    public void setZoom(int zoom) {
        if (zoom < 1) {
            throw new IllegalArgumentException("invalid zoom factor: " + zoom);
        }

        this.zoom = zoom;
    }

    public int getZoom() {
        return zoom;
    }
    
    
    
    @Override
    public BufferedImage modify(BufferedImage originalImage) {
        if (zoom == 1) {
            return originalImage;
        }
        
        BufferedImage image = ImageUtils.createImage(originalImage.getWidth() * zoom, originalImage.getHeight() * zoom, Color.WHITE);

        for (int x = 0; x < originalImage.getWidth(); x++) {
            for (int y = 0; y < originalImage.getHeight(); y++) {
                int rgb = originalImage.getRGB(x, y);
                
                for (int i = 0; i < zoom; i++) {
                    int nx = x * zoom + i;
                    
                    for (int j = 0; j < zoom; j++) { 
                        int ny = y * zoom + j;

                        image.setRGB(nx, ny, rgb); 
                    }
                }
            }
        }
        
        return image;
    }
    
}
