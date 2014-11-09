package netty.cookbook.recipe1;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;

/**
 * Handler implementation for the TCP server.
 */
@Sharable
public class TcpServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
    	System.out.println(msg);
    	
    	// TODO write your logic here
    	StringBuilder s = new StringBuilder();
    	s.append("Ok TCP client, got your message \"").append(msg.toString()).append("\"");
        ctx.write(s.toString());
    }
    
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {    	
    	super.channelRegistered(ctx);
    	InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
    	System.out.println("channelRegistered "+ address.getAddress());
    }
    
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {    	
    	super.channelUnregistered(ctx);
    	InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
    	System.out.println("channelUnregistered "+ address.getAddress());
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {    	
    	super.channelActive(ctx);
    	System.out.println("channelActive "+ctx.channel());
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {    	
    	super.channelInactive(ctx);
    	System.out.println("channelInactive "+ctx.channel());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
        
        //close the connection after flushing data to client
        //ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
