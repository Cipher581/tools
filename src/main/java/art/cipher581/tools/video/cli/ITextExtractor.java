package art.cipher581.tools.video.cli;

import java.awt.image.BufferedImage;

public interface ITextExtractor {

	public String getText(BufferedImage img) throws TextExtractionException;
	
}
