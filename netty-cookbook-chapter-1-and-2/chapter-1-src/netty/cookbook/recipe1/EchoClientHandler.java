package netty.cookbook.recipe1;


import java.util.function.Function;

import netty.cookbook.recipe1.EchoClient.AsynchCall;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Handler implementation for the echo client.  It initiates the ping-pong
 * traffic between the echo client and server by sending the first message to
 * the server.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {
	
	AsynchCall asynchCall;

    /**
     * Creates a client-side handler.
     */
    public EchoClientHandler(AsynchCall asynchCall) {
    	this.asynchCall = asynchCall;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(asynchCall.getMessage());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        this.asynchCall.apply(msg.toString());
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