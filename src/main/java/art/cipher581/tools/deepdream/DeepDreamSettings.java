package art.cipher581.tools.deepdream;


import java.io.Serializable;


/**
 * Arguments for the deep-dream call
 */
public class DeepDreamSettings implements Serializable {

	/**
	 * SVUID
	 */
	private static final long serialVersionUID = 3877780273073996437L;

	private String network;

	private int iterations;

	private int stepSize;

	private int tileSize;

	private double rescaleFactor;

	private int repeats;

	private double blend;

	private int layer;

	private Channel channel;

	private boolean recursive;


	public DeepDreamSettings() {
		super();
	}


	public DeepDreamSettings(String network, int iterations, int stepSize, int tileSize, double rescaleFactor, int repeats, double blend, int layer, Channel channel, boolean recursive) {
		super();

		this.network = network;
		this.iterations = iterations;
		this.stepSize = stepSize;
		this.tileSize = tileSize;
		this.rescaleFactor = rescaleFactor;
		this.repeats = repeats;
		this.blend = blend;
		this.layer = layer;
		this.channel = channel;
		this.recursive = recursive;
	}


	public String getNetwork() {
		return network;
	}


	public void setNetwork(String network) {
		this.network = network;
	}


	public int getIterations() {
		return iterations;
	}


	public void setIterations(int iterations) {
		this.iterations = iterations;
	}


	public int getStepSize() {
		return stepSize;
	}


	public void setStepSize(int stepSize) {
		this.stepSize = stepSize;
	}


	public int getTileSize() {
		return tileSize;
	}


	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}


	public double getRescaleFactor() {
		return rescaleFactor;
	}


	public void setRescaleFactor(double rescaleFactor) {
		this.rescaleFactor = rescaleFactor;
	}


	public int getRepeats() {
		return repeats;
	}


	public void setRepeats(int repeats) {
		this.repeats = repeats;
	}


	public double getBlend() {
		return blend;
	}


	public void setBlend(double blend) {
		this.blend = blend;
	}


	public int getLayer() {
		return layer;
	}


	public void setLayer(int layer) {
		this.layer = layer;
	}


	public Channel getChannel() {
		return channel;
	}


	public void setChannel(Channel channel) {
		this.channel = channel;
	}


	public boolean isRecursive() {
		return recursive;
	}


	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}

}
