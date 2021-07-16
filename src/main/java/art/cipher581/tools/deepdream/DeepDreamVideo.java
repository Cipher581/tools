package art.cipher581.tools.deepdream;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.imageio.ImageIO;
import art.cipher581.commons.util.FileUtilities;
import art.cipher581.commons.util.StringGenerator;
import art.cipher581.commons.util.img.IImageScaler;
import art.cipher581.commons.util.img.ImageUtils;
import art.cipher581.commons.util.img.JavaImageScaler;


public class DeepDreamVideo {

	private int width = 1920;

	private int height = 1080;

	private final File targetDir;

	private final File tempDir;

	private final IDeepDream deepDream;

	private IFileNameProvider fileNameProvider = new FileNameProvider();

	private IImageTransformator imageTransformatorBefore;

	private IImageTransformator imageTransformatorAfter;

	private IImageScaler imageScaler = new JavaImageScaler();

	private final IDeepDreamSettingsProvider deepDreamSettingsProvider;

	private final StringGenerator stringGeneratorTempFiles = new StringGenerator(16, 16, StringGenerator.ALPHANUMERIC);


	public DeepDreamVideo(File targetDir, IDeepDream deepDream, IDeepDreamSettingsProvider deepDreamSettingsProvider) {
		if (targetDir == null) {
			throw new IllegalStateException("targetDir is null");
		}
		if (deepDream == null) {
			throw new IllegalStateException("targetDir is null");
		}
		if (deepDreamSettingsProvider == null) {
			throw new IllegalStateException("deepDreamSettingsProvider is null");
		}

		this.targetDir = targetDir;
		this.tempDir = new File(targetDir, "tmp");
		this.deepDream = deepDream;
		this.deepDreamSettingsProvider = deepDreamSettingsProvider;
	}


	public void create(File file, int frameCount) throws DeepDreamException {
		createDir(targetDir);
		createDir(tempDir);

		try {
			File lastFile = getTargetFile(0);
			if (!lastFile.exists() && !file.getCanonicalPath().equals(lastFile.getCanonicalPath())) {
				Files.copy(file.toPath(), lastFile.toPath());
			}

			ensureSize(lastFile);

			for (int frameNr = 1; frameNr < frameCount; frameNr++) {
				File targetFile = getTargetFile(frameNr);

				if (targetFile.exists()) {
					System.out.println("frame " + frameNr + " already exists => skip");
					lastFile = targetFile;
					continue;
				}

				System.out.println("creating frame " + frameNr);

				long start = System.currentTimeMillis();

				File preprocessed = preProcess(lastFile, frameNr);
				File in = preprocessed == null ? lastFile : preprocessed;

				DeepDreamSettings settings = deepDreamSettingsProvider.getSettings(frameNr);

				if (settings != null) {
					deepDream.run(in, targetFile, settings);
				} else {
					Files.copy(in.toPath(), targetFile.toPath());
				}

				postProcess(targetFile, frameNr);

				long end = System.currentTimeMillis();

				System.out.println("duration: " + (end - start) + "ms");

				lastFile = targetFile;
			}
		} catch (IOException e) {
			throw new DeepDreamException("an IO error occured while creating deep dream", e);
		}
	}


	private void createDir(File dir) throws DeepDreamException {
		if (!dir.isDirectory()) {
			try {
				FileUtilities.createDir(dir);
			} catch (IOException e) {
				throw new DeepDreamException("error while creating directory " + dir, e);
			}
		}
	}


	private File preProcess(File file, int frameNr) throws IOException {
		System.out.println("pre-processing " + file);

		return process(file, null, frameNr, imageTransformatorBefore);
	}


	private void postProcess(File file, int frameNr) throws IOException {
		System.out.println("post-processing " + file);

		process(file, file, frameNr, imageTransformatorAfter);
	}


	private File process(File file, File target, int frameNr, IImageTransformator imageTransformator) throws IOException {
		if (imageTransformator != null) {
			BufferedImage image = ImageUtils.load(file);

			BufferedImage transformed = imageTransformator.transform(image, frameNr);

			if (transformed != null) {
				if (target == null) {
					String fileName = stringGeneratorTempFiles.generate() + "." + FileUtilities.getExtension(file);
					target = new File(tempDir, fileName);
				}

				transformed = imageScaler.scale(transformed, width, height);

				save(transformed, target);

				return target;
			}
		}

		ensureSize(file);

		return null;
	}


	private void ensureSize(File file) throws IOException {
		BufferedImage image = ImageUtils.load(file);

		if (image.getWidth() != width || image.getHeight() != height) {
			image = imageScaler.scale(image, width, height);

			save(image, file);
		}
	}


	private void save(BufferedImage image, File file) throws IOException {
		System.out.println("saving " + file);
		ImageIO.write(image, FileUtilities.getExtension(file), file);
	}


	public DeepDreamVideo withImageTransformatorAfter(IImageTransformator imageTransformatorAfter) {
		this.imageTransformatorAfter = imageTransformatorAfter;

		return this;
	}


	public DeepDreamVideo withImageTransformatorBefore(IImageTransformator imageTransformatorBefore) {
		this.imageTransformatorBefore = imageTransformatorBefore;

		return this;
	}


	public DeepDreamVideo withImageScaler(IImageScaler imageScaler) {
		this.imageScaler = imageScaler;

		return this;
	}


	private File getTargetFile(int frameNr) {
		return new File(targetDir, fileNameProvider.getFileName(frameNr));
	}


	public DeepDreamVideo withDimension(int width, int height) {
		this.width = width;
		this.height = height;

		return this;
	}

}
