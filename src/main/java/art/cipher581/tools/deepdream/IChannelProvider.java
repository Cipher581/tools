package art.cipher581.tools.deepdream;


@FunctionalInterface
public interface IChannelProvider {

	public Channel getChannel(int frameNr);

}
