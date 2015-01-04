package chapter2.recipe8;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.sctp.SctpMessage;
import io.netty.util.CharsetUtil;

/**
 * Handler implementation for the SCTP echo server.
 */
@Sharable
public class SimpleSctpServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
    	System.out.println(msg);
    	if(msg instanceof SctpMessage){ 
    		SctpMessage sctpMsg = (SctpMessage) msg;
    		System.out.println(sctpMsg.content().toString(CharsetUtil.UTF_8));
    		ctx.write(sctpMsg); 
    	}    	
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
