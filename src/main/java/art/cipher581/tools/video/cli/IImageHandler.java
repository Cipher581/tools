package art.cipher581.tools.video.cli;

import java.awt.image.BufferedImage;

public interface IImageHandler<E> {

	public void init() throws ImageHandlingException;

	public E handle(BufferedImage image, int frameNr) throws ImageHandlingException;
	
	public void finish() throws ImageHandlingException;

}
