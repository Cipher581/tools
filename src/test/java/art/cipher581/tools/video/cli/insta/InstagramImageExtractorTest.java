package art.cipher581.tools.video.cli.insta;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.junit.Test;
import art.cipher581.commons.util.Safely;
import art.cipher581.commons.util.img.ImageEqualityImageHash;
import art.cipher581.tools.video.cli.TextExtractorTesseract;
import art.cipher581.tools.video.cli.TextExtractorTesseractTest;


public class InstagramImageExtractorTest {

	@Test
	public void test_pattern_001() throws Exception {
		String s = "0001111100";

		Matcher m = Pattern.compile("1{3,}").matcher(s);

		boolean b = m.find();

		assertTrue(b);
		assertEquals(3, m.start());
		assertEquals(8, m.end());
	}


	@Test
	public void test_pattern_002() throws Exception {
		String s = "0001111100";

		Matcher m = Pattern.compile("1{6,}").matcher(s);

		boolean b = m.find();

		assertFalse(b);
	}

	
	private void checkImageEquality(BufferedImage a, BufferedImage b) {
		assertTrue(ImageEqualityImageHash.averageHash().equals(a, b));
	}

	@Test
	public void test_001() throws Exception {
		BufferedImage image = loadImage("insta1.png");

		InstagramExtractResult extractResult = new InstagramImageExtractor().extract(image);

		assertNotNull(extractResult);
		assertNotNull(extractResult.getImage());

		BufferedImage expected = loadImage("insta1_extract.png");

		checkImageEquality(expected, extractResult.getImage());
	}

	@Test
	public void test_002() throws Exception {
		BufferedImage image = loadImage("insta1.png");

		TextExtractorTesseract textExtractor = new TextExtractorTesseract(TextExtractorTesseractTest.tessDataStr);
		
		InstagramExtractResult extractResult = new InstagramImageExtractor().withTextExtractor(textExtractor).extract(image);

		assertNotNull(extractResult);
		assertNotNull(extractResult.getImage());

		assertNotNull(extractResult.getAccountName());
		assertTrue("Unexpected account name: " + extractResult.getAccountName(), extractResult.getAccountName().matches("(?i)^cipher[s5]81$"));

		BufferedImage expected = loadImage("insta1_extract.png");

		checkImageEquality(expected, extractResult.getImage());
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
