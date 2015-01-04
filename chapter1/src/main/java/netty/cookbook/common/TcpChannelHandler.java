package netty.cookbook.common;

import io.netty.channel.ChannelHandlerContext;

@FunctionalInterface
public interface TcpChannelHandler {    	
	public void process(ChannelHandlerContext ctx, Object msg);
}
