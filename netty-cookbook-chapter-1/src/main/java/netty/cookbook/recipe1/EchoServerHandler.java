package netty.cookbook.recipe1;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

/**
 * Handler implementation for the echo server.
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
    	System.out.println(msg);
        ctx.write(msg.toString().toUpperCase());
    }
    
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    	// TODO Auto-generated method stub
    	super.channelRegistered(ctx);
    	InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
    	System.out.println("channelRegistered "+ address.getAddress());
    }
    
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
    	// TODO Auto-generated method stub
    	super.channelUnregistered(ctx);
    	InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
    	System.out.println("channelUnregistered "+ address.getAddress());
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	// TODO Auto-generated method stub
    	super.channelActive(ctx);
    	System.out.println("channelActive "+ctx.channel());
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	// TODO Auto-generated method stub
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
