package art.cipher581.tools.deepdream;


import java.io.Serializable;


public class Channel implements Serializable {

	/**
	 * SVUID
	 */
	private static final long serialVersionUID = 6421067130866592333L;

	private transient int layer;

	private int start;

	private int stop;

	private int step;


	public Channel(int layer, int start, int stop, int step) {
		super();

		this.layer = layer;
		this.start = start;
		this.stop = stop;
		this.step = step;
	}


	public int getLayer() {
		return layer;
	}


	public void setLayer(int layer) {
		this.layer = layer;
	}


	public int getStart() {
		return start;
	}


	public void setStart(int start) {
		this.start = start;
	}


	public int getStop() {
		return stop;
	}


	public void setStop(int stop) {
		this.stop = stop;
	}


	public int getStep() {
		return step;
	}


	public void setStep(int step) {
		this.step = step;
	}


	public int[] asArray() {
		return new int[] { start, stop, step };
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + layer;
		result = prime * result + start;
		result = prime * result + step;
		result = prime * result + stop;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Channel other = (Channel) obj;
		if (layer != other.layer)
			return false;
		if (start != other.start)
			return false;
		if (step != other.step)
			return false;
		if (stop != other.stop)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Channel [layer=" + layer + ", start=" + start + ", stop=" + stop + ", step=" + step + "]";
	}

}
