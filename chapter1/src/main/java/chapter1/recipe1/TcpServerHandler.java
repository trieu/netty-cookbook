package chapter1.recipe1;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

/**
 * Handler implementation for the TCP server.
 */
@Sharable
public class TcpServerHandler extends ChannelInboundHandlerAdapter {
	
	boolean isCatchedException = false;
	final static Logger logger = Logger.getLogger(TcpServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
    	logger.info(msg);
    	
    	if(msg.equals("")){
    		isCatchedException = true;    		
    		throw new IllegalArgumentException("msg is empty");
    	}
    	
    	// TODO write your logic here
    	StringBuilder s = new StringBuilder();
    	s.append("Ok TCP client, got your message \"").append(msg.toString()).append("\"");
        ctx.write(s.toString());
    }
    
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {    	
    	super.channelRegistered(ctx);
    	InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
    	logger.info("channelRegistered "+ address.getAddress());
    	isCatchedException = false;
    }
    
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {    	
    	super.channelUnregistered(ctx);
    	InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
    	logger.info("channelUnregistered "+ address.getAddress());
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {    	
    	super.channelActive(ctx);
    	logger.info("channelActive "+ctx.channel());
    	ctx.channel().writeAndFlush("connected");
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {    	
    	super.channelInactive(ctx);
    	logger.info("channelInactive "+ctx.channel().remoteAddress());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {    	
        ctx.flush();
        
        if( ! isCatchedException ){
        	//auto close the client connection after 500 mili-seconds
        	new Timer().schedule(new TimerTask() {			
     			@Override
     			public void run() {
     				ctx.channel().writeAndFlush("close");
     			}
     		}, 500);
        }
       
        
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
