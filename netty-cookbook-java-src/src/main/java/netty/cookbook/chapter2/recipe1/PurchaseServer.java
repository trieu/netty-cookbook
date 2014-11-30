package netty.cookbook.chapter2.recipe1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

import netty.cookbook.common.PurchaseData;

import org.apache.log4j.Logger;

public class PurchaseServer {
	final static Logger logger = Logger.getLogger(PurchaseServer.class);
	 static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));
	 public static void main(String[] args) throws Exception {    	       
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline p = ch.pipeline();	                    
                     p.addLast("decoder", new PurchaseDataDecoder());	                     
                     p.addLast("encoder", new PurchaseDataEncoder());  
                     p.addLast(new ChannelInboundHandlerAdapter(){
                    	 @Override
                    	public void channelActive(ChannelHandlerContext ctx)
                    			throws Exception {
                    		 InetSocketAddress localAddr = (InetSocketAddress) ctx.channel().localAddress();
                    		 logger.info(String.format("localAddress.hostname %s",localAddr.getHostName()));
                    		 logger.info(String.format("localAddress.port %s",localAddr.getPort()));
                    		 
                    		 InetSocketAddress remoteAddr = (InetSocketAddress) ctx.channel().remoteAddress();
                    		 logger.info(String.format("remoteAddress.hostname %s",remoteAddr.getHostName()));
                    		 logger.info(String.format("remoteAddress.port %s",remoteAddr.getPort()));
                    	}
                    	 @Override
                    	 public void channelRead(ChannelHandlerContext ctx, Object data) throws Exception {
                    		 System.out.println("processed Purchase "+data);
                    		 PurchaseData processed = new PurchaseData(data, true);
                    		 ctx.writeAndFlush(processed);
                    	 }	                    	 
                     });
                 }
             }).bind(PORT).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
	 }
}
