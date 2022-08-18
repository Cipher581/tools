package art.cipher581.tools.video.cli.insta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.junit.Test;
import art.cipher581.commons.util.Safely;
import art.cipher581.commons.util.img.ImageUtils;

import com.github.kilianB.hash.Hash;
import com.github.kilianB.hashAlgorithms.AverageHash;
import com.github.kilianB.hashAlgorithms.HashingAlgorithm;

public class ImageHandlerInstagramTest {

	@Test
	public void test_reduceImage_001() throws Exception {
		BufferedImage a = loadImage("extractA1.jpg");
		BufferedImage b = loadImage("extractA2.jpg");

		assertEquals(ImageUtils.getMD5Hash(a), ImageUtils.getMD5Hash(a));
		assertFalse(ImageUtils.getMD5Hash(a).equals(ImageUtils.getMD5Hash(b)));
		
		HashingAlgorithm hashingAlgorithm = new AverageHash(4096);
		
		Hash hashA = hashingAlgorithm.hash(a);
		Hash hashB = hashingAlgorithm.hash(b);
		
		double distance = hashA.normalizedHammingDistance(hashB);
		
		assertTrue(distance < 0.005);
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
