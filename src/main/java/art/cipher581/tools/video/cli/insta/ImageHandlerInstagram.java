package art.cipher581.tools.video.cli.insta;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import art.cipher581.commons.util.FileUtilities;
import art.cipher581.commons.util.img.ImageUtils;
import art.cipher581.tools.video.cli.IImageHandler;
import art.cipher581.tools.video.cli.ITextExtractor;
import art.cipher581.tools.video.cli.ImageHandlingException;

import com.github.kilianB.hash.Hash;
import com.github.kilianB.hashAlgorithms.AverageHash;
import com.github.kilianB.hashAlgorithms.HashingAlgorithm;


public class ImageHandlerInstagram implements IImageHandler<File> {

	private static final String IMAGE_FORMAT = "jpg";

	private static final Logger LOGGER = LogManager.getLogger(ImageHandlerInstagram.class);

	private final File outputDir;

	private InstagramImageExtractor imageExtractor = new InstagramImageExtractor();

	private HashingAlgorithm hashingAlgorithm = new AverageHash(4096);

	private LinkedList<Hash> hashes = new LinkedList<>();

	private double hashDistanceThreshold = 0.1;


	public ImageHandlerInstagram(File outputDir) {
		super();

		this.outputDir = outputDir;
		imageExtractor.setIgnoreErros(true);
	}


	public void setTextExtractor(ITextExtractor textExtractor) {
		imageExtractor.setTextExtractor(textExtractor);
	}


	public ImageHandlerInstagram withTextExtractor(ITextExtractor textExtractor) {
		imageExtractor.setTextExtractor(textExtractor);

		return this;
	}


	@Override
	public void init() throws ImageHandlingException {
		LOGGER.info("init");

		try {
			// FileHashesReader.readHashFiles(outputDir, fileHashes,
			// FILE_HASHES);
			if (outputDir.isDirectory()) {
				List<File> files = FileUtilities.getFiles(outputDir, Pattern.compile("(?i).*\\.jpg"), true);

				for (File file : files) {
					BufferedImage image = ImageUtils.load(file);

					Hash hash = hashingAlgorithm.hash(image);

					hashes.add(hash);
				}
			}
		} catch (IOException e) {
			String message = "Error while reading hash files in " + outputDir;
			throw new ImageHandlingException(message, e);
		}

		LOGGER.info("init - done");
	}


	@Override
	public File handle(BufferedImage image, int frameNr) throws ImageHandlingException {
		try {
			InstagramExtractResult extractResult = imageExtractor.extract(image);

			File file = null;
			if (extractResult != null && extractResult.getImage() != null) {
				image = extractResult.getImage();

				Hash hash = hashingAlgorithm.hash(image);

				String imageHash = ImageUtils.getMD5Hash(image);

				if (!isKnown(hash)) {
					LOGGER.info("new image at frame " + frameNr);

					File targetDir = outputDir;

					String accountName = extractResult.getAccountName();
					if (accountName != null && !accountName.trim().equals("")) {
						String subDirName = FileUtilities.replaceInvalidFileNameChars(accountName);

						targetDir = new File(outputDir, subDirName);
					}

					String fileName = imageHash + "." + IMAGE_FORMAT;

					if (!targetDir.exists()) {
						FileUtilities.createDir(targetDir);
					}

					file = new File(targetDir, fileName);

					ImageIO.write(image, IMAGE_FORMAT, file);

					hashes.add(hash);
				}
			}

			return file;
		} catch (Exception e) {
			String message = "Error while handling frame " + frameNr;
			throw new ImageHandlingException(message, e);
		}
	}


	private boolean isKnown(Hash hash) {
		for (Iterator<Hash> it = hashes.descendingIterator(); it.hasNext();) {
			Hash known = it.next();

			double distance = hash.normalizedHammingDistance(known);

			if (distance < hashDistanceThreshold) {
				return true;
			}
		}

		return false;
	}


	@Override
	public void finish() throws ImageHandlingException {
		// do nothing
	}

}
