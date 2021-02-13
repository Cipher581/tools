package art.cipher581.tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import art.cipher581.commons.util.FileUtilities;
import art.cipher581.commons.util.img.IImageScaler;
import art.cipher581.commons.util.img.ImageUtils;
import art.cipher581.commons.util.img.JavaImageScaler;

public class ScaleImages {

	private File dir;

	private File destDir;

	private int targetWidth;

	private int targetHeight;

	private IImageScaler imageScaler = new JavaImageScaler(BufferedImage.SCALE_SMOOTH);

	public ScaleImages(File dir, File destDir, int targetWidth, int targetHeight) {
		super();

		this.dir = dir;
		this.destDir = destDir;
		this.targetWidth = targetWidth;
		this.targetHeight = targetHeight;
	}

	public void scale() throws IOException {
		List<File> files = FileUtilities.getFiles(dir, "(?i).*\\.jpg");

		for (File file : files) {
			BufferedImage img = ImageUtils.load(file);

			File destFile = new File(destDir, file.getName());

			if (destFile.exists()) {
				System.out.println("File " + destFile + " already exists");
			} else {
				if (!destDir.exists()) {
					FileUtilities.createDir(destDir);
				}

				System.out.println("Processing file " + file);

				BufferedImage scaled = imageScaler.scale(img, targetWidth, targetHeight);

				ImageIO.write(scaled, "jpg", destFile);
			}
		}
	}

}
