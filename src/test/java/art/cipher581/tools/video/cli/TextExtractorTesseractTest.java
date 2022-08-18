package art.cipher581.tools.video.cli;

import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.junit.Test;
import art.cipher581.commons.util.Safely;

public class TextExtractorTesseractTest {

	// TODO: NOT HARD-CODED!
	public static final String tessDataStr = "C:\\Program Files\\Tesseract-OCR\\tessdata";

	@Test
	public void test_001() throws Exception {
		BufferedImage image = loadImage("accountInfo1.png");

		TextExtractorTesseract textExtractor = new TextExtractorTesseract(tessDataStr);

		String t = textExtractor.getText(image);

		assertTrue("Text: " + t, t.matches(".*tank city pirates.*Abonniert.*"));
	}
	
	@Test
	public void test_002() throws Exception {
		BufferedImage image = loadImage("accountInfo2.png");

		TextExtractorTesseract textExtractor = new TextExtractorTesseract(tessDataStr);

		String t = textExtractor.getText(image);

		assertTrue("Text: " + t, t.matches(".*tuff.city.kids.*Abonniert.*"));
	}
	
	
	private BufferedImage loadImage(String resource) throws IOException {
		InputStream is = getClass().getResourceAsStream(resource);

		try {
			return ImageIO.read(is);
		} finally {
			Safely.close(is);
		}
	}

}
