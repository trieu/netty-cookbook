package chapter2.recipe7;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class HeartBeatHandler extends
		SimpleChannelInboundHandler<DatagramPacket> {
	private static final Queue<String> logQueue = new LinkedList<String>();
	static String log(String log) {
		return String.valueOf(logQueue.add(log));		
	}	
	static {
		new Timer(true).schedule(new TimerTask() {			
			@Override
			public void run() {
				while ( ! logQueue.isEmpty() ) {
					// log to Kafka or somewhere
					String s = logQueue.poll();
					if(s != null){
						System.out.println(s);	
					}
				}
			}
		}, 1000, 2000);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		// We don't close the channel because we can keep serving requests.
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
		System.err.println(packet);
		String s = packet.content().toString(CharsetUtil.UTF_8);
		System.out.println(s);
		ByteBuf buf = Unpooled.copiedBuffer("I'm alive at "+new Date(), CharsetUtil.UTF_8);
		ctx.write(new DatagramPacket(buf, packet.sender()));
	}
}
