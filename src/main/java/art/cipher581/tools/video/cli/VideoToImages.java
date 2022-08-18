package art.cipher581.tools.video.cli;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.FileChannelWrapper;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import art.cipher581.commons.util.ArgumentUtilities;
import art.cipher581.commons.util.FileUtilities;


public class VideoToImages {

	private static final Logger LOGGER = LogManager.getLogger(VideoToImages.class);


	public void run(File videoFile, int stepSize, IImageHandler<?> imageHandler) throws JCodecException, IOException, ImageHandlingException {
		try (FileChannelWrapper fc = NIOUtils.readableChannel(videoFile)) {
			FrameGrab grab = FrameGrab.createFrameGrab(fc);
			
			Picture picture;

			try {
				imageHandler.init();

				int frameNr = 0;
				while (null != (picture = grab.getNativeFrame())) {
					if (frameNr % stepSize == 0) {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Frame: " + frameNr);
							LOGGER.debug(picture.getWidth() + "x" + picture.getHeight() + " " + picture.getColor());
						}

						BufferedImage im = AWTUtil.toBufferedImage(picture);

						imageHandler.handle(im, frameNr);
					}

					frameNr++;
				}
			} finally {
				imageHandler.finish();
			}
		}
	}


	public static void main(String[] args) {
		try {
			ArgumentUtilities argUtil = new ArgumentUtilities(args);

			String videoFileStr = argUtil.getValue("videoFile", ArgumentUtilities.VALUE_SEPARATED_BY_EQUALSIGN);
			String outputDirStr = argUtil.getValue("outputDir", ArgumentUtilities.VALUE_SEPARATED_BY_EQUALSIGN);
			int stepSize = argUtil.getValueAsInteger("stepSize", ArgumentUtilities.VALUE_SEPARATED_BY_EQUALSIGN, 1);

			if (videoFileStr == null || videoFileStr.trim().isEmpty()) {
				throw new Exception("Argument 'videoFile' is missing or has no value");
			}
			File videoFile = new File(videoFileStr);

			File outputDir;
			if (outputDirStr == null || outputDirStr.trim().isEmpty()) {
				System.out.println("Argument 'outputDir' is missing or has no value. Using current directory ()");

				outputDir = new File(".");
			} else {
				outputDir = new File(outputDirStr);
			}

			if (!videoFile.isFile()) {
				throw new Exception("The file " + videoFile.getCanonicalPath() + " does not exist");
			}

			if (!outputDir.isDirectory()) {
				FileUtilities.createDir(outputDir);
			}

			IImageHandler<?> imageHandler = new ImageHandlerSave(outputDir);

			new VideoToImages().run(videoFile, stepSize, imageHandler);

			System.exit(0);
		} catch (Exception ex) {
			ex.printStackTrace();

			System.exit(8);
		}
	}

}
