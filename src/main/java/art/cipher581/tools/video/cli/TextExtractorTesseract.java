package art.cipher581.tools.video.cli;

import java.awt.image.BufferedImage;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

public class TextExtractorTesseract implements ITextExtractor {
	
	/**
	 * path to tessdata directory
	 */
	public final String dataPath;
	
	
	public TextExtractorTesseract(String dataPath) {
		super();

		this.dataPath = dataPath;
	}

	@Override
	public String getText(BufferedImage img) throws TextExtractionException {
		ITesseract instance = new Tesseract();

	    instance.setDatapath(dataPath);

	    try {
	        String result = instance.doOCR(img);
	        
	        result = result.trim();

	        return result;
	    } catch (Exception e) {
	        String message = "Error while extracting text from image";
	        throw new TextExtractionException(message, e);
	    }
	}
	
}
