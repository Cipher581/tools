package art.cipher581.common.color;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import art.cipher581.commons.da.DataAccessException;
import art.cipher581.commons.gui.util.ColorDistanceProviderCIEDE2000;
import art.cipher581.commons.gui.util.IColorDistanceProvider;
import art.cipher581.commons.util.img.ImageUtils;
import art.cipher581.commons.util.img.JavaImageScaler;

public class PixelateImage {

	public PixelatedImage pixelate(File imageFile, String colorSetResource, int width, int height) throws IOException, DataAccessException {
		BufferedImage image = ImageUtils.load(imageFile);
		
		ColorSet colorSet = new ColorXmlDao().loadColorSet(colorSetResource);
		
		return pixelate(image, colorSet.getActiveColors(), width, height);
	}
	
	public PixelatedImage pixelate(BufferedImage image, Collection<Color> colors, int width, int height) {
//		System.out.println("Active colors: " + colors.size());
//		for (Color color : colors) {
//			System.out.println(color.getName() + ": " + color.getAwtColor().getRed() + ", " + color.getAwtColor().getGreen() + ", " + color.getAwtColor().getBlue());
//			ImageFrame.showAndWait(ImageUtils.createImage(1000, 800, color.getAwtColor()));
//		}
		
		PixelatedImage pImg = new PixelatedImage(width, height);
		
		BufferedImage scaledImage = new JavaImageScaler().scale(image, width, height);
		
//		ImageFrame.showAndWait(scaledImage);
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				java.awt.Color c = ImageUtils.getColor(scaledImage, x, y);
				
				Color color = getMatchingColor(colors, c);
				
				pImg.setColor(x, y, color);
			}
		}
		
		return pImg;
	}

	private Color getMatchingColor(Collection<Color> colors, java.awt.Color c) {
		IColorDistanceProvider distanceProvider = new ColorDistanceProviderCIEDE2000();
		
		Color bestMatching = null;
		double bestMatchingDistance = 0.0;

		for (Color color : colors) {
			double distance = distanceProvider.getDistance(c, color.getAwtColor());
			// System.out.println(distance);
			
			if (bestMatching == null || bestMatchingDistance > distance) {
				bestMatching = color;
				bestMatchingDistance = distance;
			}
		}

		return bestMatching;
	}

}
