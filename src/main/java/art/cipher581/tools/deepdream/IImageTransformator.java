package art.cipher581.tools.deepdream;


import java.awt.image.BufferedImage;


public interface IImageTransformator {
    
    public BufferedImage transform(BufferedImage image, int frameNr);
    
}
