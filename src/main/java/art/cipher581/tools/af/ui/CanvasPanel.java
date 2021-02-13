package art.cipher581.tools.af.ui;


import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class CanvasPanel extends JPanel {

	/**
	 * SVUID
	 */
	private static final long serialVersionUID = -3364303518657191937L;

	private BufferedImage image;
	
	private Image renderImage;


	@Override
	public void paint(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		
		if (renderImage == null) {
			renderImage = image;
		}

		if (width != renderImage.getWidth(null) || height != renderImage.getHeight(null)) {
			renderImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		}
		
		g.drawImage(renderImage, 0, 0, null);
	}


	public BufferedImage getImage() {
		return image;
	}


	public void setImage(BufferedImage image) {
		this.image = image;
		this.renderImage = null;

		revalidate();
		repaint();
	}

}
