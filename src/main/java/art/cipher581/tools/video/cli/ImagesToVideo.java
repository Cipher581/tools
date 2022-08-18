package art.cipher581.tools.video.cli;


import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Rational;
import art.cipher581.commons.util.ArgumentUtilities;
import art.cipher581.commons.util.FileComparatorCreation;
import art.cipher581.commons.util.FileComparatorExtractSortNumber;
import art.cipher581.commons.util.FileUtilities;
import art.cipher581.commons.util.Safely;
import art.cipher581.commons.util.img.ImageUtils;


public class ImagesToVideo {

	public static void main(String[] args) {
		try {
			ArgumentUtilities argUtil = new ArgumentUtilities(args);

			String destFileStr = argUtil.getValue("destFile", ArgumentUtilities.VALUE_SEPARATED_BY_EQUALSIGN);
			String imageDirStr = argUtil.getValue("imageDir", ArgumentUtilities.VALUE_SEPARATED_BY_EQUALSIGN);
			String fpsStr = argUtil.getValue("fps", ArgumentUtilities.VALUE_SEPARATED_BY_EQUALSIGN);

			if (destFileStr == null || destFileStr.trim().isEmpty()) {
				throw new Exception("Argument 'destFile' is missing or has no value");
			}

			if (imageDirStr == null || imageDirStr.trim().isEmpty()) {
				throw new Exception("Argument 'imageDir' is missing or has no value");
			}

			File destFile = new File(destFileStr);
			if (destFile.exists()) {
				throw new Exception(destFile.getAbsolutePath() + " already exists");
			} else {
				if (!destFile.getParentFile().exists()) {
					FileUtilities.createDir(destFile.getParentFile());
				}
			}

			File imageDir = new File(imageDirStr);
			if (!imageDir.isDirectory()) {
				throw new Exception(imageDir.getAbsolutePath() + " does not exist or is not a directory");
			}
			
			int fps = 25;
			if (fpsStr == null || fpsStr.trim().equals("")) {
				fps = Integer.parseInt(fpsStr);
			}

			Comparator<File> comparator = new FileComparatorCreation();
			if (argUtil.isArgumentSet("sortByFrameNr")) {
				// comparator = new FileComparatorExtractSortNumber("(?i).+frame_([0-9]+)_.+");
				comparator = new FileComparatorExtractSortNumber("(?i).*frame-([0-9]+)\\..+");
			}

			SeekableByteChannel out = null;
			try {
				out = NIOUtils.writableFileChannel(destFile.getAbsolutePath());

				AWTSequenceEncoder encoder = new AWTSequenceEncoder(out, Rational.R(fps, 1));

				List<File> files = FileUtilities.getFiles(imageDir, "(?i)^.+\\.(png|jpg)$");
				
				Collections.sort(files, comparator);

				int fileNr = 1;
				for (File file : files) {
					System.out.println("Processing " + fileNr + " of " + files.size() + " (" + file + ")");

					BufferedImage image = ImageUtils.load(file);
					encoder.encodeImage(image);

					fileNr++;
				}

				encoder.finish();
			} finally {
				Safely.close(out);
			}

			System.exit(0);
		} catch (Exception ex) {
			ex.printStackTrace();

			System.exit(8);
		}
	}

}
