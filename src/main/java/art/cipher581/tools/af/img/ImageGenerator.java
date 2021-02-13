package art.cipher581.tools.af.img;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import art.cipher581.tools.af.element.IElement;
import art.cipher581.tools.af.element.gen.IElementsGenerator;


public class ImageGenerator implements IImageGenerator {

	private IElementsGenerator elementsGenerator;

	private int width = 1920;

	private int height = 1080;

	private Color backgroundColor = Color.WHITE;


	public ImageGenerator(IElementsGenerator elementsGenerator) {
		super();

		this.elementsGenerator = elementsGenerator;
	}


	@Override
	public BufferedImage generate() {
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		Graphics g = bufferedImage.getGraphics();

		g.setColor(backgroundColor);
		g.fillRect(0, 0, width, height);
		
		List<IElement> elements = elementsGenerator.generate(width, height);
		
		for (IElement element : elements) {
			element.paint(g);
		}

		return bufferedImage;
	}


	public int getWidth() {
		return width;
	}


	public void setWidth(int width) {
		this.width = width;
	}


	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
	}


	public Color getBackgroundColor() {
		return backgroundColor;
	}


	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	
	public IElementsGenerator getElementsGenerator() {
		return elementsGenerator;
	}
	
	
	public void setElementsGenerator(IElementsGenerator elementsGenerator) {
		this.elementsGenerator = elementsGenerator;
	}

}
