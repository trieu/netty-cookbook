package netty.cookbook.chapter1.recipe4;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * Handler implementation for the TCP server.
 */
@Sharable
public class TcpServerInboundHandler extends ChannelInboundHandlerAdapter {
	

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
    	try {
    		System.out.println("TcpServerInboundHandler");
        	StringBuilder s = new StringBuilder();
        	s.append("Ok TCP client, TcpServerInboundHandler got your message \"").append(msg).append("\"");
            ctx.write(s);
    	} finally {
    		ReferenceCountUtil.release(msg);
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