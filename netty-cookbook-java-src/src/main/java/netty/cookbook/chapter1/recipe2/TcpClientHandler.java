package netty.cookbook.chapter1.recipe2;


import netty.cookbook.common.CallbackProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Handler implementation for the TCP client.  It initiates the ping-pong
 * traffic between the TCP client and server by sending the first message to
 * the server.
 */
public class TcpClientHandler extends SimpleChannelInboundHandler<String> {
	
	String message;
	CallbackProcessor asynchCall;
	boolean close = false;

    public TcpClientHandler(String message, CallbackProcessor asynchCall) {
    	this.message = message;
    	this.asynchCall = asynchCall;
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	ctx.writeAndFlush(this.message);
    }
    
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        this.asynchCall.process(msg);
        if("close".equals(msg)){
        	close = true;
        }
    }
   
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {    
    	System.out.println("channelRegistered "+ ctx.channel());
    }
    
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {    	
    	System.out.println("channelUnregistered "+ ctx.channel());
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {    	
    	System.out.println("channelInactive "+ctx.channel());
    }   

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
    	 ctx.flush();
         
         //close the connection after flushing data to client
    	 if(close){
    		 ctx.close();	 
    	 }         
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}