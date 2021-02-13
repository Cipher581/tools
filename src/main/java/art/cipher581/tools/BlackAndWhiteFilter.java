package art.cipher581.tools;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import javax.imageio.ImageIO;

import art.cipher581.commons.util.ColorUtils;
import art.cipher581.commons.util.FileUtilities;
import art.cipher581.commons.util.img.ImageUtils;


public class BlackAndWhiteFilter {

    private int threshold = 30;


    public BlackAndWhiteFilter() {
        super();
    }


    public BlackAndWhiteFilter(int threshold) {
        super();

        this.threshold = threshold;

    }


    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }


    public BufferedImage transform(BufferedImage img) {
        BufferedImage t = ImageUtils.createImage(img.getWidth(), img.getHeight(), Color.WHITE);

        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                Color c = new Color(img.getRGB(x, y));

                if (ColorUtils.isBlack(c, threshold)) {
                    t.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }

        return t;
    }


    public File transform(File file, boolean overwrite) throws IOException {
        String extension = FileUtilities.getExtension(file);

        return transform(file, extension, overwrite);
    }


    public File transform(File file, String format, boolean overwrite) throws IOException {
        String outputFileName = FileUtilities.getFileNameWithoutExtension(file) + "_black_white." + format;

        File outputFile = new File(file.getParentFile(), outputFileName);

        if (outputFile.exists() && !overwrite) {
            throw new FileAlreadyExistsException("file " + outputFile + " already exists");
        }

        BufferedImage img = ImageUtils.load(file);

        BufferedImage t = transform(img);

        ImageIO.write(t, format, outputFile);

        return outputFile;
    }

}
