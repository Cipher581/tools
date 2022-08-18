package art.cipher581.tools.video.cli;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ImageHandlerSave implements IImageHandler<File> {

	private static final Logger LOGGER = LogManager.getLogger(ImageHandlerSave.class);

	private final File outputDir;


	public ImageHandlerSave(File outputDir) {
		super();

		this.outputDir = outputDir;
	}
	
	@Override
	public void init() {
		LOGGER.info("init");
	}


	@Override
	public File handle(BufferedImage image, int frameNr) throws ImageHandlingException {
		String fileName = "frame_" + frameNr + ".jpg";

		File file = new File(outputDir, fileName);

		try {
			ImageIO.write(image, "jpg", file);
		} catch (IOException e) {
			String message = "Error while saving frame " + frameNr;
			throw new ImageHandlingException(message, e);
		}
		
		return file;
	}
	
	@Override
	public void finish() {
		LOGGER.info("finish");
	}

}
