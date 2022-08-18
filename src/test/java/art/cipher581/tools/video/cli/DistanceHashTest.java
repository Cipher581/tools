package art.cipher581.tools.video.cli;


import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;

import org.junit.Test;
import art.cipher581.commons.util.img.ImageUtils;

import com.github.kilianB.hash.Hash;
import com.github.kilianB.hashAlgorithms.AverageHash;
import com.github.kilianB.hashAlgorithms.HashingAlgorithm;


public class DistanceHashTest {


	@Test
	public void test_001() {
		try {
			BufferedImage a = ImageUtils.load(DistanceHashTest.class.getResourceAsStream("insta/extractA1.jpg"));
			BufferedImage b = ImageUtils.load(DistanceHashTest.class.getResourceAsStream("insta/extractA2_edit1.jpg"));
			
			HashingAlgorithm hashingAlgorithm = new AverageHash(4096);
			
			Hash hashA = hashingAlgorithm.hash(a);
			Hash hashB = hashingAlgorithm.hash(b);
			
			double distance = hashA.normalizedHammingDistance(hashB);
			
			assertTrue(distance < 0.005);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
