package art.cipher581.tools;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import art.cipher581.commons.util.ColorUtils;
import art.cipher581.commons.util.img.ImageUtils;


public class BlackAndWhiteMerge {
   
    private int threshold = 30;


    public BlackAndWhiteMerge(int threshold) {
        super();
        
        this.threshold = threshold;
    }
   
    
    public BufferedImage transform(List<BufferedImage> images) {
        int maxHeight = ImageUtils.getMaxHeight(images);
        int maxWidth = ImageUtils.getMaxWidth(images);
        
        BufferedImage t = ImageUtils.createImage(maxWidth, maxHeight, Color.WHITE);

        for (int x = 0; x < maxWidth; x++) {
            for (int y = 0; y < maxHeight; y++) {
                boolean black = false;
                
                for (BufferedImage image : images) {
                    if (image.getWidth() > x && image.getHeight() > y) {
                        Color c = new Color(image.getRGB(x, y));

                        if (ColorUtils.isBlack(c, threshold)) {
                            black = true;
                            break;
                        }
                    }
                }
                
                if (black) {
                    t.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
        
        return t;
    }
    
}
