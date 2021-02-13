package art.cipher581.tools.pixelart.core;


public class ProjectLoadingException extends Exception {

    /**
	 * SVUID
	 */
	private static final long serialVersionUID = -71069513921968993L;


	public ProjectLoadingException(String message, Exception cause) {
        super(message, cause);
    }
    
    
    public ProjectLoadingException(String message) {
        super(message);
    }
}
