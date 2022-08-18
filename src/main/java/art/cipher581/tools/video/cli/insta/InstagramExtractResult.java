package art.cipher581.tools.video.cli.insta;


import java.awt.image.BufferedImage;


public class InstagramExtractResult {

	private final BufferedImage image;

	private final String accountName;


	public InstagramExtractResult(BufferedImage image, String accountName) {
		super();

		this.image = image;
		this.accountName = accountName;
	}


	public BufferedImage getImage() {
		return image;
	}


	public String getAccountName() {
		return accountName;
	}

}
