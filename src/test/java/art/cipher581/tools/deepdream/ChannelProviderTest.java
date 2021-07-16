package art.cipher581.tools.deepdream;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ChannelProviderTest {

	@Test
	public void test_getChannel_001() {
		ChannelProvider p = new ChannelProvider(2, 4, new int[] {8, 8, 16, 32, 32, 64, 128}, 100, 4);
		
		// 5 frames per step
		assertEquals(new Channel(2, 0, 3, 1), p.getChannel(0));
		assertEquals(new Channel(2, 0, 3, 1),  p.getChannel(1));
		assertEquals(new Channel(2, 0, 3, 1),  p.getChannel(4));
		
		assertEquals(new Channel(2, 4, 7, 1),  p.getChannel(5));
		assertEquals(new Channel(2, 8, 11, 1),  p.getChannel(10));
		assertEquals(new Channel(2, 12, 15, 1),  p.getChannel(15));
		assertEquals(new Channel(2, 12, 15, 1),  p.getChannel(19));
		
		assertEquals(new Channel(3, 0, 3, 1),  p.getChannel(20));
		assertEquals(new Channel(3, 4, 7, 1),  p.getChannel(25));
		
		assertEquals(new Channel(4, 28, 31, 1),  p.getChannel(99));
	}

}
