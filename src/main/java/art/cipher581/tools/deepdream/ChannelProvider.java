package art.cipher581.tools.deepdream;


import java.util.Arrays;


public class ChannelProvider implements IChannelProvider {

	public static final int[] CHANNEL_COUNTS_VGG16 = new int[] { 64, 64, 128, 128, 256, 256, 256, 512, 512, 512, 512, 512, 512 };

	private int startLayer;

	private int endLayer;

	/**
	 * index = layer
	 */
	private int[] channelCounts;

	/**
	 * Total frames
	 */
	private int frameCount;

	/**
	 * Channel.end - Channel.start + 1
	 */
	private int range;


	public ChannelProvider(int startLayer, int endLayer, int[] channelCounts, int frameCount, int range) {
		super();

		this.startLayer = startLayer;
		this.endLayer = endLayer;
		this.channelCounts = channelCounts;
		this.frameCount = frameCount;
		this.range = range;
	}


	@Override
	public Channel getChannel(int frameNr) {
		// int steps = Arrays.stream(channelCounts, startLayer, endLayer).sum() - 2 * (range - 1) * (endLayer - startLayer + 1);
		int steps = Arrays.stream(channelCounts, startLayer, endLayer + 1).sum() / range;
		int framesPerStep = frameCount / steps;
		int step = frameNr / framesPerStep;

		int layerStartStep = 0;
		for (int l = startLayer; l <= endLayer; l++) {
			// int lSteps = channelCounts[l] - 2 * (range - 1) * (endLayer - startLayer + 1);
			int lSteps = channelCounts[l] / range;
			
			if (layerStartStep + lSteps > step) {
				int lStep = step - layerStartStep;

				int start = lStep * range;

				return new Channel(l, start, start + range - 1, 1);
			} else {
				layerStartStep += lSteps;
			}
		}

		return null;
	}

}
