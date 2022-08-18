package art.cipher581.tools.video.cli.insta;


import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import art.cipher581.commons.util.img.ImageUtils;
import art.cipher581.tools.video.cli.ITextExtractor;
import art.cipher581.tools.video.cli.TextExtractionException;


public class InstagramImageExtractor {

	private Color bgColorMetaBox = Color.WHITE;

	private int bgColorMetaBoxMaxDiff = 5;

	private Color bgColor = new Color(127, 127, 127);

	private int bgColorMaxDiff = 5;

	private Pattern bgColorMetaBoxLinePattern = Pattern.compile("1{100,}");

	private Pattern bgColorLinePattern = Pattern.compile("1{500,}");

	private ITextExtractor textExtractor;

	private boolean ignoreErrors;


	public void setTextExtractor(ITextExtractor textExtractor) {
		this.textExtractor = textExtractor;
	}


	public InstagramImageExtractor withTextExtractor(ITextExtractor textExtractor) {
		this.textExtractor = textExtractor;

		return this;
	}


	public InstagramExtractResult extract(BufferedImage image) throws InstagramExtractException {
		try {
			int beginY = -1;
			int windowWidth = -1;

			for (int y = 0; y < image.getHeight(); y++) {
				// System.out.println("y: " + y);
				String bgString = getBgString(image, y);

				Matcher m = bgColorLinePattern.matcher(bgString);

				if (m.find()) {
					// System.out.println("match!!");
					beginY = y;
					windowWidth = m.end() - m.start();
					break;
				}
			}

			if (beginY < 0 || windowWidth < 0) {
				throw new InstagramExtractException("window area can not be detected");
			}

			// System.out.println("beginY: " + beginY);
			// System.out.println("windowWidth: " + windowWidth);

			Rectangle metaBox = getMetaBox(beginY, image);

			if (metaBox == null) {
				throw new InstagramExtractException("meta box can not be detected");
			}

			// System.out.println("metaBox: " + metaBox);

			int imageStartX = windowWidth - (((int) metaBox.getX()) + ((int) metaBox.getWidth()));

			// System.out.println("imageStartX: " + imageStartX);

			int subImageWidth = ((int) metaBox.getX()) - imageStartX;

			BufferedImage subImage = image.getSubimage(imageStartX, (int) metaBox.getY(), subImageWidth, (int) metaBox.getHeight());

			subImage = ImageUtils.removeBlackBars(subImage);

			String accountName = getAccountName(image, metaBox);

			return new InstagramExtractResult(subImage, accountName);
		} catch (Exception e) {
			if (ignoreErrors) {
				return null;
			} else {
				throw new InstagramExtractException("error while extracting image from screen", e);
			}
		}
	}


	private String getAccountName(BufferedImage image, Rectangle metaBox) throws InstagramExtractException {
		if (textExtractor != null) {
			BufferedImage subImage = image.getSubimage(metaBox.x + 50, metaBox.y, (int) metaBox.getWidth() - 80, 70);

			// art.cipher581.commons.gui.ImageFrame.showAndWait(subImage);

			try {
				String text = textExtractor.getText(subImage);

				text = text.replaceAll("\n", "").replaceAll("\r", "").trim();
				text = text.replaceAll("^@", "");
				text = text.replaceAll("[\\-\\+]*\\s*Abonniert.*$", "");
				text = text.trim();

				return text;
			} catch (TextExtractionException e) {
				String message = "error while extracting account name";
				throw new InstagramExtractException(message, e);
			}
		}

		return null;
	}


	private Rectangle getMetaBox(int beginY, BufferedImage image) {
		int startX = -1;
		int startY = -1;
		int width = -1;

		for (int y = beginY; y < image.getHeight(); y++) {
			Matcher m = bgColorMetaBoxLinePattern.matcher(getMetaBoxString(image, y));

			if (m.find()) {
				startY = y;
				startX = m.start();

				width = m.end() - startX;

				break;
			}
		}

		if (startY >= 0) {
			int height = -1;

			for (int y = image.getHeight() - 1; y >= beginY; y--) {
				Matcher m = bgColorMetaBoxLinePattern.matcher(getMetaBoxString(image, y, startX));

				if (m.find()) {
					height = y - startY;

					break;
				}
			}

			return new Rectangle(startX, startY, width, height);
		}

		return null;
	}


	private String getBgString(BufferedImage image, int y) {
		return getColorMatchingString(bgColor, bgColorMaxDiff, image, y, 0);
	}


	private String getMetaBoxString(BufferedImage image, int y) {
		return getColorMatchingString(bgColorMetaBox, bgColorMetaBoxMaxDiff, image, y, 0);
	}


	private String getMetaBoxString(BufferedImage image, int y, int startX) {
		return getColorMatchingString(bgColorMetaBox, bgColorMetaBoxMaxDiff, image, y, startX);
	}


	private String getColorMatchingString(Color expected, int maxDiff, BufferedImage image, int y, int startX) {
		StringBuilder sb = new StringBuilder(image.getWidth());
		for (int x = startX; x < image.getWidth(); x++) {
			char c = '0';

			Color color = new Color(image.getRGB(x, y));

			if (matches(color, expected, maxDiff)) {
				c = '1';
			}

			sb.append(c);
		}
		return sb.toString();
	}


	private boolean matches(Color color, Color expected, int maxDiff) {
		boolean r = color.getRed() >= expected.getRed() - maxDiff && color.getRed() <= expected.getRed() + maxDiff;
		boolean g = color.getGreen() >= expected.getGreen() - maxDiff && color.getGreen() <= expected.getGreen() + maxDiff;
		boolean b = color.getBlue() >= expected.getBlue() - maxDiff && color.getBlue() <= expected.getBlue() + maxDiff;

		return r && g && b;
	}


	public void setIgnoreErros(boolean ignoreErrors) {
		this.ignoreErrors = ignoreErrors;
	}

}
