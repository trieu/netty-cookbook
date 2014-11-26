package netty.cookbook.chapter2.recipe1;

import netty.cookbook.common.PurchaseData;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class PurchaseServer {
	 static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));
	 public static void main(String[] args) throws Exception {    	       
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            ChannelFuture f = b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline p = ch.pipeline();	                    
                     p.addLast("decoder", new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)));	                     
                     p.addLast("encoder", new ObjectEncoder());  
                     p.addLast(new ChannelInboundHandlerAdapter(){
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
