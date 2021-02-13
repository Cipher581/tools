package art.cipher581.tools;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import art.cipher581.commons.util.FileUtilities;
import art.cipher581.commons.util.img.ImageUtils;


public class ImageSplitter {

    public List<File> split(File file, File outputDir, String format, int horizontalCount, int verticalCount, int overlap) throws IOException {
        BufferedImage img = ImageUtils.load(file);

        String outputFileName = FileUtilities.getFileNameWithoutExtension(file);

        return split(img, outputDir, outputFileName, format, horizontalCount, verticalCount, overlap);
    }


    public List<File> split(BufferedImage img, File outputDir, String outputFileName, String format, int horizontalCount, int verticalCount, int overlap) throws IOException {
        List<BufferedImage> parts = split(img, horizontalCount, verticalCount, overlap);

        if (!outputDir.exists()) {
            FileUtilities.createDir(outputDir);
        }

        DecimalFormat df = new DecimalFormat("000");

        List<File> files = new ArrayList<>(parts.size());

        int i = 0;
        for (BufferedImage part : parts) {
            File outputFile = new File(outputDir, outputFileName + "_part" + df.format(i) + "." + format);

            if (outputFile.exists()) {
                throw new FileAlreadyExistsException("file " + outputFile + " already exists");
            }

            ImageIO.write(part, format, outputFile);
            i++;

            files.add(outputFile);
        }

        return files;
    }


    public List<BufferedImage> split(BufferedImage img, int horizontalCount, int verticalCount, int overlap) {
        if (overlap < 0) {
            throw new IllegalArgumentException("overlap must be >= 0");
        }

        List<BufferedImage> parts = new LinkedList<>();

        double width = img.getWidth() / horizontalCount;
        double height = img.getHeight() / verticalCount;

        for (int h = 0; h < horizontalCount; h++) {
            for (int v = 0; v < verticalCount; v++) {
                int leftOverlap = h == 0 ? 0 : overlap;
                int rightOverlap = (h + 1) < horizontalCount ? overlap : 0;

                int topOverlap = v == 0 ? 0 : overlap;
                int bottomOverlap = (v + 1) < verticalCount ? overlap : 0;

                int x = ((int) (h * width)) - leftOverlap;
                int y = ((int) (v * height)) - topOverlap;

                int partWidth = ((int) ((h + 1) * width)) - ((int) (h * width)) + leftOverlap + rightOverlap;
                int partHeight = ((int) ((v + 1) * height)) - ((int) (v * height)) + topOverlap + bottomOverlap;

                System.out.println("getSubimage(" + x + ", " + y + ", " + partWidth + ", " + partHeight + ")");

                BufferedImage part = img.getSubimage(x, y, partWidth, partHeight);

                parts.add(part);
            }
        }

        return parts;
    }

}
